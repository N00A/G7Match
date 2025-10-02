package com.g7match.rdg7.controller;

import com.g7match.rdg7.dto.ApiResponse;
import com.g7match.rdg7.dto.SportDTO;
import com.g7match.rdg7.services.SportService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/sport")
@RequiredArgsConstructor(onConstructor_ = @Autowired)
public class SportController {

    private final SportService sportService;

    @GetMapping("/all")
    public ResponseEntity<ApiResponse<SportDTO>> getAllSports() {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(
                new ApiResponse<>(false, "Error al consultar el usuario: "
                        + e.getMessage(), null)
    }
}
