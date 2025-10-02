package com.g7match.rdg7.model;

import jakarta.persistence.*;
import lombok.*;

import java.util.List;

@Entity
@Table(name = "Sport")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class SportModel {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    private String name;
    @OneToMany(mappedBy = "sport", cascade = CascadeType.ALL)
    private List<CourtModel> courts;
}

