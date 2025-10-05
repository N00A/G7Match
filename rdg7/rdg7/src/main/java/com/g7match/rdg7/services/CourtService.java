package com.g7match.rdg7.services;

import com.g7match.rdg7.dto.ApiResponse;
import com.g7match.rdg7.dto.CourtDTO;
import com.g7match.rdg7.dto.SportDTO;
import com.g7match.rdg7.exception.NotFoundException;
import com.g7match.rdg7.model.CourtModel;
import com.g7match.rdg7.model.SportModel;
import com.g7match.rdg7.repository.CourtRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class CourtService {

    private final CourtRepository courtRepository;

    private final SportService sportService;

    public CourtService(CourtRepository courtRepository, SportService sportService) {
        this.courtRepository = courtRepository;
        this.sportService = sportService;
    }

    public ApiResponse<List<CourtDTO>> getAll() {
        List<CourtDTO> sports =
                courtRepository.findAllByIsActive(true)
                        .stream().map(this::mapToDTO).toList();

        return new ApiResponse<>(true,
                "Lista de registros consultada exitosamente", sports);
    }

    public ApiResponse<CourtDTO> getById(Long id) {
        return new ApiResponse<>(
                true,
                "Registro consultado exitosamente",
                mapToDTO(
                        courtRepository.findById(id)
                                .orElseThrow(() -> new NotFoundException(
                                        String.format("No se encontró el deporte con id %s", id)
                                ))));
    }

    public ApiResponse<CourtDTO> create(CourtDTO courtDTO){
        courtRepository.save(this.mapToModel(courtDTO));
        return new ApiResponse<>(
                true,
                "Registro creado exitosamente",
                courtDTO
        );
    }

    public ApiResponse<CourtDTO> update(CourtDTO courtDTO){

        CourtModel courtModel = courtRepository.findById(courtDTO.getId()).orElseThrow(() -> new NotFoundException(
                String.format("No se encontró la cancha con id %s", courtDTO.getId())
        ));
        Optional.ofNullable(courtDTO.getName()).ifPresent(courtModel::setName);
        Optional.ofNullable(sportService.mapToModel(courtDTO.getSportDTO())).ifPresent(courtModel::setSport);
        Optional.ofNullable(courtDTO.getLocation()).ifPresent(courtModel::setLocation);
        Optional.ofNullable(courtDTO.getPricePerHour()).ifPresent(courtModel::setPricePerHour);
        Optional.ofNullable(courtDTO.getIsActive()).ifPresent(courtModel::setIsActive);

        courtRepository.save(courtModel);

        return new ApiResponse<>(
                true,
                "Registro actualizado exitosamente",
                courtDTO
        );
    }

    public ApiResponse<CourtDTO> delete(Long id){
        CourtModel courtModel = courtRepository.findById(id).orElseThrow(() -> new NotFoundException(
                String.format("No se encontró la cancha con id %s", id)
        ));
        courtModel.setIsActive(false);
        courtRepository.save(courtModel);
        return new ApiResponse<>(
                true,
                "Registro eliminado exitosamente",
                mapToDTO(courtModel)
        );
    }

    public CourtDTO mapToDTO(CourtModel courtModel) {
        return CourtDTO.builder()
                .sportDTO(sportService.mapToDTO(courtModel.getSport()))
                .isActive(courtModel.getIsActive())
                .name(courtModel.getName())
                .location(courtModel.getLocation())
                .pricePerHour(courtModel.getPricePerHour())
                .build();
    }

    public CourtModel mapToModel(CourtDTO courtDTO){
        return CourtModel.builder()
                .name(courtDTO.getName())
                .pricePerHour(courtDTO.getPricePerHour())
                .location(courtDTO.getLocation())
                .sport(sportService.mapToModel(courtDTO.getSportDTO()))
                .build();
    }
}
