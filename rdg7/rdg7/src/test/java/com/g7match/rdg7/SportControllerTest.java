package com.g7match.rdg7;


import com.fasterxml.jackson.databind.ObjectMapper;
import com.g7match.rdg7.controller.SportController;
import com.g7match.rdg7.dto.ApiResponse;
import com.g7match.rdg7.dto.SportDTO;
import com.g7match.rdg7.exception.NotFoundException;
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
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@WebMvcTest(SportController.class)
@Import(SportControllerTest.MockConfig.class)
class SportControllerTest {

    @TestConfiguration
    static class MockConfig {
        @Bean
        public SportService sportService() {
            return Mockito.mock(SportService.class);
        }
    }

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private SportService sportService;

    private final ObjectMapper objectMapper = new ObjectMapper();


    @AfterEach
    void resetMocks() {
        Mockito.reset(sportService);
    }

    @Test
    void testGetByIdSuccess() throws Exception{
        SportDTO sportDTO = SportDTO.builder()
                .name("Cricket")
                .isActive(true)
                .id(1L)
                .build();

        Mockito.when(sportService.getById(1L)).thenReturn(
                new ApiResponse<>(
                        true,
                        "Registro consultado exitosamente",
                        sportDTO
                )
        );

        mockMvc.perform(get("/sport/get-by-id/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.message").value("Registro consultado exitosamente"))
                .andExpect(jsonPath("$.data.id").value(1))
                .andExpect(jsonPath("$.data.name").value("Cricket"))
                .andExpect(jsonPath("$.data.isActive").value(true));
    }

    @Test
    void testGetBydIdError() throws Exception {
        Mockito.when(sportService.getById(99L))
                .thenThrow(new NotFoundException("No se encontró el deporte con id 99"));

        mockMvc.perform(get("/sport/get-by-id/99").param("id", "99"))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.success").value(false))
                .andExpect(jsonPath("$.message", containsString("No se encontró el deporte con id 99")))
                .andExpect(jsonPath("$.data").value(nullValue()));
    }

    @Test
    void testGetAllSuccess() throws Exception {
        List<SportDTO> sportDTOS = new ArrayList<>();

        sportDTOS.add(SportDTO.builder()
                .id(1L)
                .name("Fútbol")
                .isActive(true)
                .build());
        sportDTOS.add(SportDTO.builder()
                .id(1L)
                .name("Cricket")
                .isActive(true)
                .build());

        Mockito.when(sportService.getAllSports()).thenReturn(
                new ApiResponse<>(
                        true,
                        "Registro consultado exitosamente",
                        sportDTOS
                )
        );

        mockMvc.perform(get("/sport/get-all"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.message").value("Registro consultado exitosamente"))
                .andExpect(jsonPath("$.data", hasSize(2)));
    }

    @Test
    void testCreateSuccess() throws Exception {
        SportDTO sportDTO = SportDTO.builder()
                .id(1L)
                .name("Fútbol")
                .isActive(true)
                .build();

        SportDTO createdSport = SportDTO.builder()
                .id(1L)
                .name("Fútbol")
                .isActive(true)
                .build();

        Mockito.when(sportService.create(sportDTO)).thenReturn(
                new ApiResponse<>(
                        true,
                        "Deporte creado exitosamente",
                        createdSport
                )
        );

        mockMvc.perform(post("/sport/create")
                        .contentType("application/json")
                        .content(objectMapper.writeValueAsString(sportDTO)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.message").value("Deporte creado exitosamente"))
                .andExpect(jsonPath("$.data.id").value(1))
                .andExpect(jsonPath("$.data.name").value("Fútbol"))
                .andExpect(jsonPath("$.data.isActive").value(true));
    }

    @Test
    void testCreateError() throws Exception {
        SportDTO sportDto = SportDTO.builder()
                .name("")
                .isActive(true)
                .build();

        Mockito.when(sportService.create(sportDto))
                .thenThrow(new IllegalArgumentException("El nombre del deporte no puede estar vacío"));

        mockMvc.perform(post("/sport/create")
                        .contentType("application/json")
                        .content(objectMapper.writeValueAsString(sportDto)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.success").value(false))
                .andExpect(jsonPath("$.message", containsString("El nombre del deporte no puede estar vacío")))
                .andExpect(jsonPath("$.data").value(nullValue()));
    }

    @Test
    void testUpdateSuccess() throws Exception {
        SportDTO sportDTO = SportDTO.builder()
                .id(1L)
                .name("Deporte actualizado")
                .isActive(false)
                .build();

        Mockito.when(sportService.update(sportDTO)).thenReturn(
                new ApiResponse<>(
                        true,
                        "Deporte actualizado exitosamente",
                        sportDTO
                )
        );

        mockMvc.perform(put("/sport/update-by-id")
                        .contentType("application/json")
                        .content(objectMapper.writeValueAsString(sportDTO)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.message").value("Deporte actualizado exitosamente"))
                .andExpect(jsonPath("$.data.id").value(1))
                .andExpect(jsonPath("$.data.name").value("Deporte actualizado"))
                .andExpect(jsonPath("$.data.isActive").value(false));
    }

    @Test
    void testUpdateError() throws Exception {
        SportDTO sportDTO = SportDTO.builder()
                .id(99L)
                .name("Deporte actualizado")
                .isActive(false)
                .build();

        Mockito.when(sportService.update(sportDTO))
                .thenThrow(new NotFoundException("No se encontró el deporte con id 99"));

        mockMvc.perform(put("/sport/update-by-id")
                        .contentType("application/json")
                        .content(objectMapper.writeValueAsString(sportDTO)))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.success").value(false))
                .andExpect(jsonPath("$.message", containsString("No se encontró el deporte con id 99")))
                .andExpect(jsonPath("$.data").value(nullValue()));
    }

    @Test
    void testDeleteSuccess() throws Exception {
        SportDTO sportDTO = SportDTO.builder()
                .id(1L)
                .name("Deporte Eliminado")
                .isActive(false)
                .build();

        Mockito.when(sportService.delete(1L)).thenReturn(
                new ApiResponse<>(
                        true,
                        "Deporte eliminado exitosamente",
                        sportDTO
                )
        );

        mockMvc.perform(delete("/sport/delete-by-id/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.message").value("Deporte eliminado exitosamente"))
                .andExpect(jsonPath("$.data.id").value(1))
                .andExpect(jsonPath("$.data.name").value("Deporte Eliminado"))
                .andExpect(jsonPath("$.data.isActive").value(false));
    }

    @Test
    void testDeleteError() throws Exception {
        Mockito.when(sportService.delete(99L))
                .thenThrow(new NotFoundException("No se encontró el deporte con id 99"));

        mockMvc.perform(delete("/sport/delete-by-id/99"))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.success").value(false))
                .andExpect(jsonPath("$.message", containsString("No se encontró el deporte con id 99")))
                .andExpect(jsonPath("$.data").value(nullValue()));
    }

    @Test
    void testGetAllError() throws Exception {
        Mockito.when(sportService.getAllSports())
                .thenThrow(new RuntimeException("Error interno del servidor"));

        mockMvc.perform(get("/sport/get-all"))
                .andExpect(status().isInternalServerError())
                .andExpect(jsonPath("$.success").value(false))
                .andExpect(jsonPath("$.message", containsString("Error interno del servidor")))
                .andExpect(jsonPath("$.data").value(nullValue()));
    }
}
