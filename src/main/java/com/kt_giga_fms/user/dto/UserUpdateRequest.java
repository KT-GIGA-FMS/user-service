package com.kt_giga_fms.user.dto;

import jakarta.validation.constraints.Email;
import lombok.*;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserUpdateRequest {
    
    private String name;
    
    @Email(message = "올바른 이메일 형식이 아닙니다")
    private String email;
    
    private String phone;
    private String department;
    private String role;
    private String driverLicense;
    private String vehicleType;
    private String status;
    private List<String> permissions;
}

