package com.g7match.rdg7.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ReservationDTO {
    private Integer id;
    private CourtDTO courtDTO;
    private UsersDTO userDTO;
    private String startAt;
    private String endAt;
    private String statusCode;
    private String notes;
}
