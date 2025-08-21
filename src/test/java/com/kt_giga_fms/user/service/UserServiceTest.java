package com.kt_giga_fms.user.service;

import com.kt_giga_fms.user.dto.*;
import com.kt_giga_fms.user.entity.User;
import com.kt_giga_fms.user.entity.UserPermission;
import com.kt_giga_fms.user.repository.UserRepository;
import com.kt_giga_fms.user.repository.UserPermissionRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@DisplayName("UserService 단위 테스트")
class UserServiceTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private UserPermissionRepository userPermissionRepository;

    @Mock
    private UserActivityLogService userActivityLogService;

    @InjectMocks
    private UserService userService;

    private User testUser;
    private UserCreateRequest createRequest;
    private UserUpdateRequest updateRequest;
    private UserSearchRequest searchRequest;

    @BeforeEach
    void setUp() {
        // 테스트용 사용자 데이터 설정
        testUser = User.builder()
                .id(1L)
                .userId("testuser")
                .name("테스트 사용자")
                .email("test@example.com")
                .phone("010-1234-5678")
                .department("IT팀")
                .role("USER")
                .driverLicense("1종")
                .vehicleType("승용차")
                .status(User.UserStatus.ACTIVE)
                .createdBy("admin")
                .build();

        // 사용자 생성 요청 데이터
        createRequest = UserCreateRequest.builder()
                .userId("newuser")
                .name("새 사용자")
                .email("new@example.com")
                .phone("010-9876-5432")
                .department("운영팀")
                .role("ADMIN")
                .driverLicense("2종")
                .vehicleType("화물차")
                .status("ACTIVE")
                .permissions(Arrays.asList("READ", "WRITE"))
                .build();

        // 사용자 수정 요청 데이터
        updateRequest = UserUpdateRequest.builder()
                .name("수정된 사용자")
                .email("updated@example.com")
                .department("개발팀")
                .role("USER")
                .permissions(Arrays.asList("READ"))
                .build();

        // 사용자 검색 요청 데이터
        searchRequest = UserSearchRequest.builder()
                .searchKeyword("테스트")
                .status("ACTIVE")
                .role("USER")
                .department("IT팀")
                .vehicleType("승용차")
                .driverLicense("1종")
                .page(0)
                .size(10)
                .sortBy("name")
                .sortDirection("ASC")
                .build();
    }

    @Test
    @DisplayName("사용자 검색 - 성공")
    void searchUsers_Success() {
        // given
        List<User> users = Arrays.asList(testUser);
        Page<User> userPage = new PageImpl<>(users, PageRequest.of(0, 10), 1);
        
        when(userRepository.findBySearchCriteria(
                anyString(), anyString(), anyString(), anyString(), 
                anyString(), anyString(), any(Pageable.class)))
                .thenReturn(userPage);
        when(userRepository.count()).thenReturn(1L);
        when(userRepository.countActiveUsers()).thenReturn(1L);
        when(userRepository.countCorporateVehicleUsers()).thenReturn(0L);
        when(userRepository.countClass1LicenseHolders()).thenReturn(1L);

        // when
        UserSearchResponse response = userService.searchUsers(searchRequest);

        // then
        assertThat(response).isNotNull();
        assertThat(response.getUsers()).hasSize(1);
        assertThat(response.getTotalElements()).isEqualTo(1);
        assertThat(response.getCurrentPage()).isEqualTo(0);
        assertThat(response.getStatistics()).isNotNull();
        assertThat(response.getStatistics().getTotalUsers()).isEqualTo(1);
        assertThat(response.getStatistics().getActiveUsers()).isEqualTo(1);
    }

    @Test
    @DisplayName("사용자 통계 조회 - 성공")
    void getUserStatistics_Success() {
        // given
        when(userRepository.count()).thenReturn(100L);
        when(userRepository.countActiveUsers()).thenReturn(80L);
        when(userRepository.countCorporateVehicleUsers()).thenReturn(30L);
        when(userRepository.countClass1LicenseHolders()).thenReturn(50L);

        // when
        UserStatisticsDto statistics = userService.getUserStatistics();

        // then
        assertThat(statistics).isNotNull();
        assertThat(statistics.getTotalUsers()).isEqualTo(100);
        assertThat(statistics.getActiveUsers()).isEqualTo(80);
        assertThat(statistics.getCorporateVehicleUsers()).isEqualTo(30);
        assertThat(statistics.getClass1LicenseHolders()).isEqualTo(50);
    }

    @Test
    @DisplayName("사용자 상세 조회 - 성공")
    void getUserById_Success() {
        // given
        when(userRepository.findById(1L)).thenReturn(Optional.of(testUser));

        // when
        UserDto result = userService.getUserById(1L);

        // then
        assertThat(result).isNotNull();
        assertThat(result.getId()).isEqualTo(1L);
        assertThat(result.getUserId()).isEqualTo("testuser");
        assertThat(result.getName()).isEqualTo("테스트 사용자");
    }

    @Test
    @DisplayName("사용자 상세 조회 - 사용자를 찾을 수 없는 경우")
    void getUserById_UserNotFound() {
        // given
        when(userRepository.findById(999L)).thenReturn(Optional.empty());

        // when & then
        assertThatThrownBy(() -> userService.getUserById(999L))
                .isInstanceOf(RuntimeException.class)
                .hasMessageContaining("사용자를 찾을 수 없습니다");
    }

    @Test
    @DisplayName("사용자 생성 - 성공")
    void createUser_Success() {
        // given
        when(userRepository.existsByUserId("newuser")).thenReturn(false);
        when(userRepository.existsByEmail("new@example.com")).thenReturn(false);
        when(userRepository.save(any(User.class))).thenReturn(testUser);
        when(userPermissionRepository.saveAll(anyList())).thenReturn(Arrays.asList(new UserPermission()));

        // when
        UserDto result = userService.createUser(createRequest);

        // then
        assertThat(result).isNotNull();
        verify(userRepository).existsByUserId("newuser");
        verify(userRepository).existsByEmail("new@example.com");
        verify(userRepository).save(any(User.class));
        verify(userPermissionRepository).saveAll(anyList());
        verify(userActivityLogService).logUserAction(anyLong(), eq("USER_CREATED"), anyString());
    }

    @Test
    @DisplayName("사용자 생성 - 중복된 사용자 ID")
    void createUser_DuplicateUserId() {
        // given
        when(userRepository.existsByUserId("newuser")).thenReturn(true);

        // when & then
        assertThatThrownBy(() -> userService.createUser(createRequest))
                .isInstanceOf(RuntimeException.class)
                .hasMessageContaining("이미 존재하는 사용자 ID입니다");
        
        verify(userRepository, never()).save(any(User.class));
    }

    @Test
    @DisplayName("사용자 생성 - 중복된 이메일")
    void createUser_DuplicateEmail() {
        // given
        when(userRepository.existsByUserId("newuser")).thenReturn(false);
        when(userRepository.existsByEmail("new@example.com")).thenReturn(true);

        // when & then
        assertThatThrownBy(() -> userService.createUser(createRequest))
                .isInstanceOf(RuntimeException.class)
                .hasMessageContaining("이미 존재하는 이메일입니다");
        
        verify(userRepository, never()).save(any(User.class));
    }

    @Test
    @DisplayName("사용자 수정 - 성공")
    void updateUser_Success() {
        // given
        when(userRepository.findById(1L)).thenReturn(Optional.of(testUser));
        when(userRepository.save(any(User.class))).thenReturn(testUser);
        when(userPermissionRepository.saveAll(anyList())).thenReturn(Arrays.asList(new UserPermission()));

        // when
        UserDto result = userService.updateUser(1L, updateRequest);

        // then
        assertThat(result).isNotNull();
        verify(userRepository).findById(1L);
        verify(userRepository).save(any(User.class));
        verify(userPermissionRepository).deleteByUserId(1L);
        verify(userPermissionRepository).saveAll(anyList());
        verify(userActivityLogService).logUserAction(1L, "USER_UPDATED", "사용자 정보 수정");
    }

    @Test
    @DisplayName("사용자 수정 - 사용자를 찾을 수 없는 경우")
    void updateUser_UserNotFound() {
        // given
        when(userRepository.findById(999L)).thenReturn(Optional.empty());

        // when & then
        assertThatThrownBy(() -> userService.updateUser(999L, updateRequest))
                .isInstanceOf(RuntimeException.class)
                .hasMessageContaining("사용자를 찾을 수 없습니다");
        
        verify(userRepository, never()).save(any(User.class));
    }

    @Test
    @DisplayName("사용자 삭제 - 성공")
    void deleteUser_Success() {
        // given
        when(userRepository.findById(1L)).thenReturn(Optional.of(testUser));
        doNothing().when(userRepository).delete(testUser);

        // when
        userService.deleteUser(1L);

        // then
        verify(userRepository).findById(1L);
        verify(userRepository).delete(testUser);
        verify(userActivityLogService).logUserAction(1L, "USER_DELETED", "사용자 삭제");
    }

    @Test
    @DisplayName("사용자 삭제 - 사용자를 찾을 수 없는 경우")
    void deleteUser_UserNotFound() {
        // given
        when(userRepository.findById(999L)).thenReturn(Optional.empty());

        // when & then
        assertThatThrownBy(() -> userService.deleteUser(999L))
                .isInstanceOf(RuntimeException.class)
                .hasMessageContaining("사용자를 찾을 수 없습니다");
        
        verify(userRepository, never()).delete(any(User.class));
    }

    @Test
    @DisplayName("일괄 수정 - 성공")
    void batchUpdateUsers_Success() {
        // given
        List<User> users = Arrays.asList(testUser);
        BatchUpdateRequest batchRequest = BatchUpdateRequest.builder()
                .userIds(Arrays.asList(1L))
                .status("INACTIVE")
                .department("운영팀")
                .role("USER")
                .build();
        
        when(userRepository.findAllById(Arrays.asList(1L))).thenReturn(users);
        when(userRepository.saveAll(users)).thenReturn(users);

        // when
        userService.batchUpdateUsers(batchRequest);

        // then
        verify(userRepository).findAllById(Arrays.asList(1L));
        verify(userRepository).saveAll(users);
        verify(userActivityLogService).logUserAction(1L, "USER_BATCH_UPDATED", "일괄 수정");
    }

    @Test
    @DisplayName("일괄 수정 - 빈 사용자 목록")
    void batchUpdateUsers_EmptyUserList() {
        // given
        BatchUpdateRequest batchRequest = BatchUpdateRequest.builder()
                .userIds(Arrays.asList(999L))
                .status("INACTIVE")
                .build();
        
        when(userRepository.findAllById(Arrays.asList(999L))).thenReturn(Arrays.asList());

        // when
        userService.batchUpdateUsers(batchRequest);

        // then
        verify(userRepository).findAllById(Arrays.asList(999L));
        verify(userRepository).saveAll(Arrays.asList());
        verify(userActivityLogService, never()).logUserAction(anyLong(), anyString(), anyString());
    }
}
