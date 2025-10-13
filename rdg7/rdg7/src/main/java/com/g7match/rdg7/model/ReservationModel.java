package com.g7match.rdg7.model;

import jakarta.persistence.*;
import lombok.*;

import java.util.Date;

@Entity
@Table(name = "Reservation")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ReservationModel {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "Court_id")
    private CourtModel court;
    @ManyToOne
    @JoinColumn(name = "User_id")
    private UserModel user;
    private Date startAt;
    private Date endAt;
    private String statusCode;
    private String notes;
}

