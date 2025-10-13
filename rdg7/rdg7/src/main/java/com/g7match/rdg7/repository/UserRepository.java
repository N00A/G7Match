package com.g7match.rdg7.repository;

import com.g7match.rdg7.model.UserModel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.lang.NonNull;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<UserModel,Long> {

    @NonNull
    Optional<UserModel> findById(@NonNull Long id);

}