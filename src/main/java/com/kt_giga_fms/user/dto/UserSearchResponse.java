package com.kt_giga_fms.user.dto;

import lombok.*;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserSearchResponse {
    
    private List<UserDto> users;
    private long totalElements;
    private int totalPages;
    private int currentPage;
    private int size;
    private UserStatisticsDto statistics;
}

