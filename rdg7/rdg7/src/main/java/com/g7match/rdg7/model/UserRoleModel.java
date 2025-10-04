package com.g7match.rdg7.model;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "user_roles")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserRoleModel {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "User_id", nullable = false)
    private UserModel userModel;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "Role_id", nullable = false)
    private RoleModel roleModel;

}
