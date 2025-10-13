package com.g7match.rdg7.repository;

import com.g7match.rdg7.model.ReservationModel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ReservationRepository extends JpaRepository<ReservationModel,Long> {
}
