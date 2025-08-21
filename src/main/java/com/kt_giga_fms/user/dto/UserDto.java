package com.kt_giga_fms.user.dto;

import com.kt_giga_fms.user.entity.User;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Schema(description = "사용자 정보 DTO")
public class UserDto {
    
    @Schema(description = "사용자 고유 ID", example = "1")
    private Long id;
    
    @Schema(description = "사용자 ID (로그인용)", example = "user123")
    private String userId;
    
    @Schema(description = "사용자 이름", example = "홍길동")
    private String name;
    
    @Schema(description = "이메일 주소", example = "hong@example.com")
    private String email;
    
    @Schema(description = "전화번호", example = "010-1234-5678")
    private String phone;
    
    @Schema(description = "부서", example = "운영팀")
    private String department;
    
    @Schema(description = "사용자 역할", example = "DRIVER", allowableValues = {"ADMIN", "MANAGER", "DRIVER"})
    private String role;
    
    @Schema(description = "운전면허 번호", example = "12-345678-90")
    private String driverLicense;
    
    @Schema(description = "차량 유형", example = "SEDAN", allowableValues = {"SEDAN", "SUV", "TRUCK", "VAN"})
    private String vehicleType;
    
    @Schema(description = "사용자 상태", example = "ACTIVE", allowableValues = {"ACTIVE", "INACTIVE", "SUSPENDED"})
    private String status;
    
    @Schema(description = "생성 일시")
    private LocalDateTime createdAt;
    
    @Schema(description = "수정 일시")
    private LocalDateTime updatedAt;
    
    @Schema(description = "생성자", example = "admin")
    private String createdBy;
    
    @Schema(description = "수정자", example = "admin")
    private String updatedBy;
    
    @Schema(description = "사용자 권한 목록")
    private List<UserPermissionDto> permissions;
    
    public static UserDto fromEntity(User user) {
        return UserDto.builder()
                .id(user.getId())
                .userId(user.getUserId())
                .name(user.getName())
                .email(user.getEmail())
                .phone(user.getPhone())
                .department(user.getDepartment())
                .role(user.getRole())
                .driverLicense(user.getDriverLicense())
                .vehicleType(user.getVehicleType())
                .status(user.getStatus() != null ? user.getStatus().name() : null)
                .createdAt(user.getCreatedAt())
                .updatedAt(user.getUpdatedAt())
                .createdBy(user.getCreatedBy())
                .updatedBy(user.getUpdatedBy())
                .build();
    }
}

