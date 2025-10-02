package com.g7match.rdg7.controller;

import com.g7match.rdg7.dto.ApiResponse;
import com.g7match.rdg7.dto.UserRoleDTO;
import com.g7match.rdg7.model.UserModel;
import com.g7match.rdg7.model.UserRoleModel;
import com.g7match.rdg7.services.UserRoleService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/user-role")
@RequiredArgsConstructor(onConstructor_ = @Autowired)
public class UserRoleController {

    private final UserRoleService userRoleService;

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<UserRoleDTO>> getById(Long id){
        return new ResponseEntity<>(userRoleService.getById(id), HttpStatus.OK);
    }

    @GetMapping("all")
    public ResponseEntity<ApiResponse<List<UserRoleDTO>>> getAll(){
        return new ResponseEntity<>(userRoleService.getAll(), HttpStatus.OK);
    }

    @PostMapping("/create")
    public ResponseEntity<ApiResponse<UserRoleDTO>> createUserRole(UserRoleDTO userRoleDTO) {
        return new ResponseEntity<>(userRoleService.create(userRoleDTO), HttpStatus.OK);
    }

    @PostMapping("/delete")
    public ResponseEntity<ApiResponse<UserRoleDTO>> deleteUserRole(Long id) {
        return new ResponseEntity<>(userRoleService.delete(id), HttpStatus.OK);
    }
}
