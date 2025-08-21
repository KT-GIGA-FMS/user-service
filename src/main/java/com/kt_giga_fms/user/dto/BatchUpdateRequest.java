package com.kt_giga_fms.user.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Schema(description = "사용자 일괄 수정 요청 DTO")
public class BatchUpdateRequest {
    
    @Schema(description = "수정할 사용자 ID 목록", example = "[1, 2, 3]")
    private List<Long> userIds;
    
    @Schema(description = "변경할 사용자 상태", example = "ACTIVE", 
            allowableValues = {"ACTIVE", "INACTIVE", "SUSPENDED"})
    private String status;
    
    @Schema(description = "변경할 부서", example = "운영팀")
    private String department;
    
    @Schema(description = "변경할 사용자 역할", example = "DRIVER", 
            allowableValues = {"ADMIN", "MANAGER", "DRIVER"})
    private String role;
    
    @Schema(description = "변경할 사용자 권한 목록")
    private List<String> permissions;
}

