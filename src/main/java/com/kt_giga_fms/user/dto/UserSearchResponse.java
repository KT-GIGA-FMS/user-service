package com.kt_giga_fms.user.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Schema(description = "사용자 검색 응답 DTO")
public class UserSearchResponse {
    
    @Schema(description = "사용자 목록")
    private List<UserDto> users;
    
    @Schema(description = "전체 사용자 수", example = "150")
    private long totalElements;
    
    @Schema(description = "전체 페이지 수", example = "8")
    private int totalPages;
    
    @Schema(description = "현재 페이지 번호", example = "0")
    private int currentPage;
    
    @Schema(description = "페이지 크기", example = "20")
    private int size;
    
    @Schema(description = "사용자 통계 정보")
    private UserStatisticsDto statistics;
}

