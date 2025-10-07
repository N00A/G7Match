package com.g7match.rdg7;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.g7match.rdg7.controller.UserRoleController;
import com.g7match.rdg7.dto.ApiResponse;
import com.g7match.rdg7.dto.UserRoleDTO;
import com.g7match.rdg7.exception.NotFoundException;
import com.g7match.rdg7.services.UserRoleService;
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

@WebMvcTest(UserRoleController.class)
@Import(UserRoleControllerTest.MockConfig.class)
class UserRoleControllerTest {

    @TestConfiguration
    static class MockConfig {
        @Bean
        public UserRoleService userRoleService() {
            return Mockito.mock(UserRoleService.class);
        }
    }

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private UserRoleService userRoleService;

    private final ObjectMapper objectMapper = new ObjectMapper();

    @AfterEach
    void resetMocks() {
        Mockito.reset(userRoleService);
    }

    @Test
    void testGetByIdSuccess() throws Exception {
        UserRoleDTO userRoleDTO = UserRoleDTO.builder()
                .userId(1L)
                .roleId(1L)
                .build();

        Mockito.when(userRoleService.getById(1L)).thenReturn(
                new ApiResponse<>(
                        true,
                        "Registro consultado exitosamente",
                        userRoleDTO
                )
        );

        mockMvc.perform(get("/user-role/get-by-id/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.message").value("Registro consultado exitosamente"))
                .andExpect(jsonPath("$.data.userId").value(1))
                .andExpect(jsonPath("$.data.roleId").value(1));
    }

    @Test
    void testGetByIdError() throws Exception {
        Mockito.when(userRoleService.getById(99L))
                .thenThrow(new NotFoundException("No se encontró el user-role con id 99"));

        mockMvc.perform(get("/user-role/get-by-id/99"))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.success").value(false))
                .andExpect(jsonPath("$.message", containsString("No se encontró el user-role con id 99")))
                .andExpect(jsonPath("$.data").value(nullValue()));
    }

    @Test
    void testGetAllSuccess() throws Exception {
        List<UserRoleDTO> userRoleDTOS = new ArrayList<>();

        userRoleDTOS.add(UserRoleDTO.builder()
                .userId(1L)
                .roleId(1L)
                .build());
        userRoleDTOS.add(UserRoleDTO.builder()
                .userId(2L)
                .roleId(2L)
                .build());

        Mockito.when(userRoleService.getAll()).thenReturn(
                new ApiResponse<>(
                        true,
                        "Registro consultado exitosamente",
                        userRoleDTOS
                )
        );

        mockMvc.perform(get("/user-role/all"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.message").value("Registro consultado exitosamente"))
                .andExpect(jsonPath("$.data", hasSize(2)));
    }

    @Test
    void testCreateSuccess() throws Exception {
        UserRoleDTO userRoleDTO = UserRoleDTO.builder()
                .userId(1L)
                .roleId(1L)
                .build();

        UserRoleDTO createdUserRole = UserRoleDTO.builder()
                .userId(1L)
                .roleId(1L)
                .build();

        Mockito.when(userRoleService.create(userRoleDTO)).thenReturn(
                new ApiResponse<>(
                        true,
                        "User-Role creado exitosamente",
                        createdUserRole
                )
        );

        mockMvc.perform(post("/user-role/create")
                        .contentType("application/json")
                        .content(objectMapper.writeValueAsString(userRoleDTO)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.message").value("User-Role creado exitosamente"))
                .andExpect(jsonPath("$.data.userId").value(1))
                .andExpect(jsonPath("$.data.roleId").value(1));
    }

    @Test
    void testCreateError() throws Exception {
        UserRoleDTO userRoleDTO = UserRoleDTO.builder()
                .userId(null)
                .roleId(1L)
                .build();

        Mockito.when(userRoleService.create(userRoleDTO))
                .thenThrow(new IllegalArgumentException("El userId no puede ser nulo"));

        mockMvc.perform(post("/user-role/create")
                        .contentType("application/json")
                        .content(objectMapper.writeValueAsString(userRoleDTO)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.success").value(false))
                .andExpect(jsonPath("$.message", containsString("El userId no puede ser nulo")))
                .andExpect(jsonPath("$.data").value(nullValue()));
    }

    @Test
    void testDeleteSuccess() throws Exception {
        Mockito.doNothing().when(userRoleService).delete(1L);

        mockMvc.perform(delete("/user-role/delete-by-id/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.message").value("Registro eliminado correctamente"))
                .andExpect(jsonPath("$.data").value(nullValue()));
    }

    @Test
    void testDeleteError() throws Exception {
        Mockito.doThrow(new NotFoundException("No se encontró el user-role con id 99"))
                .when(userRoleService).delete(99L);

        mockMvc.perform(delete("/user-role/delete-by-id/99"))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.success").value(false))
                .andExpect(jsonPath("$.message", containsString("No se encontró el user-role con id 99")))
                .andExpect(jsonPath("$.data").value(nullValue()));
    }

    @Test
    void testGetAllError() throws Exception {
        Mockito.when(userRoleService.getAll())
                .thenThrow(new RuntimeException("Error interno del servidor"));

        mockMvc.perform(get("/user-role/all"))
                .andExpect(status().isInternalServerError())
                .andExpect(jsonPath("$.success").value(false))
                .andExpect(jsonPath("$.message", containsString("Error interno del servidor")))
                .andExpect(jsonPath("$.data").value(nullValue()));
    }

}
