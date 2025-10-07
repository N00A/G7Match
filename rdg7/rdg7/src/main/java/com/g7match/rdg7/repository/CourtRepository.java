package com.g7match.rdg7.repository;

import com.g7match.rdg7.model.CourtModel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CourtRepository extends JpaRepository<CourtModel,Long> {

    List<CourtModel> findAllByIsActive(boolean isActive);
}
