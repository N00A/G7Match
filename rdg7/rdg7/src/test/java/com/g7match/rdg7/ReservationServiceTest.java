package com.g7match.rdg7;

import com.g7match.rdg7.dto.*;
import com.g7match.rdg7.exception.NotFoundException;
import com.g7match.rdg7.model.CourtModel;
import com.g7match.rdg7.model.ReservationModel;
import com.g7match.rdg7.model.UserModel;
import com.g7match.rdg7.repository.CourtRepository;
import com.g7match.rdg7.repository.ReservationRepository;
import com.g7match.rdg7.repository.UserRepository;
import com.g7match.rdg7.services.CourtService;
import com.g7match.rdg7.services.ReservationService;
import com.g7match.rdg7.services.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Date;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;
@ExtendWith(MockitoExtension.class)
class ReservationServiceTest {

    private ReservationRepository reservationRepository;
    private UserRepository userRepository;
    private CourtRepository courtRepository;
    private UserService userService;
    private CourtService courtService;
    private ReservationService reservationService;

    @BeforeEach
    void setUp() {
        reservationRepository = mock(ReservationRepository.class);
        userRepository = mock(UserRepository.class);
        courtRepository = mock(CourtRepository.class);
        userService = mock(UserService.class);
        courtService = mock(CourtService.class);
        reservationService = new ReservationService(
                reservationRepository, userRepository, courtRepository, userService, courtService);
    }

    @Test
    void testGetById_Success() {
        UserModel userModel = UserModel.builder()
                .id(1L)
                .firstName("Juan")
                .email("juan@test.com")
                .build();

        CourtModel courtModel = CourtModel.builder()
                .id(1L)
                .name("Cancha 1")
                .build();

        ReservationModel reservation = ReservationModel.builder()
                .id(1L)
                .user(userModel)
                .court(courtModel)
                .startAt(new Date(System.currentTimeMillis() + 24 * 60 * 60 * 1000)) // mañana
                .endAt(new Date(System.currentTimeMillis() + 24 * 60 * 60 * 1000 + 2 * 60 * 60 * 1000)) // mañana + 2 horas
                .statusCode("ACTIVE")
                .notes("Reserva de prueba")
                .build();

        when(reservationRepository.findById(1L)).thenReturn(Optional.of(reservation));

        UsersDTO userDTO = UsersDTO.builder()
                .id(1L)
                .firstName("Juan")
                .email("juan@test.com")
                .build();

        CourtDTO courtDTO = CourtDTO.builder()
                .id(1L)
                .name("Cancha 1")
                .build();

        when(userService.mapUserDTO(userModel)).thenReturn(userDTO);
        when(courtService.mapToDTO(courtModel)).thenReturn(courtDTO);

        ApiResponse<ReservationDTO> response = reservationService.getById(1L);

        assertThat(response.isSuccess()).isTrue();
        assertThat(response.getMessage()).isEqualTo("Registro consultado exitosamente");
        assertThat(response.getData().getId()).isEqualTo(1L);
        assertThat(response.getData().getStatusCode()).isEqualTo("ACTIVE");
        assertThat(response.getData().getNotes()).isEqualTo("Reserva de prueba");

        verify(reservationRepository, times(1)).findById(1L);
    }

    @Test
    void testGetById_NotFound() {
        when(reservationRepository.findById(99L)).thenReturn(Optional.empty());

        NotFoundException exception = assertThrows(NotFoundException.class, 
            () -> reservationService.getById(99L));
        
        assertThat(exception.getMessage()).contains("No se encontró la reserva con id 99");
        verify(reservationRepository, times(1)).findById(99L);
    }

    @Test
    void testGetAll_Success() {
        UserModel userModel = UserModel.builder()
                .id(1L)
                .firstName("Juan")
                .build();

        CourtModel courtModel = CourtModel.builder()
                .id(1L)
                .name("Cancha 1")
                .build();

        ReservationModel reservation1 = ReservationModel.builder()
                .id(1L)
                .user(userModel)
                .court(courtModel)
                .startAt(new Date(System.currentTimeMillis() + 24 * 60 * 60 * 1000)) // mañana
                .endAt(new Date(System.currentTimeMillis() + 24 * 60 * 60 * 1000 + 2 * 60 * 60 * 1000)) // mañana + 2 horas
                .statusCode("ACTIVE")
                .build();

        ReservationModel reservation2 = ReservationModel.builder()
                .id(2L)
                .user(userModel)
                .court(courtModel)
                .startAt(new Date(System.currentTimeMillis() + 2 * 24 * 60 * 60 * 1000)) // pasado mañana
                .endAt(new Date(System.currentTimeMillis() + 2 * 24 * 60 * 60 * 1000 + 2 * 60 * 60 * 1000)) // pasado mañana + 2 horas
                .statusCode("CANCELLED")
                .build();

        List<ReservationModel> reservations = Arrays.asList(reservation1, reservation2);
        when(reservationRepository.findAll()).thenReturn(reservations);

        UsersDTO userDTO = UsersDTO.builder()
                .id(1L)
                .firstName("Juan")
                .build();

        CourtDTO courtDTO = CourtDTO.builder()
                .id(1L)
                .name("Cancha 1")
                .build();

        when(userService.mapUserDTO(any(UserModel.class))).thenReturn(userDTO);
        when(courtService.mapToDTO(any(CourtModel.class))).thenReturn(courtDTO);

        ApiResponse<List<ReservationDTO>> response = reservationService.getAll();

        assertThat(response.isSuccess()).isTrue();
        assertThat(response.getMessage()).isEqualTo("Lista de registros consultada exitosamente");
        assertThat(response.getData()).hasSize(2);
        assertThat(response.getData().get(0).getId()).isEqualTo(1L);
        assertThat(response.getData().get(1).getId()).isEqualTo(2L);

        verify(reservationRepository, times(1)).findAll();
    }

    @Test
    void testCreate_Success() {
        UsersDTO userDTO = UsersDTO.builder()
                .id(1L)
                .firstName("Juan")
                .email("juan@test.com")
                .build();

        CourtDTO courtDTO = CourtDTO.builder()
                .id(1L)
                .name("Cancha 1")
                .build();

        ReservationDTO reservationDTO = ReservationDTO.builder()
                .userDTO(userDTO)
                .courtDTO(courtDTO)
                .startAt(new Date(System.currentTimeMillis() + 24 * 60 * 60 * 1000)) // mañana
                .endAt(new Date(System.currentTimeMillis() + 24 * 60 * 60 * 1000 + 2 * 60 * 60 * 1000)) // mañana + 2 horas
                .statusCode("ACTIVE")
                .notes("Reserva de prueba")
                .build();

        UserModel userModel = UserModel.builder()
                .id(1L)
                .firstName("Juan")
                .email("juan@test.com")
                .build();

        CourtModel courtModel = CourtModel.builder()
                .id(1L)
                .name("Cancha 1")
                .build();

        when(userRepository.findById(1L)).thenReturn(Optional.of(userModel));
        when(courtRepository.findById(1L)).thenReturn(Optional.of(courtModel));

        ReservationModel savedReservation = ReservationModel.builder()
                .id(1L)
                .user(userModel)
                .court(courtModel)
                .startAt(new Date(System.currentTimeMillis() + 24 * 60 * 60 * 1000)) // mañana
                .endAt(new Date(System.currentTimeMillis() + 24 * 60 * 60 * 1000 + 2 * 60 * 60 * 1000)) // mañana + 2 horas
                .statusCode("ACTIVE")
                .notes("Reserva de prueba")
                .build();

        when(reservationRepository.save(any(ReservationModel.class))).thenReturn(savedReservation);
        // Mock para los mapeos de DTOs
        when(userService.mapUserDTO(any(UserModel.class))).thenReturn(userDTO);
        when(courtService.mapToDTO(any(CourtModel.class))).thenReturn(courtDTO);
        // Nota: no se requiere mock de SportService ya que CourtService está mockeado

        ApiResponse<ReservationDTO> response = reservationService.create(reservationDTO);

        assertThat(response.isSuccess()).isTrue();
        assertThat(response.getMessage()).isEqualTo("Registro creado exitosamente");
        assertThat(response.getData().getId()).isEqualTo(1L);
        assertThat(response.getData().getStatusCode()).isEqualTo("ACTIVE");

        ArgumentCaptor<ReservationModel> captor = ArgumentCaptor.forClass(ReservationModel.class);
        verify(reservationRepository).save(captor.capture());
        
        ReservationModel captured = captor.getValue();
        assertThat(captured.getUser()).isEqualTo(userModel);
        assertThat(captured.getCourt()).isEqualTo(courtModel);
        assertThat(captured.getStatusCode()).isEqualTo("ACTIVE");
        assertThat(captured.getNotes()).isEqualTo("Reserva de prueba");
    }

    @Test
    void testCreate_UserNotFound() {
        UsersDTO userDTO = UsersDTO.builder()
                .id(99L)
                .firstName("Usuario Inexistente")
                .build();

        CourtDTO courtDTO = CourtDTO.builder()
                .id(1L)
                .name("Cancha 1")
                .build();

        ReservationDTO reservationDTO = ReservationDTO.builder()
                .userDTO(userDTO)
                .courtDTO(courtDTO)
                .startAt(new Date(System.currentTimeMillis() + 24 * 60 * 60 * 1000)) // mañana
                .endAt(new Date(System.currentTimeMillis() + 24 * 60 * 60 * 1000 + 2 * 60 * 60 * 1000)) // mañana + 2 horas
                .statusCode("ACTIVE")
                .build();

        when(userRepository.findById(99L)).thenReturn(Optional.empty());

        NotFoundException exception = assertThrows(NotFoundException.class, 
            () -> reservationService.create(reservationDTO));
        
        assertThat(exception.getMessage()).isEqualTo("Usuario no encontrado");
        verify(userRepository, times(1)).findById(99L);
        verify(reservationRepository, never()).save(any(ReservationModel.class));
    }

    @Test
    void testCreate_CourtNotFound() {
        UsersDTO userDTO = UsersDTO.builder()
                .id(1L)
                .firstName("Juan")
                .build();

        CourtDTO courtDTO = CourtDTO.builder()
                .id(99L)
                .name("Cancha Inexistente")
                .build();

        ReservationDTO reservationDTO = ReservationDTO.builder()
                .userDTO(userDTO)
                .courtDTO(courtDTO)
                .startAt(new Date(System.currentTimeMillis() + 24 * 60 * 60 * 1000)) // mañana
                .endAt(new Date(System.currentTimeMillis() + 24 * 60 * 60 * 1000 + 2 * 60 * 60 * 1000)) // mañana + 2 horas
                .statusCode("ACTIVE")
                .build();

        UserModel userModel = UserModel.builder()
                .id(1L)
                .firstName("Juan")
                .build();

        when(userRepository.findById(1L)).thenReturn(Optional.of(userModel));
        when(courtRepository.findById(99L)).thenReturn(Optional.empty());

        NotFoundException exception = assertThrows(NotFoundException.class, 
            () -> reservationService.create(reservationDTO));
        
        assertThat(exception.getMessage()).isEqualTo("Cancha no encontrada");
        verify(userRepository, times(1)).findById(1L);
        verify(courtRepository, times(1)).findById(99L);
        verify(reservationRepository, never()).save(any(ReservationModel.class));
    }

    @Test
    void testUpdate_Success() {
        UsersDTO userDTO = UsersDTO.builder()
                .id(1L)
                .firstName("Juan")
                .build();

        CourtDTO courtDTO = CourtDTO.builder()
                .id(1L)
                .name("Cancha 1")
                .build();

        ReservationDTO reservationDTO = ReservationDTO.builder()
                .id(1L)
                .userDTO(userDTO)
                .courtDTO(courtDTO)
                .startAt(new Date(System.currentTimeMillis() + 2 * 24 * 60 * 60 * 1000)) // pasado mañana
                .endAt(new Date(System.currentTimeMillis() + 2 * 24 * 60 * 60 * 1000 + 3 * 60 * 60 * 1000)) // pasado mañana + 3 horas
                .statusCode("UPDATED")
                .notes("Reserva actualizada")
                .build();

        UserModel userModel = UserModel.builder()
                .id(1L)
                .firstName("Juan")
                .build();

        CourtModel courtModel = CourtModel.builder()
                .id(1L)
                .name("Cancha 1")
                .build();

        ReservationModel existingReservation = ReservationModel.builder()
                .id(1L)
                .user(userModel)
                .court(courtModel)
                .startAt(new Date(System.currentTimeMillis() + 24 * 60 * 60 * 1000)) // mañana
                .endAt(new Date(System.currentTimeMillis() + 24 * 60 * 60 * 1000 + 2 * 60 * 60 * 1000)) // mañana + 2 horas
                .statusCode("ACTIVE")
                .notes("Reserva original")
                .build();

        when(reservationRepository.findById(1L)).thenReturn(Optional.of(existingReservation));
        when(reservationRepository.save(any(ReservationModel.class))).thenReturn(existingReservation);

        ApiResponse<ReservationDTO> response = reservationService.update(reservationDTO);

        assertThat(response.isSuccess()).isTrue();
        assertThat(response.getMessage()).isEqualTo("Registro actualizado exitosamente");
        assertThat(response.getData().getId()).isEqualTo(1L);

        verify(reservationRepository, times(1)).findById(1L);
        verify(reservationRepository, times(1)).save(existingReservation);
    }

    @Test
    void testUpdate_NotFound() {
        ReservationDTO reservationDTO = ReservationDTO.builder()
                .id(99L)
                .statusCode("UPDATED")
                .build();

        when(reservationRepository.findById(99L)).thenReturn(Optional.empty());

        NotFoundException exception = assertThrows(NotFoundException.class, 
            () -> reservationService.update(reservationDTO));
        
        assertThat(exception.getMessage()).contains("No se encontró la reserva con id 99");
        verify(reservationRepository, times(1)).findById(99L);
        verify(reservationRepository, never()).save(any(ReservationModel.class));
    }

    @Test
    void testDelete_Success() {
        UserModel userModel = UserModel.builder()
                .id(1L)
                .firstName("Juan")
                .build();

        CourtModel courtModel = CourtModel.builder()
                .id(1L)
                .name("Cancha 1")
                .build();

        ReservationModel reservation = ReservationModel.builder()
                .id(1L)
                .user(userModel)
                .court(courtModel)
                .startAt(new Date(System.currentTimeMillis() + 24 * 60 * 60 * 1000)) // mañana
                .endAt(new Date(System.currentTimeMillis() + 24 * 60 * 60 * 1000 + 2 * 60 * 60 * 1000)) // mañana + 2 horas
                .statusCode("ACTIVE")
                .build();

        when(reservationRepository.findById(1L)).thenReturn(Optional.of(reservation));
        doNothing().when(reservationRepository).delete(reservation);

        ApiResponse<Void> response = reservationService.delete(1L);

        assertThat(response.isSuccess()).isTrue();
        assertThat(response.getMessage()).isEqualTo("Reserva eliminada exitosamente");
        assertThat(response.getData()).isNull();

        verify(reservationRepository, times(1)).findById(1L);
        verify(reservationRepository, times(1)).delete(reservation);
    }

    @Test
    void testDelete_NotFound() {
        when(reservationRepository.findById(99L)).thenReturn(Optional.empty());

        NotFoundException exception = assertThrows(NotFoundException.class, 
            () -> reservationService.delete(99L));
        
        assertThat(exception.getMessage()).contains("No se encontró la reserva con id 99");
        verify(reservationRepository, times(1)).findById(99L);
        verify(reservationRepository, never()).delete(any(ReservationModel.class));
    }
}
