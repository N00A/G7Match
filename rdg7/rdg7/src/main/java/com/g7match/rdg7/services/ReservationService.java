package com.g7match.rdg7.services;

import com.g7match.rdg7.dto.ApiResponse;
import com.g7match.rdg7.dto.ReservationDTO;
import com.g7match.rdg7.dto.RoleDTO;
import com.g7match.rdg7.exception.NotFoundException;
import com.g7match.rdg7.model.ReservationModel;
import com.g7match.rdg7.model.RoleModel;
import com.g7match.rdg7.repository.ReservationRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ReservationService {

    private final ReservationRepository reservationRepository;

    private final UserService userService;

    private final CourtService courtService;

    public ReservationService(ReservationRepository reservationRepository, UserService userService, CourtService courtService) {
        this.reservationRepository = reservationRepository;
        this.userService = userService;
        this.courtService = courtService;
    }

    public ApiResponse<ReservationDTO> getById(Long id) {
        return new ApiResponse<>(
                true,
                "Registro consultado exitosamente",
                mapToDTO(
                        reservationRepository.findById(id)
                                .orElseThrow(() -> new NotFoundException(
                                        String.format("No se encontró el deporte con id %s", id)
                                ))));
    }

    public ApiResponse<List<ReservationDTO>> getAll(){
        List<ReservationDTO> reservationDTOS =
                reservationRepository.findAll().stream().map(this::mapToDTO).toList();

        return new ApiResponse<>(true,
                "Lista de registros consultada exitosamente", reservationDTOS);
    }

    public ApiResponse<ReservationDTO> create (ReservationDTO reservationDTO){
        reservationRepository.save(this.mapToModel(reservationDTO));
        return new ApiResponse<>(
                true,
                "Registro creado exitosamente",
                reservationDTO
        );
    }

    public ApiResponse<ReservationDTO> update(ReservationDTO reservationDTO){

        ReservationModel reservationModel = reservationRepository.
                findById(reservationDTO.getId()).orElseThrow(() -> new NotFoundException(
                String.format("No se encontró la reserva con id %s", reservationDTO.getId())
        ));

        Optional.ofNullable(userService.mapUserModel(reservationDTO.getUserDTO())).ifPresent(reservationModel::setUser);
        Optional.ofNullable(courtService.mapToModel(reservationDTO.getCourtDTO())).ifPresent(reservationModel::setCourt);
        Optional.ofNullable(reservationDTO.getEndAt()).ifPresent(reservationModel::setEndAt);
        Optional.ofNullable(reservationDTO.getStartAt()).ifPresent(reservationModel::setStartAt);
        Optional.ofNullable(reservationDTO.getStatusCode()).ifPresent(reservationModel::setStatusCode);
        Optional.ofNullable(reservationDTO.getNotes()).ifPresent(reservationModel::setNotes);


        reservationRepository.save(reservationModel);

        return new ApiResponse<>(
                true,
                "Registro actualizado exitosamente",
                reservationDTO
        );
    }


    private ReservationDTO mapToDTO(ReservationModel reservationModel) {
        return ReservationDTO.builder()
                .userDTO(userService.mapUserDTO(reservationModel.getUser()))
                .courtDTO(null)
                .endAt(reservationModel.getEndAt())
                .notes(reservationModel.getNotes())
                .startAt(reservationModel.getStartAt())
                .statusCode(reservationModel.getStatusCode())
                .build();
    }

    private ReservationModel mapToModel(ReservationDTO reservationDTO){
        return ReservationModel.builder()
                .court(null)
                .user(userService.mapUserModel(reservationDTO.getUserDTO()))
                .startAt(reservationDTO.getStartAt())
                .notes(reservationDTO.getNotes())
                .statusCode(reservationDTO.getStatusCode())
                .build();
    }
}
