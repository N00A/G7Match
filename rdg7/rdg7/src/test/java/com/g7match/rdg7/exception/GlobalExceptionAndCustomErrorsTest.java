package com.g7match.rdg7.exception;

import com.g7match.rdg7.controller.UserController;
import com.g7match.rdg7.services.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.junit.jupiter.api.Assertions.*;

class GlobalExceptionAndCustomErrorsTest {

    private MockMvc mockMvc;
    private UserService userService;

    @BeforeEach
    void setUp() {
        userService = mock(UserService.class);
        UserController userController = new UserController(userService);
        GlobalExceptionHandler globalExceptionHandler = new GlobalExceptionHandler();

        mockMvc = MockMvcBuilders
                .standaloneSetup(userController)
                .setControllerAdvice(globalExceptionHandler)
                .build();
    }

    @Test
    void testExceptionConstructors() {
        try {
            throw new BadRequestException("Bad request");
        } catch (BadRequestException e) {
            assertNotNull(e);
        }

        try {
            throw new BusinessException("Business error");
        } catch (BusinessException e) {
            assertNotNull(e);
        }

        try {
            throw new NotFoundException("Not found");
        } catch (NotFoundException e) {
            assertNotNull(e);
        }
    }


    @Test
    void shouldHandleBadRequestException() throws Exception {
        when(userService.findById(1L)).thenThrow(new BadRequestException("Parámetro inválido"));

        mockMvc.perform(get("/user").param("id", "1"))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.success").value(false))
                .andExpect(jsonPath("$.message").value("Error al consultar el usuario: Parámetro inválido"));
    }

    @Test
    void shouldHandleBusinessException() throws Exception {
        when(userService.findById(2L)).thenThrow(new BusinessException("Error de negocio"));

        mockMvc.perform(get("/user").param("id", "2"))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.success").value(false))
                .andExpect(jsonPath("$.message").value("Error al consultar el usuario: Error de negocio"));
    }

    @Test
    void shouldHandleNotFoundException() throws Exception {
        when(userService.findById(3L)).thenThrow(new NotFoundException("Usuario no encontrado"));

        mockMvc.perform(get("/user").param("id", "3"))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.success").value(false))
                .andExpect(jsonPath("$.message").value("Error al consultar el usuario: Usuario no encontrado"));
    }

    @Test
    void shouldHandleIllegalArgumentException() throws Exception {
        when(userService.findById(4L)).thenThrow(new IllegalArgumentException("Argumento inválido"));

        mockMvc.perform(get("/user").param("id", "4"))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.success").value(false))
                .andExpect(jsonPath("$.message").value("Error al consultar el usuario: Argumento inválido"));
    }

    @Test
    void shouldHandleGenericException() throws Exception {
        when(userService.findById(5L)).thenThrow(new RuntimeException("Fallo inesperado"));

        mockMvc.perform(get("/user").param("id", "5"))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.success").value(false))
                .andExpect(jsonPath("$.message").value("Error al consultar el usuario: Fallo inesperado"));
    }

    @Test
    void shouldInstantiateGlobalExceptionHandler() {
        GlobalExceptionHandler handler = new GlobalExceptionHandler();
        assertNotNull(handler);
    }

    @Test
    void shouldTriggerAllExceptionHandlersManually() {
        GlobalExceptionHandler handler = new GlobalExceptionHandler();
        assertDoesNotThrow(() -> handler.handleBadRequest(new BadRequestException("Mensaje de prueba")));
        assertDoesNotThrow(() -> handler.handleBusiness(new BusinessException("Mensaje de prueba")));
    }
}
