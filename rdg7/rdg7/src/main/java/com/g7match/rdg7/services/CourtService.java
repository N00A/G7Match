package com.g7match.rdg7.services;

import com.g7match.rdg7.repository.CourtRepository;
import org.springframework.stereotype.Service;

@Service
public class CourtService {

    private final CourtRepository courtRepository;

    public CourtService(CourtRepository courtRepository) {
        this.courtRepository = courtRepository;
    }
}
