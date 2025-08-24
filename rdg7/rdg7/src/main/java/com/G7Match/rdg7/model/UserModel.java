package com.G7Match.rdg7.model;


import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name="users")
public class UserModel {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private long id;
    private String identification;
    private String passwordHash;
    private String email;
    private String firstName;
    private String secondName;
    private String lastName;
    private String secondLastName;
    private String phone;
    private Boolean isActive;



}
