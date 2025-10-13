package com.g7match.rdg7.repository;

import com.g7match.rdg7.model.SportModel;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface SportRepository extends JpaRepository<SportModel, Long> {

    List<SportModel> findAllByIsActive(Boolean isActive);

}
