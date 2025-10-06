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

import java.util.NoSuchElementException;

import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.nullValue;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
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
                .id(1l)
                .build();

        Mockito.when(sportService.getById(1l)).thenReturn(
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

        mockMvc.perform(get("/sport/get-by-id/1").param("id", "99"))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.success").value(false))
                .andExpect(jsonPath("$.message", containsString("Error al consultar el deporte")))
                .andExpect(jsonPath("$.data").value(nullValue()));
    }



}
