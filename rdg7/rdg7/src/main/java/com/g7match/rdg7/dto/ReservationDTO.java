package com.g7match.rdg7.dto;

import lombok.Builder;
import lombok.Data;
import java.time.LocalDateTime;

@Data
@Builder
public class ReservationDTO {
    private Long id;

    private Long courtId;
    private Long userId;

    private CourtDTO courtDTO;
    private UsersDTO userDTO;
    private LocalDateTime startAt;
    private LocalDateTime endAt;
    private String statusCode;
    private String notes;
}
