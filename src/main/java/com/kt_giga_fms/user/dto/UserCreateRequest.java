package com.kt_giga_fms.user.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserCreateRequest {
    
    @NotBlank(message = "사용자 ID는 필수입니다")
    private String userId;
    
    @NotBlank(message = "이름은 필수입니다")
    private String name;
    
    @Email(message = "올바른 이메일 형식이 아닙니다")
    private String email;
    
    private String phone;
    private String department;
    
    @NotBlank(message = "역할은 필수입니다")
    private String role;
    
    private String driverLicense;
    private String vehicleType;
    
    @NotNull(message = "상태는 필수입니다")
    private String status;
    
    private List<String> permissions;
}

