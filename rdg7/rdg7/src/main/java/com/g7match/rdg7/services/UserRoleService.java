package com.g7match.rdg7.services;

import com.g7match.rdg7.dto.ApiResponse;
import com.g7match.rdg7.dto.UserRoleDTO;
import com.g7match.rdg7.exception.NotFoundException;
import com.g7match.rdg7.model.RoleModel;
import com.g7match.rdg7.model.UserModel;
import com.g7match.rdg7.model.UserRoleModel;
import com.g7match.rdg7.repository.UserRoleRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class UserRoleService {

    private final UserRoleRepository userRoleRepository;

    private final UserService userService;

    private final RoleService roleService;

    public UserRoleService(UserRoleRepository userRoleRepository, UserService userService, RoleService roleService) {
        this.userRoleRepository = userRoleRepository;
        this.userService = userService;
        this.roleService = roleService;
    }

    public ApiResponse<UserRoleDTO> getById(Long id) {

        UserRoleModel userRoleModel = userRoleRepository.findById(id).orElse(null);
        if (userRoleModel == null) {
            throw new NotFoundException(String.format("No se encontró el registro con el id %s", id));
        }
        return new ApiResponse<>(true, "Registro consultado exitosamente",
               mapToDTO(userRoleModel));
    }

    public ApiResponse<List<UserRoleDTO>> getAll() {

        List<UserRoleDTO> userRoleDTOList = userRoleRepository.findAll().stream()
                .map(this::mapToDTO)
                .toList();
        return new ApiResponse<>(true,
                "Lista de registros consultada exitosamente", userRoleDTOList);
    }

    public ApiResponse<UserRoleDTO> create(UserRoleDTO userRoleDTO) {
        UserModel userModel = userService.findByIdModel(userRoleDTO.getUserId());
        if (userModel == null) {
            throw new NotFoundException(
                    String.format("No se encontró el usuario con id %s", userRoleDTO.getUserId())
            );
        }
        RoleModel roleModel = roleService.findById(userRoleDTO.getRoleId());
        if (roleModel == null) {
            throw new NotFoundException(
                    String.format("No se encontró el rol con id %s", userRoleDTO.getRoleId())
            );
        }
        UserRoleModel userRoleModel = UserRoleModel.builder()
                .userModel(userModel)
                .roleModel(roleModel)
                .build();

        UserRoleModel savedUserRole = userRoleRepository.save(userRoleModel);
        return new ApiResponse<>(true, "Registro creado exitosamente", mapToDTO(savedUserRole));
    }


    public void delete(Long id) {
        Optional<UserRoleModel> userRoleModelOptional = userRoleRepository.findById(id);
        if (userRoleModelOptional.isEmpty()) {
            throw new NotFoundException(String.format("No se encontró el registro con el id %s", id));
        }
        userRoleRepository.deleteById(id);
    }

    private UserRoleDTO mapToDTO(UserRoleModel userRoleModel) {
        return UserRoleDTO.builder()
                .userId(userRoleModel.getUserModel().getId())
                .roleId(userRoleModel.getRoleModel().getId())
                .build();
    }
}

