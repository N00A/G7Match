package com.g7match.rdg7.repository;

import com.g7match.rdg7.model.UserRoleModel;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRoleRepository extends JpaRepository<UserRoleModel,Long> {
}
