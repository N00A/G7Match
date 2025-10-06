package com.g7match.rdg7;

import com.g7match.rdg7.dto.ApiResponse;
import com.g7match.rdg7.dto.RoleDTO;
import com.g7match.rdg7.exception.NotFoundException;
import com.g7match.rdg7.model.RoleModel;
import com.g7match.rdg7.repository.RoleRepository;
import com.g7match.rdg7.services.RoleService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class RoleServiceTest {

    private RoleRepository roleRepository;
    private RoleService roleService;

    @BeforeEach
    void setUp() {
        roleRepository = mock(RoleRepository.class);
        roleService = new RoleService(roleRepository);
    }

    @Test
    void testGetById_Success() {
        RoleModel role = RoleModel.builder()
                .id(1L)
                .name("ADMIN")
                .build();

        when(roleRepository.findById(1L)).thenReturn(Optional.of(role));

        ApiResponse<RoleDTO> response = roleService.getById(1L);

        assertThat(response.isSuccess()).isTrue();
        assertThat(response.getMessage()).isEqualTo("Registro consultado exitosamente");
        assertThat(response.getData().getId()).isEqualTo(1L);
        assertThat(response.getData().getName()).isEqualTo("ADMIN");

        verify(roleRepository, times(1)).findById(1L);
    }

    @Test
    void testGetById_NotFound() {
        when(roleRepository.findById(99L)).thenReturn(Optional.empty());

        NotFoundException exception = assertThrows(NotFoundException.class, 
            () -> roleService.getById(99L));
        
        assertThat(exception.getMessage()).contains("No se encontró el deporte con id 99");
        verify(roleRepository, times(1)).findById(99L);
    }

    @Test
    void testGetAll_Success() {
        RoleModel role1 = RoleModel.builder()
                .id(1L)
                .name("ADMIN")
                .build();

        RoleModel role2 = RoleModel.builder()
                .id(2L)
                .name("USER")
                .build();

        List<RoleModel> roles = Arrays.asList(role1, role2);
        when(roleRepository.findAll()).thenReturn(roles);

        ApiResponse<List<RoleDTO>> response = roleService.getAll();

        assertThat(response.isSuccess()).isTrue();
        assertThat(response.getMessage()).isEqualTo("Lista de registros consultada exitosamente");
        assertThat(response.getData()).hasSize(2);
        assertThat(response.getData().get(0).getName()).isEqualTo("ADMIN");
        assertThat(response.getData().get(1).getName()).isEqualTo("USER");

        verify(roleRepository, times(1)).findAll();
    }

    @Test
    void testCreate_Success() {
        RoleDTO roleDTO = RoleDTO.builder()
                .name("MODERATOR")
                .build();

        RoleModel savedRole = RoleModel.builder()
                .id(1L)
                .name("MODERATOR")
                .build();

        when(roleRepository.save(any(RoleModel.class))).thenReturn(savedRole);

        ApiResponse<RoleDTO> response = roleService.create(roleDTO);

        assertThat(response.isSuccess()).isTrue();
        assertThat(response.getMessage()).isEqualTo("Registro creado exitosamente");
        assertThat(response.getData().getName()).isEqualTo("MODERATOR");

        ArgumentCaptor<RoleModel> captor = ArgumentCaptor.forClass(RoleModel.class);
        verify(roleRepository).save(captor.capture());
        
        RoleModel captured = captor.getValue();
        assertThat(captured.getName()).isEqualTo("MODERATOR");
    }

    @Test
    void testUpdate_Success() {
        RoleDTO roleDTO = RoleDTO.builder()
                .id(1L)
                .name("ADMIN_UPDATED")
                .build();

        RoleModel existingRole = RoleModel.builder()
                .id(1L)
                .name("ADMIN")
                .build();

        RoleModel updatedRole = RoleModel.builder()
                .id(1L)
                .name("ADMIN_UPDATED")
                .build();

        when(roleRepository.findById(1L)).thenReturn(Optional.of(existingRole));
        when(roleRepository.save(any(RoleModel.class))).thenReturn(updatedRole);

        ApiResponse<RoleDTO> response = roleService.update(roleDTO);

        assertThat(response.isSuccess()).isTrue();
        assertThat(response.getMessage()).isEqualTo("Registro actualizado exitosamente");
        assertThat(response.getData().getId()).isEqualTo(1L);
        assertThat(response.getData().getName()).isEqualTo("ADMIN_UPDATED");

        verify(roleRepository, times(1)).findById(1L);
        verify(roleRepository, times(1)).save(existingRole);
    }

    @Test
    void testUpdate_NotFound() {
        RoleDTO roleDTO = RoleDTO.builder()
                .id(99L)
                .name("ROL_INEXISTENTE")
                .build();

        when(roleRepository.findById(99L)).thenReturn(Optional.empty());

        NotFoundException exception = assertThrows(NotFoundException.class, 
            () -> roleService.update(roleDTO));
        
        assertThat(exception.getMessage()).contains("No se encontró el rol con id 99");
        verify(roleRepository, times(1)).findById(99L);
        verify(roleRepository, never()).save(any(RoleModel.class));
    }

    @Test
    void testFindById_Success() {
        RoleModel role = RoleModel.builder()
                .id(1L)
                .name("ADMIN")
                .build();

        when(roleRepository.findById(1L)).thenReturn(Optional.of(role));

        RoleModel result = roleService.findById(1L);

        assertThat(result).isNotNull();
        assertThat(result.getId()).isEqualTo(1L);
        assertThat(result.getName()).isEqualTo("ADMIN");

        verify(roleRepository, times(1)).findById(1L);
    }

    @Test
    void testFindById_NotFound() {
        when(roleRepository.findById(99L)).thenReturn(Optional.empty());

        assertThrows(RuntimeException.class, () -> roleService.findById(99L));
        verify(roleRepository, times(1)).findById(99L);
    }
}
