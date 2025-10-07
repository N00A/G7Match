package com.g7match.rdg7;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.g7match.rdg7.controller.ReservationController;
import com.g7match.rdg7.dto.ApiResponse;
import com.g7match.rdg7.dto.ReservationDTO;
import com.g7match.rdg7.exception.NotFoundException;
import com.g7match.rdg7.services.ReservationService;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Import;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Date;
import java.util.ArrayList;
import java.util.List;

import static org.hamcrest.Matchers.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(ReservationController.class)
@Import(ReservationControllerTest.MockConfig.class)
class ReservationControllerTest {

    @TestConfiguration
    static class MockConfig {
        @Bean
        public ReservationService reservationService() {
            return Mockito.mock(ReservationService.class);
        }
    }

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ReservationService reservationService;

    private final ObjectMapper objectMapper = new ObjectMapper();

    @AfterEach
    void resetMocks() {
        Mockito.reset(reservationService);
    }

    @Test
    void testGetByIdSuccess() throws Exception {
        ReservationDTO reservationDTO = ReservationDTO.builder()
                .id(1L)
                .startAt(new Date(System.currentTimeMillis() + 24 * 60 * 60 * 1000)) // mañana
                .endAt(new Date(System.currentTimeMillis() + 24 * 60 * 60 * 1000 + 2 * 60 * 60 * 1000)) // mañana + 2 horas
                .statusCode("ACTIVE")
                .build();

        Mockito.when(reservationService.getById(1L)).thenReturn(
                new ApiResponse<>(
                        true,
                        "Registro consultado exitosamente",
                        reservationDTO
                )
        );

        mockMvc.perform(get("/reservation/get-by-id/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.message").value("Registro consultado exitosamente"))
                .andExpect(jsonPath("$.data.id").value(1))
                .andExpect(jsonPath("$.data.statusCode").value("ACTIVE"));
    }

    @Test
    void testGetByIdError() throws Exception {
        Mockito.when(reservationService.getById(99L))
                .thenThrow(new NotFoundException("No se encontró la reserva con id 99"));

        mockMvc.perform(get("/reservation/get-by-id/99"))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.success").value(false))
                .andExpect(jsonPath("$.message", containsString("No se encontró la reserva con id 99")))
                .andExpect(jsonPath("$.data").value(nullValue()));
    }

    @Test
    void testGetAllSuccess() throws Exception {
        List<ReservationDTO> reservationDTOS = new ArrayList<>();

        reservationDTOS.add(ReservationDTO.builder()
                .id(1L)
                .startAt(new Date(System.currentTimeMillis() + 24 * 60 * 60 * 1000)) // mañana
                .endAt(new Date(System.currentTimeMillis() + 24 * 60 * 60 * 1000 + 2 * 60 * 60 * 1000)) // mañana + 2 horas
                .statusCode("ACTIVE")
                .build());
        reservationDTOS.add(ReservationDTO.builder()
                .id(2L)
                .startAt(new Date(System.currentTimeMillis() + 2 * 24 * 60 * 60 * 1000)) // pasado mañana
                .endAt(new Date(System.currentTimeMillis() + 2 * 24 * 60 * 60 * 1000 + 2 * 60 * 60 * 1000)) // pasado mañana + 2 horas
                .statusCode("CANCELLED")
                .build());

        Mockito.when(reservationService.getAll()).thenReturn(
                new ApiResponse<>(
                        true,
                        "Registro consultado exitosamente",
                        reservationDTOS
                )
        );

        mockMvc.perform(get("/reservation/get-all"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.message").value("Registro consultado exitosamente"))
                .andExpect(jsonPath("$.data", hasSize(2)));
    }

    @Test
    void testCreateSuccess() throws Exception {
        ReservationDTO reservationDTO = ReservationDTO.builder()
                .startAt(new Date(System.currentTimeMillis() + 24 * 60 * 60 * 1000)) // mañana
                .endAt(new Date(System.currentTimeMillis() + 24 * 60 * 60 * 1000 + 2 * 60 * 60 * 1000)) // mañana + 2 horas
                .statusCode("ACTIVE")
                .notes("Reserva de prueba")
                .build();

        ReservationDTO createdReservation = ReservationDTO.builder()
                .id(1L)
                .startAt(new Date(System.currentTimeMillis() + 24 * 60 * 60 * 1000)) // mañana
                .endAt(new Date(System.currentTimeMillis() + 24 * 60 * 60 * 1000 + 2 * 60 * 60 * 1000)) // mañana + 2 horas
                .statusCode("ACTIVE")
                .notes("Reserva de prueba")
                .build();

        Mockito.when(reservationService.create(reservationDTO)).thenReturn(
                new ApiResponse<>(
                        true,
                        "Reserva creada exitosamente",
                        createdReservation
                )
        );

        mockMvc.perform(post("/reservation/create")
                        .contentType("application/json")
                        .content(objectMapper.writeValueAsString(reservationDTO)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.message").value("Reserva creada exitosamente"))
                .andExpect(jsonPath("$.data.id").value(1))
                .andExpect(jsonPath("$.data.statusCode").value("ACTIVE"));
    }

    @Test
    void testCreateError() throws Exception {
        ReservationDTO reservationDTO = ReservationDTO.builder()
                .startAt(new Date(System.currentTimeMillis() - 24 * 60 * 60 * 1000)) // ayer
                .endAt(new Date(System.currentTimeMillis() - 24 * 60 * 60 * 1000 + 2 * 60 * 60 * 1000)) // ayer + 2 horas
                .statusCode("ACTIVE")
                .build();

        Mockito.when(reservationService.create(reservationDTO))
                .thenThrow(new IllegalArgumentException("La fecha de reserva no puede ser en el pasado"));

        mockMvc.perform(post("/reservation/create")
                        .contentType("application/json")
                        .content(objectMapper.writeValueAsString(reservationDTO)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.success").value(false))
                .andExpect(jsonPath("$.message", containsString("La fecha de reserva no puede ser en el pasado")))
                .andExpect(jsonPath("$.data").value(nullValue()));
    }

    @Test
    void testUpdateSuccess() throws Exception {
        ReservationDTO reservationDTO = ReservationDTO.builder()
                .id(1L)
                .startAt(new Date(System.currentTimeMillis() + 2 * 24 * 60 * 60 * 1000)) // pasado mañana
                .endAt(new Date(System.currentTimeMillis() + 2 * 24 * 60 * 60 * 1000 + 3 * 60 * 60 * 1000)) // pasado mañana + 3 horas
                .statusCode("UPDATED")
                .notes("Reserva actualizada")
                .build();

        Mockito.when(reservationService.update(reservationDTO)).thenReturn(
                new ApiResponse<>(
                        true,
                        "Reserva actualizada exitosamente",
                        reservationDTO
                )
        );

        mockMvc.perform(put("/reservation/update-by-id")
                        .contentType("application/json")
                        .content(objectMapper.writeValueAsString(reservationDTO)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.message").value("Reserva actualizada exitosamente"))
                .andExpect(jsonPath("$.data.id").value(1))
                .andExpect(jsonPath("$.data.statusCode").value("UPDATED"));
    }

    @Test
    void testUpdateError() throws Exception {
        ReservationDTO reservationDTO = ReservationDTO.builder()
                .id(99L)
                .startAt(new Date(System.currentTimeMillis() + 24 * 60 * 60 * 1000)) // mañana
                .endAt(new Date(System.currentTimeMillis() + 24 * 60 * 60 * 1000 + 2 * 60 * 60 * 1000)) // mañana + 2 horas
                .statusCode("ACTIVE")
                .build();

        Mockito.when(reservationService.update(reservationDTO))
                .thenThrow(new NotFoundException("No se encontró la reserva con id 99"));

        mockMvc.perform(put("/reservation/update-by-id")
                        .contentType("application/json")
                        .content(objectMapper.writeValueAsString(reservationDTO)))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.success").value(false))
                .andExpect(jsonPath("$.message", containsString("No se encontró la reserva con id 99")))
                .andExpect(jsonPath("$.data").value(nullValue()));
    }

    @Test
    void testDeleteSuccess() throws Exception {
        Mockito.when(reservationService.delete(1L)).thenReturn(
                new ApiResponse<>(
                        true,
                        "Reserva eliminada exitosamente",
                        null
                )
        );

        mockMvc.perform(delete("/reservation/delete-by-id/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.message").value("Reserva eliminada exitosamente"))
                .andExpect(jsonPath("$.data").value(nullValue()));
    }

    @Test
    void testDeleteError() throws Exception {
        Mockito.when(reservationService.delete(99L))
                .thenThrow(new NotFoundException("No se encontró la reserva con id 99"));

        mockMvc.perform(delete("/reservation/delete-by-id/99"))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.success").value(false))
                .andExpect(jsonPath("$.message", containsString("No se encontró la reserva con id 99")))
                .andExpect(jsonPath("$.data").value(nullValue()));
    }

    @Test
    void testGetAllError() throws Exception {
        Mockito.when(reservationService.getAll())
                .thenThrow(new RuntimeException("Error interno del servidor"));

        mockMvc.perform(get("/reservation/get-all"))
                .andExpect(status().isInternalServerError())
                .andExpect(jsonPath("$.success").value(false))
                .andExpect(jsonPath("$.message", containsString("Error interno del servidor")))
                .andExpect(jsonPath("$.data").value(nullValue()));
    }

}
