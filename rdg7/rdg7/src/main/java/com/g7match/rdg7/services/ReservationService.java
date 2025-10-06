package com.g7match.rdg7.services;

import com.g7match.rdg7.dto.ApiResponse;
import com.g7match.rdg7.dto.ReservationDTO;
import com.g7match.rdg7.exception.NotFoundException;
import com.g7match.rdg7.model.CourtModel;
import com.g7match.rdg7.model.ReservationModel;
import com.g7match.rdg7.model.UserModel;
import com.g7match.rdg7.repository.CourtRepository;
import com.g7match.rdg7.repository.ReservationRepository;
import com.g7match.rdg7.repository.UserRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ReservationService {

    private final ReservationRepository reservationRepository;
    private final UserRepository userRepository;
    private final CourtRepository courtRepository;
    private final UserService userService;
    private final CourtService courtService;

    private static final String RESERVATION_NOT_FOUND_MSG = "No se encontró la reserva con id %s";

    public ReservationService(
            ReservationRepository reservationRepository,
            UserRepository userRepository,
            CourtRepository courtRepository,
            UserService userService,
            CourtService courtService) {
        this.reservationRepository = reservationRepository;
        this.userRepository = userRepository;
        this.courtRepository = courtRepository;
        this.userService = userService;
        this.courtService = courtService;
    }

    // === GET BY ID ===
    public ApiResponse<ReservationDTO> getById(Long id) {
        ReservationModel model = reservationRepository.findById(id)
                .orElseThrow(() -> new NotFoundException(
                        String.format(RESERVATION_NOT_FOUND_MSG, id)
                ));
        return new ApiResponse<>(true, "Registro consultado exitosamente", mapToDTO(model));
    }

    // === GET ALL ===
    public ApiResponse<List<ReservationDTO>> getAll() {
        List<ReservationDTO> list = reservationRepository.findAll().stream()
                .map(this::mapToDTO)
                .toList();
        return new ApiResponse<>(true, "Lista de registros consultada exitosamente", list);
    }

    // === CREATE ===
    public ApiResponse<ReservationDTO> create(ReservationDTO dto) {

        UserModel user = userRepository.findById(dto.getUserDTO().getId())
                .orElseThrow(() -> new NotFoundException("Usuario no encontrado"));
        CourtModel court = courtRepository.findById(dto.getCourtDTO().getId())
                .orElseThrow(() -> new NotFoundException("Cancha no encontrada"));

        ReservationModel model = ReservationModel.builder()
                .user(user)
                .court(court)
                .startAt(dto.getStartAt())
                .endAt(dto.getEndAt())
                .statusCode(dto.getStatusCode())
                .notes(dto.getNotes())
                .build();

        ReservationModel saved = reservationRepository.save(model);

        return new ApiResponse<>(true, "Registro creado exitosamente", mapToDTO(saved));
    }

    // === UPDATE ===
    public ApiResponse<ReservationDTO> update(ReservationDTO dto) {
        ReservationModel existing = reservationRepository.findById(dto.getId())
                .orElseThrow(() -> new NotFoundException(
                        String.format(RESERVATION_NOT_FOUND_MSG, dto.getId())
                ));

        Optional.ofNullable(dto.getStartAt()).ifPresent(existing::setStartAt);
        Optional.ofNullable(dto.getEndAt()).ifPresent(existing::setEndAt);
        Optional.ofNullable(dto.getStatusCode()).ifPresent(existing::setStatusCode);
        Optional.ofNullable(dto.getNotes()).ifPresent(existing::setNotes);

        if (dto.getUserDTO() != null && dto.getUserDTO().getId() != null) {
            UserModel user = userRepository.findById(dto.getUserDTO().getId())
                    .orElseThrow(() -> new NotFoundException("Usuario no encontrado"));
            existing.setUser(user);
        }

        if (dto.getCourtDTO() != null && dto.getCourtDTO().getId() != null) {
            CourtModel court = courtRepository.findById(dto.getCourtDTO().getId())
                    .orElseThrow(() -> new NotFoundException("Cancha no encontrada"));
            existing.setCourt(court);
        }

        reservationRepository.save(existing);
        return new ApiResponse<>(true, "Registro actualizado exitosamente", mapToDTO(existing));
    }

    // === DELETE ===
    public ApiResponse<Void> delete(Long id) {
        ReservationModel model = reservationRepository.findById(id)
                .orElseThrow(() -> new NotFoundException(
                        String.format(RESERVATION_NOT_FOUND_MSG, id)
                ));
        reservationRepository.delete(model);
        return new ApiResponse<>(true, "Reserva eliminada exitosamente", null);
    }

    // === MAP TO DTO ===
    private ReservationDTO mapToDTO(ReservationModel model) {
        var userDTO = userService.mapUserDTO(model.getUser());

        return ReservationDTO.builder()
                .id(model.getId() != null ? model.getId().longValue() : null)
                .userDTO(userDTO)
                .courtDTO(courtService.mapToDTO(model.getCourt()))
                .startAt(model.getStartAt())
                .endAt(model.getEndAt())
                .statusCode(model.getStatusCode())
                .notes(model.getNotes())
                .build();
    }

}
