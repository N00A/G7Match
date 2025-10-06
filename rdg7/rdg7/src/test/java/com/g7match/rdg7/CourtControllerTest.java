package com.g7match.rdg7;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.g7match.rdg7.controller.CourtController;
import com.g7match.rdg7.dto.ApiResponse;
import com.g7match.rdg7.dto.CourtDTO;
import com.g7match.rdg7.dto.SportDTO;
import com.g7match.rdg7.exception.NotFoundException;
import com.g7match.rdg7.services.CourtService;
import com.g7match.rdg7.services.SportService;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Import;
import org.springframework.test.web.servlet.MockMvc;

import java.util.ArrayList;
import java.util.List;

import static org.hamcrest.Matchers.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(CourtController.class)
@Import(CourtControllerTest.MockConfig.class)
class CourtControllerTest {

    @TestConfiguration
    static class MockConfig {
        @Bean
        public CourtService courtService() {
            return Mockito.mock(CourtService.class);
        }
        
        @Bean
        public SportService sportService() {
            return Mockito.mock(SportService.class);
        }
    }

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private CourtService courtService;
    
    @Autowired
    private SportService sportService;

    private final ObjectMapper objectMapper = new ObjectMapper();

    @AfterEach
    void resetMocks() {
        Mockito.reset(courtService, sportService);
    }

    @Test
    void testGetByIdSuccess() throws Exception {
        CourtDTO courtDTO = CourtDTO.builder()
                .name("Cancha 1")
                .isActive(true)
                .id(1L)
                .build();

        Mockito.when(courtService.getById(1L)).thenReturn(
                new ApiResponse<>(
                        true,
                        "Registro consultado exitosamente",
                        courtDTO
                )
        );

        mockMvc.perform(get("/court/get-by-id/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.message").value("Registro consultado exitosamente"))
                .andExpect(jsonPath("$.data.id").value(1))
                .andExpect(jsonPath("$.data.name").value("Cancha 1"))
                .andExpect(jsonPath("$.data.isActive").value(true));
    }

    @Test
    void testGetByIdError() throws Exception {
        Mockito.when(courtService.getById(99L))
                .thenThrow(new NotFoundException("No se encontró la cancha con id 99"));

        mockMvc.perform(get("/court/get-by-id/99"))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.success").value(false))
                .andExpect(jsonPath("$.message", containsString("No se encontró la cancha con id 99")))
                .andExpect(jsonPath("$.data").value(nullValue()));
    }

    @Test
    void testGetAllSuccess() throws Exception {
        SportDTO sportDTO = SportDTO.builder()
                .id(1L)
                .name("Fútbol")
                .isActive(true)
                .build();

        List<CourtDTO> courtDTOS = new ArrayList<>();

        courtDTOS.add(CourtDTO.builder()
                .id(1L)
                .isActive(true)
                .name("Cancha 1")
                .sportDTO(sportDTO)
                .build());
        courtDTOS.add(CourtDTO.builder()
                .id(2L)
                .isActive(false)
                .name("Cancha 2")
                .sportDTO(sportDTO)
                .build());

        Mockito.when(courtService.getAll()).thenReturn(
                new ApiResponse<>(
                        true,
                        "Registro consultado exitosamente",
                        courtDTOS
                )
        );

        mockMvc.perform(get("/court/get-all"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.message").value("Registro consultado exitosamente"))
                .andExpect(jsonPath("$.data", hasSize(2)));
    }

    @Test
    void testCreateSuccess() throws Exception {
        CourtDTO courtDTO = CourtDTO.builder()
                .name("Cancha Nueva")
                .isActive(true)
                .build();

        CourtDTO createdCourt = CourtDTO.builder()
                .id(1L)
                .name("Cancha Nueva")
                .isActive(true)
                .build();

        Mockito.when(courtService.create(courtDTO)).thenReturn(
                new ApiResponse<>(
                        true,
                        "Cancha creada exitosamente",
                        createdCourt
                )
        );

        mockMvc.perform(post("/court/create")
                        .contentType("application/json")
                        .content(objectMapper.writeValueAsString(courtDTO)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.message").value("Cancha creada exitosamente"))
                .andExpect(jsonPath("$.data.id").value(1))
                .andExpect(jsonPath("$.data.name").value("Cancha Nueva"))
                .andExpect(jsonPath("$.data.isActive").value(true));
    }

    @Test
    void testCreateError() throws Exception {
        CourtDTO courtDTO = CourtDTO.builder()
                .name("")
                .isActive(true)
                .build();

        Mockito.when(courtService.create(courtDTO))
                .thenThrow(new IllegalArgumentException("El nombre de la cancha no puede estar vacío"));

        mockMvc.perform(post("/court/create")
                        .contentType("application/json")
                        .content(objectMapper.writeValueAsString(courtDTO)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.success").value(false))
                .andExpect(jsonPath("$.message", containsString("El nombre de la cancha no puede estar vacío")))
                .andExpect(jsonPath("$.data").value(nullValue()));
    }

    @Test
    void testUpdateSuccess() throws Exception {
        CourtDTO courtDTO = CourtDTO.builder()
                .id(1L)
                .name("Cancha Actualizada")
                .isActive(false)
                .build();

        Mockito.when(courtService.update(courtDTO)).thenReturn(
                new ApiResponse<>(
                        true,
                        "Cancha actualizada exitosamente",
                        courtDTO
                )
        );

        mockMvc.perform(put("/court/update-by-id")
                        .contentType("application/json")
                        .content(objectMapper.writeValueAsString(courtDTO)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.message").value("Cancha actualizada exitosamente"))
                .andExpect(jsonPath("$.data.id").value(1))
                .andExpect(jsonPath("$.data.name").value("Cancha Actualizada"))
                .andExpect(jsonPath("$.data.isActive").value(false));
    }

    @Test
    void testUpdateError() throws Exception {
        CourtDTO courtDTO = CourtDTO.builder()
                .id(99L)
                .name("Cancha Inexistente")
                .isActive(true)
                .build();

        Mockito.when(courtService.update(courtDTO))
                .thenThrow(new NotFoundException("No se encontró la cancha con id 99"));

        mockMvc.perform(put("/court/update-by-id")
                        .contentType("application/json")
                        .content(objectMapper.writeValueAsString(courtDTO)))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.success").value(false))
                .andExpect(jsonPath("$.message", containsString("No se encontró la cancha con id 99")))
                .andExpect(jsonPath("$.data").value(nullValue()));
    }

    @Test
    void testDeleteSuccess() throws Exception {
        CourtDTO deletedCourt = CourtDTO.builder()
                .id(1L)
                .name("Cancha Eliminada")
                .isActive(false)
                .build();

        Mockito.when(courtService.delete(1L)).thenReturn(
                new ApiResponse<>(
                        true,
                        "Cancha eliminada exitosamente",
                        deletedCourt
                )
        );

        mockMvc.perform(delete("/court/delete-by-id/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.message").value("Cancha eliminada exitosamente"))
                .andExpect(jsonPath("$.data.id").value(1))
                .andExpect(jsonPath("$.data.name").value("Cancha Eliminada"))
                .andExpect(jsonPath("$.data.isActive").value(false));
    }

    @Test
    void testDeleteError() throws Exception {
        Mockito.when(courtService.delete(99L))
                .thenThrow(new NotFoundException("No se encontró la cancha con id 99"));

        mockMvc.perform(delete("/court/delete-by-id/99"))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.success").value(false))
                .andExpect(jsonPath("$.message", containsString("No se encontró la cancha con id 99")))
                .andExpect(jsonPath("$.data").value(nullValue()));
    }

    @Test
    void testGetAllError() throws Exception {
        Mockito.when(courtService.getAll())
                .thenThrow(new RuntimeException("Error interno del servidor"));

        mockMvc.perform(get("/court/get-all"))
                .andExpect(status().isInternalServerError())
                .andExpect(jsonPath("$.success").value(false))
                .andExpect(jsonPath("$.message", containsString("Error interno del servidor")))
                .andExpect(jsonPath("$.data").value(nullValue()));
    }

}
