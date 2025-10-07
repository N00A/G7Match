package com.g7match.rdg7;

import com.g7match.rdg7.dto.SportDTO;
import com.g7match.rdg7.dto.UsersDTO;
import com.g7match.rdg7.model.SportModel;
import com.g7match.rdg7.model.UserModel;
import com.g7match.rdg7.repository.UserRepository;
import com.g7match.rdg7.services.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import java.util.*;

import static org.assertj.core.api.Assertions.as;
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
    void testFindByIdModel_UserFound() {
        UserModel user = new UserModel();
        user.setId(1L);
        user.setFirstName("John");

        when(userRepository.findById(1L)).thenReturn(Optional.of(user));

        UserModel result = userService.findByIdModel(1L);

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
    void testGetAllUsers() {
        List<UserModel> users = Arrays.asList(new UserModel(), new UserModel());
        when(userRepository.findAll()).thenReturn(users);

        List<UserModel> result = userService.getAllUsers();

        assertThat(result).hasSize(2);
        verify(userRepository, times(1)).findAll();
    }

    @Test
    void testSave() {
        UsersDTO dto = UsersDTO.builder()
                .identification("123")
                .password("pass")
                .email("test@mail.com")
                .firstName("John")
                .secondName("D.")
                .lastName("Doe")
                .secondLastName("Smith")
                .phone("555-123")
                .isActive(true)
                .build();

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

        when(userRepository.findById(1L)).thenReturn(Optional.of(existingUser));
        when(userRepository.save(any(UserModel.class))).thenAnswer(invocation -> invocation.getArgument(0));

        UserModel updated = userService.updateUser(1L, UsersDTO.builder()
                .phone("111")
                .email("new@mail.com")
                .password("newPass")
                .build());

        assertThat(updated.getEmail()).isEqualTo("new@mail.com");
        assertThat(updated.getPasswordHash()).isEqualTo("newPass");
        assertThat(updated.getPhone()).isEqualTo("111");

        verify(userRepository, times(1)).findById(1L);
        verify(userRepository, times(1)).save(existingUser);
    }

    @Test
    void testUpdateUser_AllFields() {
        UserModel existingUser = new UserModel();
        existingUser.setId(1L);

        UsersDTO dto = UsersDTO.builder()
                .identification("456")
                .password("updatedPass")
                .email("updated@mail.com")
                .firstName("Alice")
                .secondName("B.")
                .lastName("Wonderland")
                .secondLastName("Smith")
                .phone("777-8888")
                .isActive(true)
                .build();


        when(userRepository.findById(1L)).thenReturn(Optional.of(existingUser));
        when(userRepository.save(any(UserModel.class))).thenAnswer(invocation -> invocation.getArgument(0));

        UserModel updated = userService.updateUser(1L, dto);

        assertThat(updated.getIdentification()).isEqualTo("456");
        assertThat(updated.getPasswordHash()).isEqualTo("updatedPass");
        assertThat(updated.getEmail()).isEqualTo("updated@mail.com");
        assertThat(updated.getFirstName()).isEqualTo("Alice");
        assertThat(updated.getSecondName()).isEqualTo("B.");
        assertThat(updated.getLastName()).isEqualTo("Wonderland");
        assertThat(updated.getSecondLastName()).isEqualTo("Smith");
        assertThat(updated.getPhone()).isEqualTo("777-8888");
        assertThat(updated.getIsActive()).isTrue();
    }

    @Test
    void testUpdateUser_UserNotFound() {
        UsersDTO dto = UsersDTO.builder()
                .email("new@mail.com")
                .password("newPass")
                .phone("111")
                .build();

        when(userRepository.findById(1L)).thenReturn(Optional.empty());

        RuntimeException exception = assertThrows(RuntimeException.class, () -> userService.updateUser(1L, dto));

        assertThat(exception.getMessage()).isEqualTo("User not found with id 1");
        verify(userRepository, times(1)).findById(1L);
        verify(userRepository, never()).save(any(UserModel.class));
    }

    @Test
    void testMapToDTO() {
        UserModel userModel = UserModel.builder()
                .firstName("John")
                .secondName("Jairo")
                .isActive(true)
                .identification("1005959222")
                .phone("30030030")
                .passwordHash("#######").build();

        UsersDTO result = userService.mapUserDTO(userModel);

        assertThat(result.getFirstName()).isEqualTo("John");
        assertThat(result.getSecondName()).isEqualTo("Jairo");
        assertThat(result.getIdentification()).isEqualTo("1005959222");
        assertThat(result.getPhone()).isEqualTo("30030030");
        assertThat(result.getPassword()).isEqualTo("#######");
        assertThat(result.getIsActive()).isTrue();
    }

   @Test
    void testMapToModel() {
        UsersDTO usersDTO = UsersDTO.builder()
                .id(1L)
                .email("jjjjj@kkk.com")
                .firstName("John")
                .secondName("Jairo")
                .identification("1010010101")
                .isActive(true)
                .password("######")
                .phone("202020202")
                .build();

        UserModel result = userService.mapUserModel(usersDTO);


        assertThat(result.getEmail()).isEqualTo("jjjjj@kkk.com");
        assertThat(result.getIdentification()).isEqualTo("1010010101");
        assertThat(result.getSecondName()).isEqualTo("Jairo");
        assertThat(result.getPhone()).isEqualTo("202020202");
        assertThat(result.getPasswordHash()).isEqualTo("######");
        assertThat(result.getFirstName()).isEqualTo("John");
    }

    @Test
    void testMapToModel_WithNullIsActive() {
        UsersDTO usersDTO = UsersDTO.builder()
                .id(1L)
                .email("jjjjj@kkk.com")
                .firstName("John")
                .secondName("Jairo")
                .identification("1010010101")
                .isActive(null)
                .password("######")
                .phone("202020202")
                .build();

        UserModel result = userService.mapUserModel(usersDTO);

        assertThat(result.getEmail()).isEqualTo("jjjjj@kkk.com");
        assertThat(result.getIdentification()).isEqualTo("1010010101");
        assertThat(result.getSecondName()).isEqualTo("Jairo");
        assertThat(result.getPhone()).isEqualTo("202020202");
        assertThat(result.getPasswordHash()).isEqualTo("######");
        assertThat(result.getFirstName()).isEqualTo("John");
    }
}
