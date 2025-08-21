package com.kt_giga_fms.user.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Schema(description = "사용자 생성 요청 DTO")
public class UserCreateRequest {
    
    @NotBlank(message = "사용자 ID는 필수입니다")
    @Schema(description = "사용자 ID (로그인용)", example = "user123", required = true)
    private String userId;
    
    @NotBlank(message = "이름은 필수입니다")
    @Schema(description = "사용자 이름", example = "홍길동", required = true)
    private String name;
    
    @Email(message = "올바른 이메일 형식이 아닙니다")
    @Schema(description = "이메일 주소", example = "hong@example.com")
    private String email;
    
    @Schema(description = "전화번호", example = "010-1234-5678")
    private String phone;
    
    @Schema(description = "부서", example = "운영팀")
    private String department;
    
    @NotBlank(message = "역할은 필수입니다")
    @Schema(description = "사용자 역할", example = "DRIVER", required = true, 
            allowableValues = {"ADMIN", "MANAGER", "DRIVER"})
    private String role;
    
    @Schema(description = "운전면허 번호", example = "12-345678-90")
    private String driverLicense;
    
    @Schema(description = "차량 유형", example = "SEDAN", 
            allowableValues = {"SEDAN", "SUV", "TRUCK", "VAN"})
    private String vehicleType;
    
    @NotNull(message = "상태는 필수입니다")
    @Schema(description = "사용자 상태", example = "ACTIVE", required = true,
            allowableValues = {"ACTIVE", "INACTIVE", "SUSPENDED"})
    private String status;
    
    @Schema(description = "사용자 권한 목록")
    private List<String> permissions;
}

