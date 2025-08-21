package com.kt_giga_fms.user.dto;

import com.kt_giga_fms.user.entity.UserPermission;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Schema(description = "사용자 권한 정보 DTO")
public class UserPermissionDto {
    
    @Schema(description = "권한 고유 ID", example = "1")
    private Long id;
    
    @Schema(description = "권한 이름", example = "CAR_READ", 
            allowableValues = {"CAR_READ", "CAR_WRITE", "USER_READ", "USER_WRITE", "TRACKING_READ"})
    private String permissionName;
    
    @Schema(description = "권한 값 (허용 여부)", example = "true")
    private Boolean permissionValue;
    
    @Schema(description = "생성 일시")
    private LocalDateTime createdAt;
    
    @Schema(description = "수정 일시")
    private LocalDateTime updatedAt;
    
    public static UserPermissionDto fromEntity(UserPermission permission) {
        return UserPermissionDto.builder()
                .id(permission.getId())
                .permissionName(permission.getPermissionName())
                .permissionValue(permission.getPermissionValue())
                .createdAt(permission.getCreatedAt())
                .updatedAt(permission.getUpdatedAt())
                .build();
    }
}

