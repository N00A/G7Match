package com.g7match.rdg7.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class CourtDTO {
    private Long id;
    private String name;
    private String location;
    private SportDTO sportDTO;
    private Float pricePerHour;
    private Boolean isActive;
}
