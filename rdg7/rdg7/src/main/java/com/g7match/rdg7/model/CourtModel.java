package com.g7match.rdg7.model;

import jakarta.persistence.*;
import lombok.*;

import java.util.List;

@Entity
@Table(name = "Court")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CourtModel {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    private String name;
    private String location;
    @ManyToOne
    @JoinColumn(name = "Sport_id")
    private SportModel sport;
    private Float pricePerHour;
    private Boolean isActive;

    @OneToMany(mappedBy = "court", cascade = CascadeType.ALL)
    private List<ReservationModel> reservations;
}

