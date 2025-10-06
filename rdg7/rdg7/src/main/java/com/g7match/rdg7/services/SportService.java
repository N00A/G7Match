package com.g7match.rdg7.services;

import com.g7match.rdg7.dto.ApiResponse;
import com.g7match.rdg7.dto.SportDTO;
import com.g7match.rdg7.exception.NotFoundException;
import com.g7match.rdg7.model.SportModel;
import com.g7match.rdg7.repository.SportRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class SportService {

    private final SportRepository sportRepository;

    private static final String SPORT_NOT_FOUND_MSG = "No se encontró el deporte con id %s";

    public SportService(SportRepository sportRepository) {
        this.sportRepository = sportRepository;
    }

    public ApiResponse<List<SportDTO>> getAllSports() {
        List<SportDTO> sports =
                sportRepository.findAllByIsActive(true).stream().map(this::mapToDTO).toList();

        return new ApiResponse<>(
                true,
                "Lista de registros consultada exitosamente",
                sports
        );
    }

    public ApiResponse<SportDTO> getById(Long id) {
        SportModel model = sportRepository.findById(id)
                .orElseThrow(() -> new NotFoundException(
                        String.format(SPORT_NOT_FOUND_MSG, id)
                ));

        return new ApiResponse<>(
                true,
                "Registro consultado exitosamente",
                mapToDTO(model)
        );
    }

    public ApiResponse<SportDTO> create(SportDTO sportDTO){
        // Aseguramos isActive = TRUE al crear
        SportModel toSave = SportModel.builder()
                .name(sportDTO.getName())
                .isActive(Boolean.TRUE)
                .build();

        SportModel saved = sportRepository.save(toSave);

        return new ApiResponse<>(
                true,
                "Registro creado exitosamente",
                mapToDTO(saved)
        );
    }

    public ApiResponse<SportDTO> update(SportDTO sportDTO){

        SportModel sportModel = sportRepository.findById(sportDTO.getId()).orElseThrow(() -> new NotFoundException(
                String.format(SPORT_NOT_FOUND_MSG, sportDTO.getId())
        ));

        Optional.ofNullable(sportDTO.getName()).ifPresent(sportModel::setName);
        Optional.ofNullable(sportDTO.getIsActive()).ifPresent(sportModel::setIsActive);

        SportModel saved = sportRepository.save(sportModel);

        return new ApiResponse<>(
                true,
                "Registro actualizado exitosamente",
                mapToDTO(saved)
        );
    }

    public ApiResponse<SportDTO> delete(Long id){
        SportModel sportModel = sportRepository.findById(id).orElseThrow(() -> new NotFoundException(
                String.format(SPORT_NOT_FOUND_MSG, id)
        ));
        sportModel.setIsActive(Boolean.FALSE);
        SportModel saved = sportRepository.save(sportModel);

        return new ApiResponse<>(
                true,
                "Registro eliminado exitosamente",
                mapToDTO(saved)
        );
    }

    public SportDTO mapToDTO(SportModel sportModel) {
        return SportDTO.builder()
                .id(sportModel.getId())
                .name(sportModel.getName())
                .isActive(sportModel.getIsActive())
                .build();
    }

    public SportModel mapToModel(SportDTO sportDTO){
        return SportModel.builder()
                .id(sportDTO.getId())
                .name(sportDTO.getName())
                // default TRUE si viene nulo
                .isActive(sportDTO.getIsActive() != null ? sportDTO.getIsActive() : Boolean.TRUE)
                .build();
    }
}
