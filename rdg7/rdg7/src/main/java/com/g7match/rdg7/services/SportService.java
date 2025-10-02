package com.g7match.rdg7.services;

import com.g7match.rdg7.dto.ApiResponse;
import com.g7match.rdg7.dto.SportDTO;
import com.g7match.rdg7.model.SportModel;
import com.g7match.rdg7.repository.SportRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class SportService {

    private final SportRepository sportRepository;


    public SportService(SportRepository sportRepository) {
        this.sportRepository = sportRepository;
    }

    public ApiResponse<List<SportDTO>> getAllSports() {
        List<SportDTO> sports =
                sportRepository.findAll().stream().map(this::mapToDTO).toList();

        return new ApiResponse<>(true,
                "Lista de registros consultada exitosamente", sports);
    }

    public SportDTO mapToDTO(SportModel sportModel) {
        return SportDTO.builder()
                .id(Long.valueOf(sportModel.getId()))
                .name(sportModel.getName())
                .build();
    }

}
