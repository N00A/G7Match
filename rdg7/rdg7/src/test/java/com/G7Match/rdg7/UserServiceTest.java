package com.G7Match.rdg7;

import com.G7Match.rdg7.Dto.UsersDTO;
import com.G7Match.rdg7.model.UserModel;
import com.G7Match.rdg7.repository.UserRepository;
import com.G7Match.rdg7.services.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;

import java.util.*;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class UserServiceTest {

    private UserRepository userRepository;
    private UserService userService;

    @BeforeEach
    void setUp() {
        userRepository = mock(UserRepository.class);
        userService = new UserService(userRepository);
    }

    @Test
    void testFindById_UserFound() {
        UserModel user = new UserModel();
        user.setId(1L);
        user.setFirstName("John");

        when(userRepository.findById(1L)).thenReturn(Optional.of(user));

        UserModel result = userService.findById(1L);

        assertThat(result).isNotNull();
        assertThat(result.getFirstName()).isEqualTo("John");
        verify(userRepository, times(1)).findById(1L);
    }

    @Test
    void testFindById_UserNotFound() {
        when(userRepository.findById(99L)).thenReturn(Optional.empty());

        assertThrows(NoSuchElementException.class, () -> userService.findById(99L));
        verify(userRepository, times(1)).findById(99L);
    }

    @Test
    void testGetAllClubs() {
        List<UserModel> users = Arrays.asList(new UserModel(), new UserModel());
        when(userRepository.findAll()).thenReturn(users);

        List<UserModel> result = userService.getAllUsers();

        assertThat(result).hasSize(2);
        verify(userRepository, times(1)).findAll();
    }

    @Test
    void testSave() {
        UsersDTO dto = new UsersDTO();
        dto.setIdentification("123");
        dto.setPassword("pass");
        dto.setEmail("test@mail.com");
        dto.setFirstName("John");
        dto.setSecondName("D.");
        dto.setLastName("Doe");
        dto.setSecondLastName("Smith");
        dto.setPhone("555-123");

        UserModel savedUser = new UserModel();
        savedUser.setId(1L);
        savedUser.setEmail("test@mail.com");

        when(userRepository.save(any(UserModel.class))).thenReturn(savedUser);

        UserModel result = userService.save(dto);

        assertThat(result.getId()).isEqualTo(1L);
        assertThat(result.getEmail()).isEqualTo("test@mail.com");

        ArgumentCaptor<UserModel> captor = ArgumentCaptor.forClass(UserModel.class);
        verify(userRepository).save(captor.capture());

        UserModel captured = captor.getValue();
        assertThat(captured.getFirstName()).isEqualTo("John");
        assertThat(captured.getIdentification()).isEqualTo("123");
        assertThat(captured.getIsActive()).isTrue();
    }

    @Test
    void testDeleteById() {
        doNothing().when(userRepository).deleteById(1L);

        userService.deleteById(1L);

        verify(userRepository, times(1)).deleteById(1L);
    }

    @Test
    void testUpdateUser_UserFound() {
        UserModel existingUser = new UserModel();
        existingUser.setId(1L);
        existingUser.setEmail("old@mail.com");
        existingUser.setPasswordHash("oldPass");
        existingUser.setPhone("000");

        UsersDTO dto = new UsersDTO();
        dto.setEmail("new@mail.com");
        dto.setPassword("newPass");
        dto.setPhone("111");

        when(userRepository.findById(1L)).thenReturn(Optional.of(existingUser));
        when(userRepository.save(any(UserModel.class))).thenAnswer(invocation -> invocation.getArgument(0));

        UserModel updated = userService.updateUser(1L, dto);

        assertThat(updated.getEmail()).isEqualTo("new@mail.com");
        assertThat(updated.getPasswordHash()).isEqualTo("newPass");
        assertThat(updated.getPhone()).isEqualTo("111");

        verify(userRepository, times(1)).findById(1L);
        verify(userRepository, times(1)).save(existingUser);
    }

    @Test
    void testUpdateUser_UserNotFound() {
        UsersDTO dto = new UsersDTO();
        dto.setEmail("new@mail.com");
        dto.setPassword("newPass");
        dto.setPhone("111");

        when(userRepository.findById(1L)).thenReturn(Optional.empty());

        RuntimeException exception = assertThrows(RuntimeException.class, () -> userService.updateUser(1L, dto));

        assertThat(exception.getMessage()).isEqualTo("User not found with id 1");
        verify(userRepository, times(1)).findById(1L);
        verify(userRepository, never()).save(any(UserModel.class));
    }
}


