package com.kt_giga_fms.user.controller;

import com.kt_giga_fms.user.dto.*;
import com.kt_giga_fms.user.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
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
@Tag(name = "User Management", description = "사용자 관리 API")
public class UserController {
    
    private final UserService userService;
    
    // 사용자 검색 (대시보드 메인)
    @GetMapping("/search")
    @Operation(summary = "사용자 검색", description = "검색 조건에 따라 사용자를 검색합니다.")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "검색 성공", 
                    content = @Content(schema = @Schema(implementation = UserSearchResponse.class))),
        @ApiResponse(responseCode = "400", description = "잘못된 요청"),
        @ApiResponse(responseCode = "500", description = "서버 오류")
    })
    public ResponseEntity<UserSearchResponse> searchUsers(
            @Parameter(description = "사용자 검색 조건") UserSearchRequest request) {
        log.info("사용자 검색 API 호출: {}", request);
        UserSearchResponse response = userService.searchUsers(request);
        return ResponseEntity.ok(response);
    }
    
    // 사용자 통계 조회
    @GetMapping("/statistics")
    @Operation(summary = "사용자 통계 조회", description = "전체 사용자 통계 정보를 조회합니다.")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "통계 조회 성공", 
                    content = @Content(schema = @Schema(implementation = UserStatisticsDto.class))),
        @ApiResponse(responseCode = "500", description = "서버 오류")
    })
    public ResponseEntity<UserStatisticsDto> getUserStatistics() {
        log.info("사용자 통계 조회 API 호출");
        UserStatisticsDto statistics = userService.getUserStatistics();
        return ResponseEntity.ok(statistics);
    }
    
    // 사용자 상세 조회
    @GetMapping("/{id}")
    @Operation(summary = "사용자 상세 조회", description = "ID로 사용자 상세 정보를 조회합니다.")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "사용자 조회 성공", 
                    content = @Content(schema = @Schema(implementation = UserDto.class))),
        @ApiResponse(responseCode = "404", description = "사용자를 찾을 수 없음"),
        @ApiResponse(responseCode = "500", description = "서버 오류")
    })
    public ResponseEntity<UserDto> getUserById(
            @Parameter(description = "사용자 ID") @PathVariable Long id) {
        log.info("사용자 상세 조회 API 호출: ID={}", id);
        UserDto user = userService.getUserById(id);
        return ResponseEntity.ok(user);
    }
    
    // 사용자 생성
    @PostMapping
    @Operation(summary = "사용자 생성", description = "새로운 사용자를 생성합니다.")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "201", description = "사용자 생성 성공", 
                    content = @Content(schema = @Schema(implementation = UserDto.class))),
        @ApiResponse(responseCode = "400", description = "잘못된 요청 데이터"),
        @ApiResponse(responseCode = "409", description = "중복된 사용자"),
        @ApiResponse(responseCode = "500", description = "서버 오류")
    })
    public ResponseEntity<UserDto> createUser(
            @Parameter(description = "사용자 생성 정보") @Valid @RequestBody UserCreateRequest request) {
        log.info("사용자 생성 API 호출: {}", request);
        UserDto createdUser = userService.createUser(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdUser);
    }
    
    // 사용자 수정
    @PutMapping("/{id}")
    @Operation(summary = "사용자 수정", description = "기존 사용자 정보를 수정합니다.")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "사용자 수정 성공", 
                    content = @Content(schema = @Schema(implementation = UserDto.class))),
        @ApiResponse(responseCode = "400", description = "잘못된 요청 데이터"),
        @ApiResponse(responseCode = "404", description = "사용자를 찾을 수 없음"),
        @ApiResponse(responseCode = "500", description = "서버 오류")
    })
    public ResponseEntity<UserDto> updateUser(
            @Parameter(description = "사용자 ID") @PathVariable Long id, 
            @Parameter(description = "사용자 수정 정보") @Valid @RequestBody UserUpdateRequest request) {
        log.info("사용자 수정 API 호출: ID={}, 요청={}", id, request);
        UserDto updatedUser = userService.updateUser(id, request);
        return ResponseEntity.ok(updatedUser);
    }
    
    // 사용자 삭제
    @DeleteMapping("/{id}")
    @Operation(summary = "사용자 삭제", description = "사용자를 삭제합니다.")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "204", description = "사용자 삭제 성공"),
        @ApiResponse(responseCode = "404", description = "사용자를 찾을 수 없음"),
        @ApiResponse(responseCode = "500", description = "서버 오류")
    })
    public ResponseEntity<Void> deleteUser(
            @Parameter(description = "사용자 ID") @PathVariable Long id) {
        log.info("사용자 삭제 API 호출: ID={}", id);
        userService.deleteUser(id);
        return ResponseEntity.noContent().build();
    }
    
    // 일괄 수정
    @PutMapping("/batch")
    @Operation(summary = "사용자 일괄 수정", description = "여러 사용자 정보를 일괄적으로 수정합니다.")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "일괄 수정 성공"),
        @ApiResponse(responseCode = "400", description = "잘못된 요청 데이터"),
        @ApiResponse(responseCode = "500", description = "서버 오류")
    })
    public ResponseEntity<Void> batchUpdateUsers(
            @Parameter(description = "일괄 수정 정보") @Valid @RequestBody BatchUpdateRequest request) {
        log.info("사용자 일괄 수정 API 호출: {}", request);
        userService.batchUpdateUsers(request);
        return ResponseEntity.ok().build();
    }
    
    // 사용자 권한 관리
    @GetMapping("/{id}/permissions")
    @Operation(summary = "사용자 권한 조회", description = "사용자의 권한 정보를 조회합니다.")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "권한 조회 성공", 
                    content = @Content(schema = @Schema(implementation = UserDto.class))),
        @ApiResponse(responseCode = "404", description = "사용자를 찾을 수 없음"),
        @ApiResponse(responseCode = "500", description = "서버 오류")
    })
    public ResponseEntity<UserDto> getUserPermissions(
            @Parameter(description = "사용자 ID") @PathVariable Long id) {
        log.info("사용자 권한 조회 API 호출: ID={}", id);
        UserDto user = userService.getUserById(id);
        return ResponseEntity.ok(user);
    }
    
    // 헬스 체크
    @GetMapping("/health")
    @Operation(summary = "헬스 체크", description = "서비스 상태를 확인합니다.")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "서비스 정상 동작")
    })
    public ResponseEntity<String> health() {
        return ResponseEntity.ok("User Service is running");
    }
}

