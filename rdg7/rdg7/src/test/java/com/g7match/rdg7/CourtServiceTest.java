package com.g7match.rdg7;

import com.g7match.rdg7.dto.ApiResponse;
import com.g7match.rdg7.dto.CourtDTO;
import com.g7match.rdg7.dto.SportDTO;
import com.g7match.rdg7.exception.NotFoundException;
import com.g7match.rdg7.model.CourtModel;
import com.g7match.rdg7.model.SportModel;
import com.g7match.rdg7.repository.CourtRepository;
import com.g7match.rdg7.services.CourtService;
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

class CourtServiceTest {

    private CourtRepository courtRepository;
    private SportService sportService;
    private CourtService courtService;

    @BeforeEach
    void setUp() {
        courtRepository = mock(CourtRepository.class);
        sportService = mock(SportService.class);
        courtService = new CourtService(courtRepository, sportService);
    }

    @Test
    void testGetAll_Success() {
        SportModel sportModel = SportModel.builder()
                .id(1L)
                .name("Fútbol")
                .isActive(true)
                .build();

        CourtModel court1 = CourtModel.builder()
                .id(1)
                .name("Cancha 1")
                .location("Zona Norte")
                .pricePerHour(50000f)
                .sport(sportModel)
                .isActive(true)
                .build();

        CourtModel court2 = CourtModel.builder()
                .id(2)
                .name("Cancha 2")
                .location("Zona Sur")
                .pricePerHour(60000f)
                .sport(sportModel)
                .isActive(true)
                .build();

        List<CourtModel> activeCourts = Arrays.asList(court1, court2);
        when(courtRepository.findAllByIsActive(true)).thenReturn(activeCourts);

        SportDTO sportDTO = SportDTO.builder()
                .id(1L)
                .name("Fútbol")
                .isActive(true)
                .build();

        when(sportService.mapToDTO(any(SportModel.class))).thenReturn(sportDTO);

        ApiResponse<List<CourtDTO>> response = courtService.getAll();

        assertThat(response.isSuccess()).isTrue();
        assertThat(response.getMessage()).isEqualTo("Lista de registros consultada exitosamente");
        assertThat(response.getData()).hasSize(2);
        assertThat(response.getData().get(0).getName()).isEqualTo("Cancha 1");
        assertThat(response.getData().get(1).getName()).isEqualTo("Cancha 2");

        verify(courtRepository, times(1)).findAllByIsActive(true);
    }

    @Test
    void testGetById_Success() {
        SportModel sportModel = SportModel.builder()
                .id(1L)
                .name("Fútbol")
                .isActive(true)
                .build();

        CourtModel court = CourtModel.builder()
                .id(1)
                .name("Cancha 1")
                .location("Zona Norte")
                .pricePerHour(50000f)
                .sport(sportModel)
                .isActive(true)
                .build();

        when(courtRepository.findById(1L)).thenReturn(Optional.of(court));

        SportDTO sportDTO = SportDTO.builder()
                .id(1L)
                .name("Fútbol")
                .isActive(true)
                .build();

        when(sportService.mapToDTO(any(SportModel.class))).thenReturn(sportDTO);

        ApiResponse<CourtDTO> response = courtService.getById(1L);

        assertThat(response.isSuccess()).isTrue();
        assertThat(response.getMessage()).isEqualTo("Registro consultado exitosamente");
        assertThat(response.getData().getId()).isEqualTo(1L);
        assertThat(response.getData().getName()).isEqualTo("Cancha 1");
        assertThat(response.getData().getLocation()).isEqualTo("Zona Norte");
        assertThat(response.getData().getPricePerHour()).isEqualTo(50000f);

        verify(courtRepository, times(1)).findById(1L);
    }

    @Test
    void testGetById_NotFound() {
        when(courtRepository.findById(99L)).thenReturn(Optional.empty());

        NotFoundException exception = assertThrows(NotFoundException.class, 
            () -> courtService.getById(99L));
        
        assertThat(exception.getMessage()).contains("No se encontró el deporte con id 99");
        verify(courtRepository, times(1)).findById(99L);
    }

    @Test
    void testCreate_Success() {
        SportDTO sportDTO = SportDTO.builder()
                .id(1L)
                .name("Fútbol")
                .isActive(true)
                .build();

        CourtDTO courtDTO = CourtDTO.builder()
                .name("Cancha Nueva")
                .location("Zona Centro")
                .pricePerHour(70000f)
                .sportDTO(sportDTO)
                .isActive(true)
                .build();

        SportModel sportModel = SportModel.builder()
                .id(1L)
                .name("Fútbol")
                .isActive(true)
                .build();

        CourtModel savedCourt = CourtModel.builder()
                .id(1)
                .name("Cancha Nueva")
                .location("Zona Centro")
                .pricePerHour(70000f)
                .sport(sportModel)
                .isActive(true)
                .build();

        when(sportService.mapToModel(sportDTO)).thenReturn(sportModel);
        when(courtRepository.save(any(CourtModel.class))).thenReturn(savedCourt);

        ApiResponse<CourtDTO> response = courtService.create(courtDTO);

        assertThat(response.isSuccess()).isTrue();
        assertThat(response.getMessage()).isEqualTo("Registro creado exitosamente");
        assertThat(response.getData().getId()).isEqualTo(1L);
        assertThat(response.getData().getName()).isEqualTo("Cancha Nueva");

        ArgumentCaptor<CourtModel> captor = ArgumentCaptor.forClass(CourtModel.class);
        verify(courtRepository).save(captor.capture());
        
        CourtModel captured = captor.getValue();
        assertThat(captured.getName()).isEqualTo("Cancha Nueva");
        assertThat(captured.getLocation()).isEqualTo("Zona Centro");
        assertThat(captured.getPricePerHour()).isEqualTo(70000f);
    }

    @Test
    void testUpdate_Success() {
        SportDTO sportDTO = SportDTO.builder()
                .id(1L)
                .name("Fútbol")
                .isActive(true)
                .build();

        CourtDTO courtDTO = CourtDTO.builder()
                .id(1L)
                .name("Cancha Actualizada")
                .location("Zona Nueva")
                .pricePerHour(80000f)
                .sportDTO(sportDTO)
                .isActive(false)
                .build();

        SportModel sportModel = SportModel.builder()
                .id(1L)
                .name("Fútbol")
                .isActive(true)
                .build();

        CourtModel existingCourt = CourtModel.builder()
                .id(1)
                .name("Cancha Original")
                .location("Zona Original")
                .pricePerHour(50000f)
                .sport(sportModel)
                .isActive(true)
                .build();

        CourtModel updatedCourt = CourtModel.builder()
                .id(1)
                .name("Cancha Actualizada")
                .location("Zona Nueva")
                .pricePerHour(80000f)
                .sport(sportModel)
                .isActive(false)
                .build();

        when(courtRepository.findById(1L)).thenReturn(Optional.of(existingCourt));
        when(sportService.mapToModel(sportDTO)).thenReturn(sportModel);
        when(courtRepository.save(any(CourtModel.class))).thenReturn(updatedCourt);

        ApiResponse<CourtDTO> response = courtService.update(courtDTO);

        assertThat(response.isSuccess()).isTrue();
        assertThat(response.getMessage()).isEqualTo("Registro actualizado exitosamente");
        assertThat(response.getData().getId()).isEqualTo(1L);
        assertThat(response.getData().getName()).isEqualTo("Cancha Actualizada");

        verify(courtRepository, times(1)).findById(1L);
        verify(courtRepository, times(1)).save(existingCourt);
    }

    @Test
    void testUpdate_NotFound() {
        CourtDTO courtDTO = CourtDTO.builder()
                .id(99L)
                .name("Cancha Inexistente")
                .isActive(true)
                .build();

        when(courtRepository.findById(99L)).thenReturn(Optional.empty());

        NotFoundException exception = assertThrows(NotFoundException.class, 
            () -> courtService.update(courtDTO));
        
        assertThat(exception.getMessage()).contains("No se encontró la cancha con id 99");
        verify(courtRepository, times(1)).findById(99L);
        verify(courtRepository, never()).save(any(CourtModel.class));
    }

    @Test
    void testDelete_Success() {
        SportModel sportModel = SportModel.builder()
                .id(1L)
                .name("Fútbol")
                .isActive(true)
                .build();

        CourtModel existingCourt = CourtModel.builder()
                .id(1)
                .name("Cancha 1")
                .location("Zona Norte")
                .pricePerHour(50000f)
                .sport(sportModel)
                .isActive(true)
                .build();

        CourtModel deletedCourt = CourtModel.builder()
                .id(1)
                .name("Cancha 1")
                .location("Zona Norte")
                .pricePerHour(50000f)
                .sport(sportModel)
                .isActive(false)
                .build();

        when(courtRepository.findById(1L)).thenReturn(Optional.of(existingCourt));
        when(courtRepository.save(any(CourtModel.class))).thenReturn(deletedCourt);

        ApiResponse<CourtDTO> response = courtService.delete(1L);

        assertThat(response.isSuccess()).isTrue();
        assertThat(response.getMessage()).isEqualTo("Registro eliminado exitosamente");
        assertThat(response.getData().getId()).isEqualTo(1L);
        assertThat(response.getData().getName()).isEqualTo("Cancha 1");
        assertThat(response.getData().getIsActive()).isFalse();

        verify(courtRepository, times(1)).findById(1L);
        verify(courtRepository, times(1)).save(existingCourt);
    }

    @Test
    void testDelete_NotFound() {
        when(courtRepository.findById(99L)).thenReturn(Optional.empty());

        NotFoundException exception = assertThrows(NotFoundException.class, 
            () -> courtService.delete(99L));
        
        assertThat(exception.getMessage()).contains("No se encontró la cancha con id 99");
        verify(courtRepository, times(1)).findById(99L);
        verify(courtRepository, never()).save(any(CourtModel.class));
    }

    @Test
    void testMapToDTO() {
        SportModel sportModel = SportModel.builder()
                .id(1L)
                .name("Fútbol")
                .isActive(true)
                .build();

        CourtModel courtModel = CourtModel.builder()
                .id(1)
                .name("Cancha 1")
                .location("Zona Norte")
                .pricePerHour(50000f)
                .sport(sportModel)
                .isActive(true)
                .build();

        SportDTO sportDTO = SportDTO.builder()
                .id(1L)
                .name("Fútbol")
                .isActive(true)
                .build();

        when(sportService.mapToDTO(sportModel)).thenReturn(sportDTO);

        CourtDTO result = courtService.mapToDTO(courtModel);

        assertThat(result.getId()).isEqualTo(1L);
        assertThat(result.getName()).isEqualTo("Cancha 1");
        assertThat(result.getLocation()).isEqualTo("Zona Norte");
        assertThat(result.getPricePerHour()).isEqualTo(50000f);
        assertThat(result.getIsActive()).isTrue();
        assertThat(result.getSportDTO()).isNotNull();
    }

    @Test
    void testMapToModel() {
        SportDTO sportDTO = SportDTO.builder()
                .id(1L)
                .name("Fútbol")
                .isActive(true)
                .build();

        CourtDTO courtDTO = CourtDTO.builder()
                .name("Cancha 1")
                .location("Zona Norte")
                .pricePerHour(50000f)
                .sportDTO(sportDTO)
                .isActive(true)
                .build();

        SportModel sportModel = SportModel.builder()
                .id(1L)
                .name("Fútbol")
                .isActive(true)
                .build();

        when(sportService.mapToModel(sportDTO)).thenReturn(sportModel);

        CourtModel result = courtService.mapToModel(courtDTO);

        assertThat(result.getName()).isEqualTo("Cancha 1");
        assertThat(result.getLocation()).isEqualTo("Zona Norte");
        assertThat(result.getPricePerHour()).isEqualTo(50000f);
        assertThat(result.getIsActive()).isTrue();
        assertThat(result.getSport()).isNotNull();
    }
}
