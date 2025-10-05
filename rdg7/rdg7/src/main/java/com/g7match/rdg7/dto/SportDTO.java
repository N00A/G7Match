package com.g7match.rdg7.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class SportDTO {
    private Long id;
    private String name;
    private Boolean isActive;
}
