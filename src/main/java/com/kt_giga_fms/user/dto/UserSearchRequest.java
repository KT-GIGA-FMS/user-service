package com.kt_giga_fms.user.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Schema(description = "사용자 검색 요청 DTO")
public class UserSearchRequest {
    
    @Schema(description = "검색 키워드 (이름, 사용자ID 또는 부서로 검색)", example = "홍길동")
    private String searchKeyword;
    
    @Schema(description = "사용자 상태", example = "ACTIVE", 
            allowableValues = {"ACTIVE", "INACTIVE", "SUSPENDED"})
    private String status;
    
    @Schema(description = "사용자 역할", example = "DRIVER", 
            allowableValues = {"ADMIN", "MANAGER", "DRIVER"})
    private String role;
    
    @Schema(description = "부서", example = "운영팀")
    private String department;
    
    @Schema(description = "차량 유형", example = "SEDAN", 
            allowableValues = {"SEDAN", "SUV", "TRUCK", "VAN"})
    private String vehicleType;
    
    @Schema(description = "운전면허 번호", example = "12-345678-90")
    private String driverLicense;
    
    @Schema(description = "페이지 번호 (0부터 시작)", example = "0", defaultValue = "0")
    private Integer page = 0;
    
    @Schema(description = "페이지 크기", example = "20", defaultValue = "20")
    private Integer size = 20;
    
    @Schema(description = "정렬 기준 필드", example = "createdAt", defaultValue = "createdAt")
    private String sortBy = "createdAt";
    
    @Schema(description = "정렬 방향", example = "DESC", defaultValue = "DESC", 
            allowableValues = {"ASC", "DESC"})
    private String sortDirection = "DESC";
}

