package com.kt_giga_fms.user.service;

import com.kt_giga_fms.user.dto.*;
import com.kt_giga_fms.user.entity.User;
import com.kt_giga_fms.user.entity.UserPermission;
import com.kt_giga_fms.user.entity.UserActivityLog;
import com.kt_giga_fms.user.repository.UserRepository;
import com.kt_giga_fms.user.repository.UserPermissionRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional(readOnly = true)
public class UserService {
    
    private final UserRepository userRepository;
    private final UserPermissionRepository userPermissionRepository;
    private final UserActivityLogService userActivityLogService;
    
    // 사용자 검색 (대시보드 메인 기능)
    public UserSearchResponse searchUsers(UserSearchRequest request) {
        log.info("사용자 검색 요청: {}", request);
        
        // 페이징 및 정렬 설정
        Sort sort = Sort.by(
            Sort.Direction.fromString(request.getSortDirection()),
            request.getSortBy()
        );
        Pageable pageable = PageRequest.of(request.getPage(), request.getSize(), sort);
        
        // 검색 조건으로 사용자 조회
        Page<User> userPage = userRepository.findBySearchCriteria(
            request.getSearchKeyword(),
            request.getStatus(),
            request.getRole(),
            request.getDepartment(),
            request.getVehicleType(),
            request.getDriverLicense(),
            pageable
        );
        
        // 통계 정보 조회
        UserStatisticsDto statistics = getUserStatistics();
        
        // 응답 생성
        List<UserDto> userDtos = userPage.getContent().stream()
            .map(UserDto::fromEntity)
            .collect(Collectors.toList());
        
        return UserSearchResponse.builder()
            .users(userDtos)
            .totalElements(userPage.getTotalElements())
            .totalPages(userPage.getTotalPages())
            .currentPage(userPage.getNumber())
            .size(userPage.getSize())
            .statistics(statistics)
            .build();
    }
    
    // 사용자 통계 조회
    public UserStatisticsDto getUserStatistics() {
        long totalUsers = userRepository.count();
        long activeUsers = userRepository.countActiveUsers();
        long corporateVehicleUsers = userRepository.countCorporateVehicleUsers();
        long class1LicenseHolders = userRepository.countClass1LicenseHolders();
        
        return UserStatisticsDto.builder()
            .totalUsers(totalUsers)
            .activeUsers(activeUsers)
            .corporateVehicleUsers(corporateVehicleUsers)
            .class1LicenseHolders(class1LicenseHolders)
            .build();
    }
    
    // 사용자 상세 조회
    public UserDto getUserById(Long id) {
        User user = userRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("사용자를 찾을 수 없습니다: " + id));
        
        return UserDto.fromEntity(user);
    }
    
    // 사용자 생성
    @Transactional
    public UserDto createUser(UserCreateRequest request) {
        log.info("사용자 생성 요청: {}", request);
        
        // 중복 검사
        if (userRepository.existsByUserId(request.getUserId())) {
            throw new RuntimeException("이미 존재하는 사용자 ID입니다: " + request.getUserId());
        }
        
        if (request.getEmail() != null && userRepository.existsByEmail(request.getEmail())) {
            throw new RuntimeException("이미 존재하는 이메일입니다: " + request.getEmail());
        }
        
        // 사용자 엔티티 생성
        User user = User.builder()
            .userId(request.getUserId())
            .name(request.getName())
            .email(request.getEmail())
            .phone(request.getPhone())
            .department(request.getDepartment())
            .role(request.getRole())
            .driverLicense(request.getDriverLicense())
            .vehicleType(request.getVehicleType())
            .status(User.UserStatus.valueOf(request.getStatus()))
            .createdBy("admin") // TODO: 실제 인증된 사용자 정보 사용
            .build();
        
        User savedUser = userRepository.save(user);
        
        // 권한 설정
        if (request.getPermissions() != null) {
            setUserPermissions(savedUser, request.getPermissions());
        }
        
        // 활동 로그 기록
        userActivityLogService.logUserAction(savedUser.getId(), "USER_CREATED", "새 사용자 생성");
        
        return UserDto.fromEntity(savedUser);
    }
    
    // 사용자 수정
    @Transactional
    public UserDto updateUser(Long id, UserUpdateRequest request) {
        log.info("사용자 수정 요청: ID={}, 요청={}", id, request);
        
        User user = userRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("사용자를 찾을 수 없습니다: " + id));
        
        // 필드 업데이트
        if (request.getName() != null) user.setName(request.getName());
        if (request.getEmail() != null) user.setEmail(request.getEmail());
        if (request.getPhone() != null) user.setPhone(request.getPhone());
        if (request.getDepartment() != null) user.setDepartment(request.getDepartment());
        if (request.getRole() != null) user.setRole(request.getRole());
        if (request.getDriverLicense() != null) user.setDriverLicense(request.getDriverLicense());
        if (request.getVehicleType() != null) user.setVehicleType(request.getVehicleType());
        if (request.getStatus() != null) user.setStatus(User.UserStatus.valueOf(request.getStatus()));
        
        user.setUpdatedBy("admin"); // TODO: 실제 인증된 사용자 정보 사용
        
        User updatedUser = userRepository.save(user);
        
        // 권한 업데이트
        if (request.getPermissions() != null) {
            updateUserPermissions(user, request.getPermissions());
        }
        
        // 활동 로그 기록
        userActivityLogService.logUserAction(user.getId(), "USER_UPDATED", "사용자 정보 수정");
        
        return UserDto.fromEntity(updatedUser);
    }
    
    // 사용자 삭제
    @Transactional
    public void deleteUser(Long id) {
        log.info("사용자 삭제 요청: ID={}", id);
        
        User user = userRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("사용자를 찾을 수 없습니다: " + id));
        
        userRepository.delete(user);
        
        // 활동 로그 기록
        userActivityLogService.logUserAction(id, "USER_DELETED", "사용자 삭제");
    }
    
    // 일괄 수정
    @Transactional
    public void batchUpdateUsers(BatchUpdateRequest request) {
        log.info("일괄 수정 요청: {}", request);
        
        List<User> users = userRepository.findAllById(request.getUserIds());
        
        for (User user : users) {
            if (request.getStatus() != null) {
                user.setStatus(User.UserStatus.valueOf(request.getStatus()));
            }
            if (request.getDepartment() != null) {
                user.setDepartment(request.getDepartment());
            }
            if (request.getRole() != null) {
                user.setRole(request.getRole());
            }
            user.setUpdatedBy("admin"); // TODO: 실제 인증된 사용자 정보 사용
        }
        
        userRepository.saveAll(users);
        
        // 활동 로그 기록
        for (User user : users) {
            userActivityLogService.logUserAction(user.getId(), "USER_BATCH_UPDATED", "일괄 수정");
        }
    }
    
    // 사용자 권한 설정
    private void setUserPermissions(User user, List<String> permissionNames) {
        List<UserPermission> permissions = permissionNames.stream()
            .map(name -> UserPermission.builder()
                .user(user)
                .permissionName(name)
                .permissionValue(true)
                .build())
            .collect(Collectors.toList());
        
        userPermissionRepository.saveAll(permissions);
    }
    
    // 사용자 권한 업데이트
    private void updateUserPermissions(User user, List<String> permissionNames) {
        // 기존 권한 삭제
        userPermissionRepository.deleteByUserId(user.getId());
        
        // 새 권한 설정
        setUserPermissions(user, permissionNames);
    }
}

