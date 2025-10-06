package com.g7match.rdg7;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.g7match.rdg7.controller.RoleController;
import com.g7match.rdg7.dto.ApiResponse;
import com.g7match.rdg7.dto.RoleDTO;
import com.g7match.rdg7.exception.NotFoundException;
import com.g7match.rdg7.services.RoleService;
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

@WebMvcTest(RoleController.class)
@Import(RoleControllerTest.MockConfig.class)
class RoleControllerTest {

    @TestConfiguration
    static class MockConfig {
        @Bean
        public RoleService roleService() {
            return Mockito.mock(RoleService.class);
        }
    }

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private RoleService roleService;

    private final ObjectMapper objectMapper = new ObjectMapper();

    @AfterEach
    void resetMocks() {
        Mockito.reset(roleService);
    }

    @Test
    void testGetByIdSuccess() throws Exception {
        RoleDTO roleDTO = RoleDTO.builder()
                .name("ADMIN")
                .id(1L)
                .build();

        Mockito.when(roleService.getById(1L)).thenReturn(
                new ApiResponse<>(
                        true,
                        "Registro consultado exitosamente",
                        roleDTO
                )
        );

        mockMvc.perform(get("/role/get-by-id/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.message").value("Registro consultado exitosamente"))
                .andExpect(jsonPath("$.data.id").value(1))
                .andExpect(jsonPath("$.data.name").value("ADMIN"));
    }

    @Test
    void testGetByIdError() throws Exception {
        Mockito.when(roleService.getById(99L))
                .thenThrow(new NotFoundException("No se encontró el rol con id 99"));

        mockMvc.perform(get("/role/get-by-id/99"))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.success").value(false))
                .andExpect(jsonPath("$.message", containsString("No se encontró el rol con id 99")))
                .andExpect(jsonPath("$.data").value(nullValue()));
    }

    @Test
    void testGetAllSuccess() throws Exception {
        List<RoleDTO> roleDTOS = new ArrayList<>();

        roleDTOS.add(RoleDTO.builder()
                .id(1L)
                .name("ADMIN")
                .build());
        roleDTOS.add(RoleDTO.builder()
                .id(2L)
                .name("USER")
                .build());

        Mockito.when(roleService.getAll()).thenReturn(
                new ApiResponse<>(
                        true,
                        "Registro consultado exitosamente",
                        roleDTOS
                )
        );

        mockMvc.perform(get("/role/get-all"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.message").value("Registro consultado exitosamente"))
                .andExpect(jsonPath("$.data", hasSize(2)));
    }

    @Test
    void testCreateSuccess() throws Exception {
        RoleDTO roleDTO = RoleDTO.builder()
                .name("MODERATOR")
                .build();

        RoleDTO createdRole = RoleDTO.builder()
                .id(1L)
                .name("MODERATOR")
                .build();

        Mockito.when(roleService.create(roleDTO)).thenReturn(
                new ApiResponse<>(
                        true,
                        "Rol creado exitosamente",
                        createdRole
                )
        );

        mockMvc.perform(post("/role/create")
                        .contentType("application/json")
                        .content(objectMapper.writeValueAsString(roleDTO)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.message").value("Rol creado exitosamente"))
                .andExpect(jsonPath("$.data.id").value(1))
                .andExpect(jsonPath("$.data.name").value("MODERATOR"));
    }

    @Test
    void testCreateError() throws Exception {
        RoleDTO roleDTO = RoleDTO.builder()
                .name("")
                .build();

        Mockito.when(roleService.create(roleDTO))
                .thenThrow(new IllegalArgumentException("El nombre del rol no puede estar vacío"));

        mockMvc.perform(post("/role/create")
                        .contentType("application/json")
                        .content(objectMapper.writeValueAsString(roleDTO)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.success").value(false))
                .andExpect(jsonPath("$.message", containsString("El nombre del rol no puede estar vacío")))
                .andExpect(jsonPath("$.data").value(nullValue()));
    }

    @Test
    void testUpdateSuccess() throws Exception {
        RoleDTO roleDTO = RoleDTO.builder()
                .id(1L)
                .name("ADMIN_UPDATED")
                .build();

        Mockito.when(roleService.update(roleDTO)).thenReturn(
                new ApiResponse<>(
                        true,
                        "Rol actualizado exitosamente",
                        roleDTO
                )
        );

        mockMvc.perform(put("/role/update-by-id")
                        .contentType("application/json")
                        .content(objectMapper.writeValueAsString(roleDTO)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.message").value("Rol actualizado exitosamente"))
                .andExpect(jsonPath("$.data.id").value(1))
                .andExpect(jsonPath("$.data.name").value("ADMIN_UPDATED"));
    }

    @Test
    void testUpdateError() throws Exception {
        RoleDTO roleDTO = RoleDTO.builder()
                .id(99L)
                .name("ROL_INEXISTENTE")
                .build();

        Mockito.when(roleService.update(roleDTO))
                .thenThrow(new NotFoundException("No se encontró el rol con id 99"));

        mockMvc.perform(put("/role/update-by-id")
                        .contentType("application/json")
                        .content(objectMapper.writeValueAsString(roleDTO)))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.success").value(false))
                .andExpect(jsonPath("$.message", containsString("No se encontró el rol con id 99")))
                .andExpect(jsonPath("$.data").value(nullValue()));
    }

    @Test
    void testGetAllError() throws Exception {
        Mockito.when(roleService.getAll())
                .thenThrow(new RuntimeException("Error interno del servidor"));

        mockMvc.perform(get("/role/get-all"))
                .andExpect(status().isInternalServerError())
                .andExpect(jsonPath("$.success").value(false))
                .andExpect(jsonPath("$.message", containsString("Error interno del servidor")))
                .andExpect(jsonPath("$.data").value(nullValue()));
    }

}
