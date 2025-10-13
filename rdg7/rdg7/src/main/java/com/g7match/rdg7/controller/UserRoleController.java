package com.g7match.rdg7.controller;

import com.g7match.rdg7.dto.ApiResponse;
import com.g7match.rdg7.dto.UserRoleDTO;
import com.g7match.rdg7.services.UserRoleService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/user-role")
@RequiredArgsConstructor(onConstructor_ = @Autowired)
public class UserRoleController {

    private final UserRoleService userRoleService;

    @GetMapping("/get-by-id/{id}")
    public ResponseEntity<ApiResponse<UserRoleDTO>> getById(@PathVariable("id") Long id) {
        return new ResponseEntity<>(userRoleService.getById(id), HttpStatus.OK);
    }

    @GetMapping("/all")
    public ResponseEntity<ApiResponse<List<UserRoleDTO>>> getAll(){
        return new ResponseEntity<>(userRoleService.getAll(), HttpStatus.OK);
    }

    @PostMapping("/create")
    public ResponseEntity<ApiResponse<UserRoleDTO>> createUserRole(@RequestBody UserRoleDTO userRoleDTO) {
        return new ResponseEntity<>(userRoleService.create(userRoleDTO), HttpStatus.OK);
    }
    @DeleteMapping("/delete-by-id/{id}")
    public ResponseEntity<ApiResponse<Void>> deleteUserRole(@PathVariable Long id) {
        userRoleService.delete(id);
        return ResponseEntity.ok(new ApiResponse<>(true, "Registro eliminado correctamente", null));
    }

}
