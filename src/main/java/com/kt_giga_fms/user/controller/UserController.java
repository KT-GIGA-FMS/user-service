package com.kt_giga_fms.user.controller;

import com.kt_giga_fms.user.dto.*;
import com.kt_giga_fms.user.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/v1/users")
@RequiredArgsConstructor
@Slf4j
public class UserController {
    
    private final UserService userService;
    
    // 사용자 검색 (대시보드 메인)
    @GetMapping("/search")
    public ResponseEntity<UserSearchResponse> searchUsers(UserSearchRequest request) {
        log.info("사용자 검색 API 호출: {}", request);
        UserSearchResponse response = userService.searchUsers(request);
        return ResponseEntity.ok(response);
    }
    
    // 사용자 통계 조회
    @GetMapping("/statistics")
    public ResponseEntity<UserStatisticsDto> getUserStatistics() {
        log.info("사용자 통계 조회 API 호출");
        UserStatisticsDto statistics = userService.getUserStatistics();
        return ResponseEntity.ok(statistics);
    }
    
    // 사용자 상세 조회
    @GetMapping("/{id}")
    public ResponseEntity<UserDto> getUserById(@PathVariable Long id) {
        log.info("사용자 상세 조회 API 호출: ID={}", id);
        UserDto user = userService.getUserById(id);
        return ResponseEntity.ok(user);
    }
    
    // 사용자 생성
    @PostMapping
    public ResponseEntity<UserDto> createUser(@Valid @RequestBody UserCreateRequest request) {
        log.info("사용자 생성 API 호출: {}", request);
        UserDto createdUser = userService.createUser(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdUser);
    }
    
    // 사용자 수정
    @PutMapping("/{id}")
    public ResponseEntity<UserDto> updateUser(@PathVariable Long id, 
                                           @Valid @RequestBody UserUpdateRequest request) {
        log.info("사용자 수정 API 호출: ID={}, 요청={}", id, request);
        UserDto updatedUser = userService.updateUser(id, request);
        return ResponseEntity.ok(updatedUser);
    }
    
    // 사용자 삭제
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteUser(@PathVariable Long id) {
        log.info("사용자 삭제 API 호출: ID={}", id);
        userService.deleteUser(id);
        return ResponseEntity.noContent().build();
    }
    
    // 일괄 수정
    @PutMapping("/batch")
    public ResponseEntity<Void> batchUpdateUsers(@Valid @RequestBody BatchUpdateRequest request) {
        log.info("사용자 일괄 수정 API 호출: {}", request);
        userService.batchUpdateUsers(request);
        return ResponseEntity.ok().build();
    }
    
    // 사용자 권한 관리
    @GetMapping("/{id}/permissions")
    public ResponseEntity<UserDto> getUserPermissions(@PathVariable Long id) {
        log.info("사용자 권한 조회 API 호출: ID={}", id);
        UserDto user = userService.getUserById(id);
        return ResponseEntity.ok(user);
    }
    
    // 헬스 체크
    @GetMapping("/health")
    public ResponseEntity<String> health() {
        return ResponseEntity.ok("User Service is running");
    }
}

