package com.g7match.rdg7;

import com.g7match.rdg7.dto.ApiResponse;
import com.g7match.rdg7.dto.SportDTO;
import com.g7match.rdg7.exception.NotFoundException;
import com.g7match.rdg7.model.SportModel;
import com.g7match.rdg7.repository.SportRepository;
import com.g7match.rdg7.services.SportService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class SportServiceTest {

    private SportRepository sportRepository;
    private SportService sportService;

    @BeforeEach
    void setUp() {
        sportRepository = mock(SportRepository.class);
        sportService = new SportService(sportRepository);
    }

    @Test
    void testGetAllSports_Success() {
        SportModel sport1 = SportModel.builder()
                .id(1L)
                .name("Fútbol")
                .isActive(true)
                .build();
        
        SportModel sport2 = SportModel.builder()
                .id(2L)
                .name("Básquet")
                .isActive(true)
                .build();

        List<SportModel> activeSports = Arrays.asList(sport1, sport2);
        when(sportRepository.findAllByIsActive(true)).thenReturn(activeSports);

        ApiResponse<List<SportDTO>> response = sportService.getAllSports();

        assertThat(response.isSuccess()).isTrue();
        assertThat(response.getMessage()).isEqualTo("Lista de registros consultada exitosamente");
        assertThat(response.getData()).hasSize(2);
        assertThat(response.getData().get(0).getName()).isEqualTo("Fútbol");
        assertThat(response.getData().get(1).getName()).isEqualTo("Básquet");
        
        verify(sportRepository, times(1)).findAllByIsActive(true);
    }

    @Test
    void testGetById_Success() {
        SportModel sport = SportModel.builder()
                .id(1L)
                .name("Fútbol")
                .isActive(true)
                .build();
        
        when(sportRepository.findById(1L)).thenReturn(Optional.of(sport));

        ApiResponse<SportDTO> response = sportService.getById(1L);

        assertThat(response.isSuccess()).isTrue();
        assertThat(response.getMessage()).isEqualTo("Registro consultado exitosamente");
        assertThat(response.getData().getId()).isEqualTo(1L);
        assertThat(response.getData().getName()).isEqualTo("Fútbol");
        assertThat(response.getData().getIsActive()).isTrue();
        
        verify(sportRepository, times(1)).findById(1L);
    }

    @Test
    void testGetById_NotFound() {
        when(sportRepository.findById(99L)).thenReturn(Optional.empty());

        NotFoundException exception = assertThrows(NotFoundException.class, 
            () -> sportService.getById(99L));
        
        assertThat(exception.getMessage()).contains("No se encontró el deporte con id 99");
        verify(sportRepository, times(1)).findById(99L);
    }

    @Test
    void testCreate_Success() {
        SportDTO sportDTO = SportDTO.builder()
                .name("Tenis")
                .isActive(true)
                .build();

        SportModel savedSport = SportModel.builder()
                .id(1L)
                .name("Tenis")
                .isActive(true)
                .build();

        when(sportRepository.save(any(SportModel.class))).thenReturn(savedSport);

        ApiResponse<SportDTO> response = sportService.create(sportDTO);

        assertThat(response.isSuccess()).isTrue();
        assertThat(response.getMessage()).isEqualTo("Registro creado exitosamente");
        assertThat(response.getData().getId()).isEqualTo(1L);
        assertThat(response.getData().getName()).isEqualTo("Tenis");
        assertThat(response.getData().getIsActive()).isTrue();

        ArgumentCaptor<SportModel> captor = ArgumentCaptor.forClass(SportModel.class);
        verify(sportRepository).save(captor.capture());
        
        SportModel captured = captor.getValue();
        assertThat(captured.getName()).isEqualTo("Tenis");
        assertThat(captured.getIsActive()).isTrue();
    }

    @Test
    void testUpdate_Success() {
        SportDTO sportDTO = SportDTO.builder()
                .id(1L)
                .name("Fútbol Actualizado")
                .isActive(false)
                .build();

        SportModel existingSport = SportModel.builder()
                .id(1L)
                .name("Fútbol")
                .isActive(true)
                .build();

        SportModel updatedSport = SportModel.builder()
                .id(1L)
                .name("Fútbol Actualizado")
                .isActive(false)
                .build();

        when(sportRepository.findById(1L)).thenReturn(Optional.of(existingSport));
        when(sportRepository.save(any(SportModel.class))).thenReturn(updatedSport);

        ApiResponse<SportDTO> response = sportService.update(sportDTO);

        assertThat(response.isSuccess()).isTrue();
        assertThat(response.getMessage()).isEqualTo("Registro actualizado exitosamente");
        assertThat(response.getData().getId()).isEqualTo(1L);
        assertThat(response.getData().getName()).isEqualTo("Fútbol Actualizado");
        assertThat(response.getData().getIsActive()).isFalse();

        verify(sportRepository, times(1)).findById(1L);
        verify(sportRepository, times(1)).save(existingSport);
    }

    @Test
    void testUpdate_NotFound() {
        SportDTO sportDTO = SportDTO.builder()
                .id(99L)
                .name("Deporte Inexistente")
                .isActive(true)
                .build();

        when(sportRepository.findById(99L)).thenReturn(Optional.empty());

        NotFoundException exception = assertThrows(NotFoundException.class, 
            () -> sportService.update(sportDTO));
        
        assertThat(exception.getMessage()).contains("No se encontró el deporte con id 99");
        verify(sportRepository, times(1)).findById(99L);
        verify(sportRepository, never()).save(any(SportModel.class));
    }

    @Test
    void testDelete_Success() {
        SportModel existingSport = SportModel.builder()
                .id(1L)
                .name("Fútbol")
                .isActive(true)
                .build();

        SportModel deletedSport = SportModel.builder()
                .id(1L)
                .name("Fútbol")
                .isActive(false)
                .build();

        when(sportRepository.findById(1L)).thenReturn(Optional.of(existingSport));
        when(sportRepository.save(any(SportModel.class))).thenReturn(deletedSport);

        ApiResponse<SportDTO> response = sportService.delete(1L);

        assertThat(response.isSuccess()).isTrue();
        assertThat(response.getMessage()).isEqualTo("Registro eliminado exitosamente");
        assertThat(response.getData().getId()).isEqualTo(1L);
        assertThat(response.getData().getName()).isEqualTo("Fútbol");
        assertThat(response.getData().getIsActive()).isFalse();

        verify(sportRepository, times(1)).findById(1L);
        verify(sportRepository, times(1)).save(existingSport);
    }

    @Test
    void testDelete_NotFound() {
        when(sportRepository.findById(99L)).thenReturn(Optional.empty());

        NotFoundException exception = assertThrows(NotFoundException.class, 
            () -> sportService.delete(99L));
        
        assertThat(exception.getMessage()).contains("No se encontró el deporte con id 99");
        verify(sportRepository, times(1)).findById(99L);
        verify(sportRepository, never()).save(any(SportModel.class));
    }

    @Test
    void testMapToDTO() {
        SportModel sportModel = SportModel.builder()
                .id(1L)
                .name("Fútbol")
                .isActive(true)
                .build();

        SportDTO result = sportService.mapToDTO(sportModel);

        assertThat(result.getId()).isEqualTo(1L);
        assertThat(result.getName()).isEqualTo("Fútbol");
        assertThat(result.getIsActive()).isTrue();
    }

    @Test
    void testMapToModel() {
        SportDTO sportDTO = SportDTO.builder()
                .id(1L)
                .name("Fútbol")
                .isActive(true)
                .build();

        SportModel result = sportService.mapToModel(sportDTO);

        assertThat(result.getId()).isEqualTo(1L);
        assertThat(result.getName()).isEqualTo("Fútbol");
        assertThat(result.getIsActive()).isTrue();
    }

    @Test
    void testMapToModel_WithNullIsActive() {
        SportDTO sportDTO = SportDTO.builder()
                .id(1L)
                .name("Fútbol")
                .isActive(null)
                .build();

        SportModel result = sportService.mapToModel(sportDTO);

        assertThat(result.getId()).isEqualTo(1L);
        assertThat(result.getName()).isEqualTo("Fútbol");
        assertThat(result.getIsActive()).isTrue();
    }
}
