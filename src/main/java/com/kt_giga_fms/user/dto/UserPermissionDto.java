package com.kt_giga_fms.user.dto;

import com.kt_giga_fms.user.entity.UserPermission;
import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserPermissionDto {
    
    private Long id;
    private String permissionName;
    private Boolean permissionValue;
    private LocalDateTime createdAt;
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

