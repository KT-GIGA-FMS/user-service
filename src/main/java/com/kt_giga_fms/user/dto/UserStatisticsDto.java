package com.kt_giga_fms.user.dto;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserStatisticsDto {
    
    private long totalUsers;
    private long activeUsers;
    private long corporateVehicleUsers;
    private long class1LicenseHolders;
}

