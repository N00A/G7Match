package com.g7match.rdg7.controller;

import com.g7match.rdg7.dto.ApiResponse;
import com.g7match.rdg7.dto.RoleDTO;
import com.g7match.rdg7.dto.RoleDTO;
import com.g7match.rdg7.services.RoleService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/role")
@RequiredArgsConstructor(onConstructor_ = @Autowired)
public class RoleController {

    private final RoleService roleService;

    @GetMapping("/get-all")
    public ResponseEntity<ApiResponse<List<RoleDTO>>> getAll(){
        return new ResponseEntity<>(roleService.getAll(), HttpStatus.OK);
    }

    @GetMapping("/get-by-id/{id}")
    public ResponseEntity<ApiResponse<RoleDTO>> getById(@PathVariable Long id){
        return new ResponseEntity<>(roleService.getById(id), HttpStatus.OK);
    }

    @PostMapping("/create")
    public ResponseEntity<ApiResponse<RoleDTO>> create(@RequestBody RoleDTO RoleDTO){
        return new ResponseEntity<>(roleService.create(RoleDTO), HttpStatus.OK);
    }

    @PutMapping("/update-by-id")
    public ResponseEntity<ApiResponse<RoleDTO>> update(@RequestBody RoleDTO RoleDTO){
        return new ResponseEntity<>(roleService.update(RoleDTO), HttpStatus.OK);
    }
}
