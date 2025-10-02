package com.g7match.rdg7.model;

import jakarta.persistence.*;
import lombok.*;
import com.g7match.rdg7.model.UserModel;

import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "Roles")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class RoleModel {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    private String name;

    @OneToMany(mappedBy = "roleModel", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<UserRoleModel> userRoles = new HashSet<>();

}
