package com.g7match.rdg7.dto;

import lombok.Builder;
import lombok.Data;
import java.util.Date;

@Data
@Builder
public class ReservationDTO {
    private Long id;
    private Long courtId;
    private Long userId;
    private CourtDTO courtDTO;
    private UsersDTO userDTO;
    private Date startAt;
    private Date endAt;
    private String statusCode;
    private String notes;
}
