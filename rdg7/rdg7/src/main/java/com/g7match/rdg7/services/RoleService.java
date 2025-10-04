package com.g7match.rdg7.services;

import com.g7match.rdg7.dto.ApiResponse;
import com.g7match.rdg7.dto.RoleDTO;
import com.g7match.rdg7.dto.SportDTO;
import com.g7match.rdg7.exception.NotFoundException;
import com.g7match.rdg7.model.RoleModel;
import com.g7match.rdg7.model.SportModel;
import com.g7match.rdg7.repository.RoleRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class RoleService {

    private final RoleRepository roleRepository;

    public RoleService(RoleRepository roleRepository) {
        this.roleRepository = roleRepository;
    }

    public ApiResponse<RoleDTO> getById(Long id) {
        return new ApiResponse<>(
                true,
                "Registro consultado exitosamente",
                mapToDTO(
                        roleRepository.findById(id)
                                .orElseThrow(() -> new NotFoundException(
                                        String.format("No se encontró el deporte con id %s", id)
                                ))));
    }

    public ApiResponse<List<RoleDTO>> getAll(){
        List<RoleDTO> roleDTOS =
                roleRepository.findAll().stream().map(this::mapToDTO).toList();

        return new ApiResponse<>(true,
                "Lista de registros consultada exitosamente", roleDTOS);
    }

    public ApiResponse<RoleDTO> create (RoleDTO roleDTO){
        roleRepository.save(
                RoleModel
                        .builder()
                        .name(roleDTO.getName())
                        .build());
        return new ApiResponse<>(
                true,
                "Registro creado exitosamente",
                roleDTO
        );
    }

    public ApiResponse<RoleDTO> update(RoleDTO roleDTO){

        RoleModel roleModel = roleRepository.findById(roleDTO.getId()).orElseThrow(() -> new NotFoundException(
                String.format("No se encontró el rol con id %s", roleDTO.getId())
        ));

        roleModel.setName(roleDTO.getName());

        roleRepository.save(roleModel);

        return new ApiResponse<>(
                true,
                "Registro actualizado exitosamente",
                roleDTO
        );
    }


    public RoleModel findById(Long id) {
        return roleRepository.findById(id).orElseThrow();
    }

    private RoleDTO mapToDTO(RoleModel roleModel){
        return RoleDTO.builder()
                .id(roleModel.getId())
                .name(roleModel.getName())
                .build();

    }
}
