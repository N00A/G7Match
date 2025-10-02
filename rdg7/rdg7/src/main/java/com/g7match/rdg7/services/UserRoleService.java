package com.g7match.rdg7.services;

import com.g7match.rdg7.dto.ApiResponse;
import com.g7match.rdg7.dto.UserRoleDTO;
import com.g7match.rdg7.exception.NotFoundException;
import com.g7match.rdg7.model.RoleModel;
import com.g7match.rdg7.model.UserModel;
import com.g7match.rdg7.model.UserRoleModel;
import com.g7match.rdg7.repository.UserRepository;
import com.g7match.rdg7.repository.UserRoleRepository;
import org.apache.catalina.User;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
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
            throw new NotFoundException(String.format("No se encontró el registro con el id {}", id));
        }
        return new ApiResponse<>(true, "Registro consultado exitosamente",
                UserRoleDTO.builder()
                        .userId(userRoleModel.getUserModel().getId())
                        .roleId(Long.valueOf(userRoleModel.getRoleModel().getId()))
                        .build());
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
        RoleModel roleModel = roleService.findById(userRoleDTO.getRoleId());

        UserRoleModel userRoleModel = UserRoleModel.builder()
                .userModel(userModel)
                .roleModel(roleModel)
                .build();
        UserRoleModel savedUserRole = userRoleRepository.save(userRoleModel);
        return new ApiResponse<>(true, "Registro creado exitosamente", mapToDTO(savedUserRole));
    }

    public ApiResponse delete(Long id) {
        Optional<UserRoleModel> userRoleModelOptional = userRoleRepository.findById(id);
        if (userRoleModelOptional.isEmpty()) {
            throw new NotFoundException(String.format("No se encontró el registro con el id {}", id));
        }
        userRoleRepository.deleteById(id);
        return new ApiResponse(true, "Registro eliminado exitosamente", null);
    }

    private UserRoleDTO mapToDTO(UserRoleModel userRoleModel) {
        return UserRoleDTO.builder()
                .userId(userRoleModel.getUserModel().getId())
                .roleId(Long.valueOf(userRoleModel.getRoleModel().getId()))
                .build();
    }
}

