package com.kt_giga_fms.user.dto;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserSearchRequest {
    
    private String searchKeyword; // 이름, 사용자ID 또는 부서로 검색
    private String status; // 전체 상태
    private String role;
    private String department;
    private String vehicleType;
    private String driverLicense;
    private Integer page = 0;
    private Integer size = 20;
    private String sortBy = "createdAt";
    private String sortDirection = "DESC";
}

