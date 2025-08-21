package com.kt_giga_fms.user.dto;

import lombok.*;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class BatchUpdateRequest {
    
    private List<Long> userIds;
    private String status;
    private String department;
    private String role;
    private List<String> permissions;
}

