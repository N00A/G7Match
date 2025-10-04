package com.g7match.rdg7.controller;

import com.g7match.rdg7.dto.ApiResponse;
import com.g7match.rdg7.dto.CourtDTO;
import com.g7match.rdg7.dto.ReservationDTO;
import com.g7match.rdg7.services.CourtService;
import com.g7match.rdg7.services.ReservationService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/court")
@RequiredArgsConstructor(onConstructor_ = @Autowired)
public class CourtController {

    private final CourtService courtService;

    @GetMapping("/get-all")
    public ResponseEntity<ApiResponse<List<CourtDTO>>> getAll(){
        return new ResponseEntity<>(courtService.getAll(), HttpStatus.OK);
    }

    @GetMapping("/get-by-id/{id}")
    public ResponseEntity<ApiResponse<CourtDTO>> getById(@PathVariable Long id){
        return new ResponseEntity<>(courtService.getById(id), HttpStatus.OK);
    }

    @PostMapping("/create")
    public ResponseEntity<ApiResponse<CourtDTO>> create(@RequestBody CourtDTO courtDTO){
        return new ResponseEntity<>(courtService.create(courtDTO), HttpStatus.OK);
    }

    @PutMapping("/update-by-id")
    public ResponseEntity<ApiResponse<CourtDTO>> update(@RequestBody CourtDTO courtDTO){
        return new ResponseEntity<>(courtService.update(courtDTO), HttpStatus.OK);
    }
}
