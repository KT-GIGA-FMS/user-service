package com.kt_giga_fms.user.dto;

import jakarta.validation.constraints.NotEmpty;
import lombok.*;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class BatchUpdateRequest {
    
    @NotEmpty(message = "사용자 ID 목록은 필수입니다")
    private List<Long> userIds;
    private String status;
    private String department;
    private String role;
    private List<String> permissions;
}

