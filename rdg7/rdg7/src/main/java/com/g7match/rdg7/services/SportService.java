package com.g7match.rdg7.services;

import com.g7match.rdg7.dto.ApiResponse;
import com.g7match.rdg7.dto.SportDTO;
import com.g7match.rdg7.dto.UserRoleDTO;
import com.g7match.rdg7.exception.NotFoundException;
import com.g7match.rdg7.model.SportModel;
import com.g7match.rdg7.repository.SportRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class SportService {

    private final SportRepository sportRepository;


    public SportService(SportRepository sportRepository) {
        this.sportRepository = sportRepository;
    }

    public ApiResponse<List<SportDTO>> getAllSports() {
        List<SportDTO> sports =
                sportRepository.findAllByIsActive(true).stream().map(this::mapToDTO).toList();

        return new ApiResponse<>(true,
                "Lista de registros consultada exitosamente", sports);
    }

    public ApiResponse<SportDTO> getById(Long id) {
        return new ApiResponse<>(
                true,
                "Registro consultado exitosamente",
                mapToDTO(
                        sportRepository.findById(id)
                                .orElseThrow(() -> new NotFoundException(
                                        String.format("No se encontró el deporte con id %s", id)
                                ))));
    }

    public ApiResponse<SportDTO> create(SportDTO sportDTO){
        sportRepository.save(
                SportModel
                        .builder()
                        .name(sportDTO.getName())
                        .build());
        return new ApiResponse<>(
                true,
                "Registro creado exitosamente",
                sportDTO
        );
    }

    public ApiResponse<SportDTO> update(SportDTO sportDTO){

        SportModel sportModel = sportRepository.findById(sportDTO.getId()).orElseThrow(() -> new NotFoundException(
                String.format("No se encontró el deporte con id %s", sportDTO.getId())
        ));

        sportModel.setName(sportDTO.getName());
        Optional.ofNullable(sportDTO.getName()).ifPresent(sportModel::setName);
        Optional.ofNullable(sportDTO.getIsActive()).ifPresent(sportModel::setIsActive);
        sportRepository.save(sportModel);

        return new ApiResponse<>(
                true,
                "Registro actualizado exitosamente",
                sportDTO
        );
    }

    public ApiResponse<SportDTO> delete(Long id){
        SportModel sportModel = sportRepository.findById(id).orElseThrow(() -> new NotFoundException(
                String.format("No se encontró el deporte con id %s", id)
        ));
        sportModel.setIsActive(Boolean.FALSE);
        sportRepository.save(sportModel);
        return new ApiResponse<>(
                true,
                "Registro eliminado exitosamente",
                mapToDTO(sportModel)
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
                .name(sportDTO.getName())
                .id(sportDTO.getId())
                .isActive(sportDTO.getIsActive())
                .build();
    }

}
