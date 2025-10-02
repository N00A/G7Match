package com.g7match.rdg7.services;

import com.g7match.rdg7.exception.NotFoundException;
import com.g7match.rdg7.model.RoleModel;
import com.g7match.rdg7.repository.RoleRepository;
import org.springframework.stereotype.Service;

@Service
public class RoleService {

    private final RoleRepository roleRepository;

    public RoleService(RoleRepository roleRepository) {
        this.roleRepository = roleRepository;
    }

    public RoleModel findById(Long id) {
        return roleRepository.findById(id).orElseThrow();
    }
}
