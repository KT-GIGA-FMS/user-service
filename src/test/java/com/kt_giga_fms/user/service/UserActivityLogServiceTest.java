package com.kt_giga_fms.user.service;

import com.kt_giga_fms.user.entity.UserActivityLog;
import com.kt_giga_fms.user.repository.UserActivityLogRepository;
import jakarta.servlet.http.HttpServletRequest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@DisplayName("UserActivityLogService 단위 테스트")
class UserActivityLogServiceTest {

    @Mock
    private UserActivityLogRepository userActivityLogRepository;

    @Mock
    private HttpServletRequest request;

    @InjectMocks
    private UserActivityLogService userActivityLogService;

    @BeforeEach
    void setUp() {
        // 기본 HTTP 요청 정보 설정
        lenient().when(request.getRemoteAddr()).thenReturn("192.168.1.100");
        lenient().when(request.getHeader("User-Agent")).thenReturn("Mozilla/5.0 (Test Browser)");
        lenient().when(request.getHeader("X-Forwarded-For")).thenReturn(null);
    }

    @Test
    @DisplayName("사용자 활동 로그 기록 - 성공")
    void logUserAction_Success() {
        // given
        Long userId = 1L;
        String action = "USER_LOGIN";
        String details = "사용자 로그인";
        
        when(userActivityLogRepository.save(any(UserActivityLog.class)))
                .thenReturn(UserActivityLog.builder().id(1L).build());

        // when
        userActivityLogService.logUserAction(userId, action, details);

        // then
        verify(userActivityLogRepository).save(any(UserActivityLog.class));
    }

    @Test
    @DisplayName("사용자 활동 로그 기록 - X-Forwarded-For 헤더 사용")
    void logUserAction_WithXForwardedFor() {
        // given
        Long userId = 1L;
        String action = "USER_LOGOUT";
        String details = "사용자 로그아웃";
        
        when(request.getHeader("X-Forwarded-For")).thenReturn("203.0.113.1, 192.168.1.100");
        when(userActivityLogRepository.save(any(UserActivityLog.class)))
                .thenReturn(UserActivityLog.builder().id(1L).build());

        // when
        userActivityLogService.logUserAction(userId, action, details);

        // then
        verify(userActivityLogRepository).save(any(UserActivityLog.class));
        verify(request).getHeader("X-Forwarded-For");
    }

    @Test
    @DisplayName("사용자 활동 로그 기록 - 예외 발생 시 처리")
    void logUserAction_ExceptionHandling() {
        // given
        Long userId = 1L;
        String action = "USER_UPDATE";
        String details = "사용자 정보 수정";
        
        when(userActivityLogRepository.save(any(UserActivityLog.class)))
                .thenThrow(new RuntimeException("데이터베이스 오류"));

        // when & then (예외가 발생해도 메서드가 정상 종료되어야 함)
        userActivityLogService.logUserAction(userId, action, details);

        // then
        verify(userActivityLogRepository).save(any(UserActivityLog.class));
    }

    @Test
    @DisplayName("사용자 활동 로그 기록 - 다양한 액션 타입")
    void logUserAction_VariousActionTypes() {
        // given
        Long userId = 1L;
        when(userActivityLogRepository.save(any(UserActivityLog.class)))
                .thenReturn(UserActivityLog.builder().id(1L).build());

        // when & then
        userActivityLogService.logUserAction(userId, "USER_CREATED", "새 사용자 생성");
        userActivityLogService.logUserAction(userId, "USER_UPDATED", "사용자 정보 수정");
        userActivityLogService.logUserAction(userId, "USER_DELETED", "사용자 삭제");
        userActivityLogService.logUserAction(userId, "USER_BATCH_UPDATED", "일괄 수정");

        // then
        verify(userActivityLogRepository, times(4)).save(any(UserActivityLog.class));
    }

    @Test
    @DisplayName("사용자 활동 로그 기록 - 빈 X-Forwarded-For 헤더")
    void logUserAction_EmptyXForwardedFor() {
        // given
        Long userId = 1L;
        String action = "USER_LOGIN";
        String details = "사용자 로그인";
        
        when(request.getHeader("X-Forwarded-For")).thenReturn("");
        when(userActivityLogRepository.save(any(UserActivityLog.class)))
                .thenReturn(UserActivityLog.builder().id(1L).build());

        // when
        userActivityLogService.logUserAction(userId, action, details);

        // then
        verify(userActivityLogRepository).save(any(UserActivityLog.class));
    }

    @Test
    @DisplayName("사용자 활동 로그 기록 - null User-Agent")
    void logUserAction_NullUserAgent() {
        // given
        Long userId = 1L;
        String action = "USER_LOGIN";
        String details = "사용자 로그인";
        
        when(request.getHeader("User-Agent")).thenReturn(null);
        when(userActivityLogRepository.save(any(UserActivityLog.class)))
                .thenReturn(UserActivityLog.builder().id(1L).build());

        // when
        userActivityLogService.logUserAction(userId, action, details);

        // then
        verify(userActivityLogRepository).save(any(UserActivityLog.class));
    }
}
