package com.g7match.rdg7;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.g7match.rdg7.controller.UserController;
import com.g7match.rdg7.dto.UsersDTO;
import com.g7match.rdg7.model.UserModel;
import com.g7match.rdg7.services.UserService;
import org.apache.catalina.User;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.beans.factory.annotation.Autowired;
import java.util.List;
import java.util.NoSuchElementException;
import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.nullValue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(UserController.class)
@Import(UserControllerTest.MockConfig.class)
class UserControllerTest {

    @TestConfiguration
    static class MockConfig {
        @Bean
        public UserService userService() {
            return Mockito.mock(UserService.class);
        }
    }

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private UserService userService;

    private final ObjectMapper objectMapper = new ObjectMapper();

    @AfterEach
    void resetMocks() {
        Mockito.reset(userService);
    }

    @Test
    void testGetById_Success() throws Exception {
        UserModel user = new UserModel();
        user.setId(1L);
        user.setFirstName("Cristian");

        Mockito.when(userService.findById(1L)).thenReturn(user);

        mockMvc.perform(get("/user").param("id", "1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.message").value("Usuario consultado exitosamente"))
                .andExpect(jsonPath("$.data.firstName").value("Cristian"));
    }

    @Test
    void testGetById_Error() throws Exception {
        Mockito.when(userService.findById(99L))
                .thenThrow(new NoSuchElementException("User not found"));

        mockMvc.perform(get("/user").param("id", "99"))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.success").value(false))
                .andExpect(jsonPath("$.message", containsString("Error al consultar el usuario")))
                .andExpect(jsonPath("$.data").value(nullValue()));
    }

    @Test
    void testGetAllUsers_Success() throws Exception {
        UserModel user1 = new UserModel();
        user1.setId(1L);
        UserModel user2 = new UserModel();
        user2.setId(2L);

        Mockito.when(userService.getAllUsers()).thenReturn(List.of(user1, user2));

        mockMvc.perform(get("/user/all"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.message").value("Lista de usuarios consultada exitosamente"))
                .andExpect(jsonPath("$.data", hasSize(2)));
    }

    @Test
    void testGetAllUsers_Error() throws Exception {
        Mockito.when(userService.getAllUsers())
                .thenThrow(new RuntimeException("boom"));

        mockMvc.perform(get("/user/all"))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.success").value(false))
                .andExpect(jsonPath("$.message", containsString("Error al consultar los usuarios")))
                .andExpect(jsonPath("$.data").value(nullValue()));
    }

    @Test
    void testCreateUser_Success() throws Exception {
        UsersDTO dto = UsersDTO.builder()
                .email("cristian@example.com")
                .build();
        dto.setEmail("cristian@example.com");

        UserModel savedUser = new UserModel();
        savedUser.setId(1L);
        savedUser.setEmail("cristian@example.com");

        Mockito.when(userService.save(any(UsersDTO.class))).thenReturn(savedUser);

        mockMvc.perform(post("/user/create")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.message").value("Usuario creado exitosamente"))
                .andExpect(jsonPath("$.data.email").value("cristian@example.com"));
    }

    @Test
    void testCreateUser_Error() throws Exception {
        UsersDTO dto = UsersDTO.builder().email("bad@example.com").build();

        Mockito.when(userService.save(any(UsersDTO.class)))
                .thenThrow(new IllegalArgumentException("invalid"));

        mockMvc.perform(post("/user/create")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.success").value(false))
                .andExpect(jsonPath("$.message", containsString("Error al crear el usuario")))
                .andExpect(jsonPath("$.data").value(nullValue()));
    }

    @Test
    void testDeleteUser_Success() throws Exception {
        Mockito.doNothing().when(userService).deleteById(1L);

        mockMvc.perform(delete("/user/delete/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.message").value("Usuario borrado exitosamente"))
                .andExpect(jsonPath("$.data").value(1));
    }

    @Test
    void testDeleteUser_Error() throws Exception {
        Mockito.doThrow(new RuntimeException("cannot delete"))
                .when(userService).deleteById(7L);

        mockMvc.perform(delete("/user/delete/7"))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.success").value(false))
                .andExpect(jsonPath("$.message", containsString("Error al borrar el usuario")))
                .andExpect(jsonPath("$.data").value(nullValue()));
    }

    @Test
    void testUpdateUser_Success() throws Exception {
        UsersDTO dto = UsersDTO.builder()
                .email("updated@example.com")
                .build();
        dto.setEmail("updated@example.com");

        Mockito.when(userService.updateUser(eq(1L), any(UsersDTO.class)))
                .thenReturn(new UserModel());

        mockMvc.perform(put("/user/update/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.message").value("Usuario actualizado exitosamente"))
                .andExpect(jsonPath("$.data").value(1));
    }

    @Test
    void testUpdateUser_Error() throws Exception {
        UsersDTO dto = UsersDTO.builder()
                .email("bad@example.com")
                .build();

        Mockito.when(userService.updateUser(eq(9L), any(UsersDTO.class)))
                .thenThrow(new RuntimeException("cannot update"));

        mockMvc.perform(put("/user/update/9")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.success").value(false))
                .andExpect(jsonPath("$.message", containsString("Error al intentar actualizar el usuario")))
                .andExpect(jsonPath("$.data").value(nullValue()));
    }
}