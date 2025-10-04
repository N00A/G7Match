package com.g7match.rdg7.repository;

import com.g7match.rdg7.model.CourtModel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CourtRepository extends JpaRepository<CourtModel,Long> {
}
