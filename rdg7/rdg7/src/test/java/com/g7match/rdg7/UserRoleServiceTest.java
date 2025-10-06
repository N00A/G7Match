package com.g7match.rdg7;

import com.g7match.rdg7.dto.ApiResponse;
import com.g7match.rdg7.dto.UserRoleDTO;
import com.g7match.rdg7.exception.NotFoundException;
import com.g7match.rdg7.model.RoleModel;
import com.g7match.rdg7.model.UserModel;
import com.g7match.rdg7.model.UserRoleModel;
import com.g7match.rdg7.repository.UserRoleRepository;
import com.g7match.rdg7.services.RoleService;
import com.g7match.rdg7.services.UserRoleService;
import com.g7match.rdg7.services.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class UserRoleServiceTest {

    private UserRoleRepository userRoleRepository;
    private UserService userService;
    private RoleService roleService;
    private UserRoleService userRoleService;

    @BeforeEach
    void setUp() {
        userRoleRepository = mock(UserRoleRepository.class);
        userService = mock(UserService.class);
        roleService = mock(RoleService.class);
        userRoleService = new UserRoleService(userRoleRepository, userService, roleService);
    }

    @Test
    void testGetById_Success() {
        UserModel userModel = UserModel.builder()
                .id(1L)
                .firstName("Juan")
                .email("juan@test.com")
                .build();

        RoleModel roleModel = RoleModel.builder()
                .id(1L)
                .name("ADMIN")
                .build();

        UserRoleModel userRole = UserRoleModel.builder()
                .id(1L)
                .userModel(userModel)
                .roleModel(roleModel)
                .build();

        when(userRoleRepository.findById(1L)).thenReturn(Optional.of(userRole));

        ApiResponse<UserRoleDTO> response = userRoleService.getById(1L);

        assertThat(response.isSuccess()).isTrue();
        assertThat(response.getMessage()).isEqualTo("Registro consultado exitosamente");
        assertThat(response.getData().getUserId()).isEqualTo(1L);
        assertThat(response.getData().getRoleId()).isEqualTo(1L);

        verify(userRoleRepository, times(1)).findById(1L);
    }

    @Test
    void testGetById_NotFound() {
        when(userRoleRepository.findById(99L)).thenReturn(Optional.empty());

        NotFoundException exception = assertThrows(NotFoundException.class, 
            () -> userRoleService.getById(99L));
        
        assertThat(exception.getMessage()).contains("No se encontró el registro con el id 99");
        verify(userRoleRepository, times(1)).findById(99L);
    }

    @Test
    void testGetAll_Success() {
        UserModel userModel1 = UserModel.builder()
                .id(1L)
                .firstName("Juan")
                .build();

        UserModel userModel2 = UserModel.builder()
                .id(2L)
                .firstName("María")
                .build();

        RoleModel roleModel1 = RoleModel.builder()
                .id(1L)
                .name("ADMIN")
                .build();

        RoleModel roleModel2 = RoleModel.builder()
                .id(2L)
                .name("USER")
                .build();

        UserRoleModel userRole1 = UserRoleModel.builder()
                .id(1L)
                .userModel(userModel1)
                .roleModel(roleModel1)
                .build();

        UserRoleModel userRole2 = UserRoleModel.builder()
                .id(2L)
                .userModel(userModel2)
                .roleModel(roleModel2)
                .build();

        List<UserRoleModel> userRoles = Arrays.asList(userRole1, userRole2);
        when(userRoleRepository.findAll()).thenReturn(userRoles);

        ApiResponse<List<UserRoleDTO>> response = userRoleService.getAll();

        assertThat(response.isSuccess()).isTrue();
        assertThat(response.getMessage()).isEqualTo("Lista de registros consultada exitosamente");
        assertThat(response.getData()).hasSize(2);
        assertThat(response.getData().get(0).getUserId()).isEqualTo(1L);
        assertThat(response.getData().get(0).getRoleId()).isEqualTo(1L);
        assertThat(response.getData().get(1).getUserId()).isEqualTo(2L);
        assertThat(response.getData().get(1).getRoleId()).isEqualTo(2L);

        verify(userRoleRepository, times(1)).findAll();
    }

    @Test
    void testCreate_Success() {
        UserRoleDTO userRoleDTO = UserRoleDTO.builder()
                .userId(1L)
                .roleId(1L)
                .build();

        UserModel userModel = UserModel.builder()
                .id(1L)
                .firstName("Juan")
                .email("juan@test.com")
                .build();

        RoleModel roleModel = RoleModel.builder()
                .id(1L)
                .name("ADMIN")
                .build();

        UserRoleModel savedUserRole = UserRoleModel.builder()
                .id(1L)
                .userModel(userModel)
                .roleModel(roleModel)
                .build();

        when(userService.findByIdModel(1L)).thenReturn(userModel);
        when(roleService.findById(1L)).thenReturn(roleModel);
        when(userRoleRepository.save(any(UserRoleModel.class))).thenReturn(savedUserRole);

        ApiResponse<UserRoleDTO> response = userRoleService.create(userRoleDTO);

        assertThat(response.isSuccess()).isTrue();
        assertThat(response.getMessage()).isEqualTo("Registro creado exitosamente");
        assertThat(response.getData().getUserId()).isEqualTo(1L);
        assertThat(response.getData().getRoleId()).isEqualTo(1L);

        ArgumentCaptor<UserRoleModel> captor = ArgumentCaptor.forClass(UserRoleModel.class);
        verify(userRoleRepository).save(captor.capture());
        
        UserRoleModel captured = captor.getValue();
        assertThat(captured.getUserModel()).isEqualTo(userModel);
        assertThat(captured.getRoleModel()).isEqualTo(roleModel);

        verify(userService, times(1)).findByIdModel(1L);
        verify(roleService, times(1)).findById(1L);
    }

    @Test
    void testCreate_UserNotFound() {
        UserRoleDTO userRoleDTO = UserRoleDTO.builder()
                .userId(99L)
                .roleId(1L)
                .build();

        RoleModel roleModel = RoleModel.builder()
                .id(1L)
                .name("ADMIN")
                .build();

        when(userService.findByIdModel(99L)).thenReturn(null);
        when(roleService.findById(1L)).thenReturn(roleModel);

        NotFoundException exception = assertThrows(NotFoundException.class, 
            () -> userRoleService.create(userRoleDTO));
        
        assertThat(exception.getMessage()).contains("No se encontró el usuario con id 99");
        verify(userService, times(1)).findByIdModel(99L);
        verify(userRoleRepository, never()).save(any(UserRoleModel.class));
    }

    @Test
    void testCreate_RoleNotFound() {
        UserRoleDTO userRoleDTO = UserRoleDTO.builder()
                .userId(1L)
                .roleId(99L)
                .build();

        UserModel userModel = UserModel.builder()
                .id(1L)
                .firstName("Juan")
                .build();

        when(userService.findByIdModel(1L)).thenReturn(userModel);
        when(roleService.findById(99L)).thenReturn(null);

        NotFoundException exception = assertThrows(NotFoundException.class, 
            () -> userRoleService.create(userRoleDTO));
        
        assertThat(exception.getMessage()).contains("No se encontró el rol con id 99");
        verify(userService, times(1)).findByIdModel(1L);
        verify(roleService, times(1)).findById(99L);
        verify(userRoleRepository, never()).save(any(UserRoleModel.class));
    }

    @Test
    void testDelete_Success() {
        UserModel userModel = UserModel.builder()
                .id(1L)
                .firstName("Juan")
                .build();

        RoleModel roleModel = RoleModel.builder()
                .id(1L)
                .name("ADMIN")
                .build();

        UserRoleModel userRole = UserRoleModel.builder()
                .id(1L)
                .userModel(userModel)
                .roleModel(roleModel)
                .build();

        when(userRoleRepository.findById(1L)).thenReturn(Optional.of(userRole));
        doNothing().when(userRoleRepository).deleteById(1L);

        userRoleService.delete(1L);

        verify(userRoleRepository, times(1)).findById(1L);
        verify(userRoleRepository, times(1)).deleteById(1L);
    }

    @Test
    void testDelete_NotFound() {
        when(userRoleRepository.findById(99L)).thenReturn(Optional.empty());

        NotFoundException exception = assertThrows(NotFoundException.class, 
            () -> userRoleService.delete(99L));
        
        assertThat(exception.getMessage()).contains("No se encontró el registro con el id 99");
        verify(userRoleRepository, times(1)).findById(99L);
        verify(userRoleRepository, never()).deleteById(anyLong());
    }
}
