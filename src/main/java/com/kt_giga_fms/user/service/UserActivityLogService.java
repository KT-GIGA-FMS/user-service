package com.kt_giga_fms.user.service;

import com.kt_giga_fms.user.entity.UserActivityLog;
import com.kt_giga_fms.user.repository.UserActivityLogRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import jakarta.servlet.http.HttpServletRequest;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional
public class UserActivityLogService {
    
    private final UserActivityLogRepository userActivityLogRepository;
    private final HttpServletRequest request;
    
    public void logUserAction(Long userId, String action, String details) {
        try {
            UserActivityLog activityLog = UserActivityLog.builder()
                .user(null) // userId로 참조
                .action(action)
                .details(details)
                .ipAddress(getClientIpAddress())
                .userAgent(getUserAgent())
                .build();
            
            userActivityLogRepository.save(activityLog);
            log.info("사용자 활동 로그 기록: userId={}, action={}", userId, action);
        } catch (Exception e) {
            log.error("사용자 활동 로그 기록 실패: userId={}, action={}", userId, action, e);
        }
    }
    
    private String getClientIpAddress() {
        String xForwardedFor = request.getHeader("X-Forwarded-For");
        if (xForwardedFor != null && !xForwardedFor.isEmpty()) {
            return xForwardedFor.split(",")[0].trim();
        }
        return request.getRemoteAddr();
    }
    
    private String getUserAgent() {
        return request.getHeader("User-Agent");
    }
}
