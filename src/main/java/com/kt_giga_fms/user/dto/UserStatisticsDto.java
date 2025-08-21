package com.kt_giga_fms.user.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Schema(description = "사용자 통계 정보 DTO")
public class UserStatisticsDto {
    
    @Schema(description = "전체 사용자 수", example = "150")
    private long totalUsers;
    
    @Schema(description = "활성 사용자 수", example = "120")
    private long activeUsers;
    
    @Schema(description = "법인 차량 사용자 수", example = "80")
    private long corporateVehicleUsers;
    
    @Schema(description = "1종 운전면허 소지자 수", example = "95")
    private long class1LicenseHolders;
}

