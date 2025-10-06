package com.g7match.rdg7.controller;

import com.g7match.rdg7.dto.ApiResponse;
import com.g7match.rdg7.dto.ReservationDTO;
import com.g7match.rdg7.dto.ReservationDTO;
import com.g7match.rdg7.services.ReservationService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/reservation")
@RequiredArgsConstructor(onConstructor_ = @Autowired)
public class ReservationController {

    private final ReservationService reservationService;

    @GetMapping("/get-all")
    public ResponseEntity<ApiResponse<List<ReservationDTO>>> getAll() {
        return new ResponseEntity<>(reservationService.getAll(), HttpStatus.OK);
    }

    @GetMapping("/get-by-id/{id}")
    public ResponseEntity<ApiResponse<ReservationDTO>> getById(@PathVariable Long id) {
        return new ResponseEntity<>(reservationService.getById(id), HttpStatus.OK);
    }

    @PostMapping("/create")
    public ResponseEntity<ApiResponse<ReservationDTO>> create(@RequestBody ReservationDTO reservationDTO) {
        return new ResponseEntity<>(reservationService.create(reservationDTO), HttpStatus.OK);
    }

    @PutMapping("/update-by-id")
    public ResponseEntity<ApiResponse<ReservationDTO>> update(@RequestBody ReservationDTO reservationDTO) {
        return new ResponseEntity<>(reservationService.update(reservationDTO), HttpStatus.OK);
    }

    @DeleteMapping("/delete-by-id/{id}")
    public ResponseEntity<ApiResponse<Void>> delete(@PathVariable Long id) {
        return new ResponseEntity<>(reservationService.delete(id), HttpStatus.OK);
    }
}
