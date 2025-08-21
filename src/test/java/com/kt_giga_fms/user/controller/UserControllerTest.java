package com.kt_giga_fms.user.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.kt_giga_fms.user.dto.*;
import com.kt_giga_fms.user.service.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.http.HttpStatus;

import java.util.Arrays;
import java.util.List;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(MockitoExtension.class)
@DisplayName("UserController 단위 테스트")
class UserControllerTest {

    @Mock
    private UserService userService;

    @InjectMocks
    private UserController userController;

    private MockMvc mockMvc;
    private ObjectMapper objectMapper;

    private UserDto testUserDto;
    private UserSearchResponse testSearchResponse;
    private UserStatisticsDto testStatisticsDto;
    private UserCreateRequest testCreateRequest;
    private UserUpdateRequest testUpdateRequest;
    private BatchUpdateRequest testBatchRequest;

    // 테스트용 전역 예외 핸들러
    @ControllerAdvice
    static class TestExceptionHandler {
        @ExceptionHandler(RuntimeException.class)
        @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
        public String handleRuntimeException(RuntimeException e) {
            return e.getMessage();
        }
    }

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(userController)
                .setControllerAdvice(new TestExceptionHandler())
                .build();
        objectMapper = new ObjectMapper();

        // 테스트용 DTO 데이터 설정
        testUserDto = UserDto.builder()
                .id(1L)
                .userId("testuser")
                .name("테스트 사용자")
                .email("test@example.com")
                .phone("010-1234-5678")
                .department("IT팀")
                .role("USER")
                .driverLicense("1종")
                .vehicleType("승용차")
                .status("ACTIVE")
                .build();

        testSearchResponse = UserSearchResponse.builder()
                .users(Arrays.asList(testUserDto))
                .totalElements(1L)
                .totalPages(1)
                .currentPage(0)
                .size(10)
                .statistics(UserStatisticsDto.builder()
                        .totalUsers(100L)
                        .activeUsers(80L)
                        .corporateVehicleUsers(30L)
                        .class1LicenseHolders(50L)
                        .build())
                .build();

        testStatisticsDto = UserStatisticsDto.builder()
                .totalUsers(100L)
                .activeUsers(80L)
                .corporateVehicleUsers(30L)
                .class1LicenseHolders(50L)
                .build();

        testCreateRequest = UserCreateRequest.builder()
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

        testUpdateRequest = UserUpdateRequest.builder()
                .name("수정된 사용자")
                .email("updated@example.com")
                .department("개발팀")
                .role("USER")
                .permissions(Arrays.asList("READ"))
                .build();

        testBatchRequest = BatchUpdateRequest.builder()
                .userIds(Arrays.asList(1L, 2L))
                .status("INACTIVE")
                .department("운영팀")
                .role("USER")
                .build();
    }

    @Test
    @DisplayName("사용자 검색 API - 성공")
    void searchUsers_Success() throws Exception {
        // given
        when(userService.searchUsers(any(UserSearchRequest.class))).thenReturn(testSearchResponse);

        // when & then
        mockMvc.perform(get("/api/v1/users/search")
                        .param("searchKeyword", "테스트")
                        .param("status", "ACTIVE")
                        .param("role", "USER")
                        .param("department", "IT팀")
                        .param("vehicleType", "승용차")
                        .param("driverLicense", "1종")
                        .param("page", "0")
                        .param("size", "10")
                        .param("sortBy", "name")
                        .param("sortDirection", "ASC"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.users").isArray())
                .andExpect(jsonPath("$.users[0].id").value(1))
                .andExpect(jsonPath("$.users[0].userId").value("testuser"))
                .andExpect(jsonPath("$.users[0].name").value("테스트 사용자"))
                .andExpect(jsonPath("$.totalElements").value(1))
                .andExpect(jsonPath("$.currentPage").value(0))
                .andExpect(jsonPath("$.statistics.totalUsers").value(100))
                .andExpect(jsonPath("$.statistics.activeUsers").value(80));

        verify(userService).searchUsers(any(UserSearchRequest.class));
    }

    @Test
    @DisplayName("사용자 통계 조회 API - 성공")
    void getUserStatistics_Success() throws Exception {
        // given
        when(userService.getUserStatistics()).thenReturn(testStatisticsDto);

        // when & then
        mockMvc.perform(get("/api/v1/users/statistics"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.totalUsers").value(100))
                .andExpect(jsonPath("$.activeUsers").value(80))
                .andExpect(jsonPath("$.corporateVehicleUsers").value(30))
                .andExpect(jsonPath("$.class1LicenseHolders").value(50));

        verify(userService).getUserStatistics();
    }

    @Test
    @DisplayName("사용자 상세 조회 API - 성공")
    void getUserById_Success() throws Exception {
        // given
        when(userService.getUserById(1L)).thenReturn(testUserDto);

        // when & then
        mockMvc.perform(get("/api/v1/users/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.userId").value("testuser"))
                .andExpect(jsonPath("$.name").value("테스트 사용자"))
                .andExpect(jsonPath("$.email").value("test@example.com"))
                .andExpect(jsonPath("$.department").value("IT팀"))
                .andExpect(jsonPath("$.role").value("USER"));

        verify(userService).getUserById(1L);
    }

    @Test
    @DisplayName("사용자 상세 조회 API - 사용자를 찾을 수 없는 경우")
    void getUserById_UserNotFound() throws Exception {
        // given
        when(userService.getUserById(999L)).thenThrow(new RuntimeException("사용자를 찾을 수 없습니다: 999"));

        // when & then
        mockMvc.perform(get("/api/v1/users/999"))
                .andExpect(status().isInternalServerError());

        verify(userService).getUserById(999L);
    }

    @Test
    @DisplayName("사용자 생성 API - 성공")
    void createUser_Success() throws Exception {
        // given
        when(userService.createUser(any(UserCreateRequest.class))).thenReturn(testUserDto);

        // when & then
        mockMvc.perform(post("/api/v1/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(testCreateRequest)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.userId").value("testuser"))
                .andExpect(jsonPath("$.name").value("테스트 사용자"));

        verify(userService).createUser(any(UserCreateRequest.class));
    }

    @Test
    @DisplayName("사용자 생성 API - 유효성 검사 실패")
    void createUser_ValidationFailure() throws Exception {
        // given
        UserCreateRequest invalidRequest = UserCreateRequest.builder()
                .userId("") // 빈 사용자 ID
                .name("") // 빈 이름
                .build();

        // when & then
        mockMvc.perform(post("/api/v1/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(invalidRequest)))
                .andExpect(status().isBadRequest());

        verify(userService, never()).createUser(any(UserCreateRequest.class));
    }

    @Test
    @DisplayName("사용자 생성 API - 중복된 사용자 ID")
    void createUser_DuplicateUserId() throws Exception {
        // given
        when(userService.createUser(any(UserCreateRequest.class)))
                .thenThrow(new RuntimeException("이미 존재하는 사용자 ID입니다: newuser"));

        // when & then
        mockMvc.perform(post("/api/v1/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(testCreateRequest)))
                .andExpect(status().isInternalServerError());

        verify(userService).createUser(any(UserCreateRequest.class));
    }

    @Test
    @DisplayName("사용자 수정 API - 성공")
    void updateUser_Success() throws Exception {
        // given
        when(userService.updateUser(eq(1L), any(UserUpdateRequest.class))).thenReturn(testUserDto);

        // when & then
        mockMvc.perform(put("/api/v1/users/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(testUpdateRequest)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.userId").value("testuser"))
                .andExpect(jsonPath("$.name").value("테스트 사용자"));

        verify(userService).updateUser(eq(1L), any(UserUpdateRequest.class));
    }

    @Test
    @DisplayName("사용자 수정 API - 사용자를 찾을 수 없는 경우")
    void updateUser_UserNotFound() throws Exception {
        // given
        when(userService.updateUser(eq(999L), any(UserUpdateRequest.class)))
                .thenThrow(new RuntimeException("사용자를 찾을 수 없습니다: 999"));

        // when & then
        mockMvc.perform(put("/api/v1/users/999")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(testUpdateRequest)))
                .andExpect(status().isInternalServerError());

        verify(userService).updateUser(eq(999L), any(UserUpdateRequest.class));
    }

    @Test
    @DisplayName("사용자 삭제 API - 성공")
    void deleteUser_Success() throws Exception {
        // given
        doNothing().when(userService).deleteUser(1L);

        // when & then
        mockMvc.perform(delete("/api/v1/users/1"))
                .andExpect(status().isNoContent());

        verify(userService).deleteUser(1L);
    }

    @Test
    @DisplayName("사용자 삭제 API - 사용자를 찾을 수 없는 경우")
    void deleteUser_UserNotFound() throws Exception {
        // given
        doThrow(new RuntimeException("사용자를 찾을 수 없습니다: 999")).when(userService).deleteUser(999L);

        // when & then
        mockMvc.perform(delete("/api/v1/users/999"))
                .andExpect(status().isInternalServerError());

        verify(userService).deleteUser(999L);
    }

    @Test
    @DisplayName("일괄 수정 API - 성공")
    void batchUpdateUsers_Success() throws Exception {
        // given
        doNothing().when(userService).batchUpdateUsers(any(BatchUpdateRequest.class));

        // when & then
        mockMvc.perform(put("/api/v1/users/batch")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(testBatchRequest)))
                .andExpect(status().isOk());

        verify(userService).batchUpdateUsers(any(BatchUpdateRequest.class));
    }

    @Test
    @DisplayName("일괄 수정 API - 유효성 검사 실패")
    void batchUpdateUsers_ValidationFailure() throws Exception {
        // given
        BatchUpdateRequest invalidRequest = BatchUpdateRequest.builder()
                .userIds(Arrays.asList()) // 빈 사용자 목록
                .build();

        // when & then
        mockMvc.perform(put("/api/v1/users/batch")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(invalidRequest)))
                .andExpect(status().isBadRequest());

        verify(userService, never()).batchUpdateUsers(any(BatchUpdateRequest.class));
    }

    @Test
    @DisplayName("사용자 권한 조회 API - 성공")
    void getUserPermissions_Success() throws Exception {
        // given
        when(userService.getUserById(1L)).thenReturn(testUserDto);

        // when & then
        mockMvc.perform(get("/api/v1/users/1/permissions"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.userId").value("testuser"))
                .andExpect(jsonPath("$.name").value("테스트 사용자"));

        verify(userService).getUserById(1L);
    }

    @Test
    @DisplayName("헬스 체크 API - 성공")
    void health_Success() throws Exception {
        // when & then
        mockMvc.perform(get("/api/v1/users/health"))
                .andExpect(status().isOk())
                .andExpect(content().string("User Service is running"));
    }
}
