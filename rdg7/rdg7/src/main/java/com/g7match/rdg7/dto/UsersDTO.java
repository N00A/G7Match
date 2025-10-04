package com.g7match.rdg7.dto;

import lombok.Builder;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
@Data
public class UsersDTO {
    private String identification;
    private String password;
    private String email;
    private String firstName;
    private String secondName;
    private String lastName;
    private String secondLastName;
    private String phone;
    private Boolean isActive;
}