package com.g7match.rdg7.controller;

import com.g7match.rdg7.dto.ApiResponse;
import com.g7match.rdg7.dto.SportDTO;
import com.g7match.rdg7.services.SportService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/sport")
@RequiredArgsConstructor(onConstructor_ = @Autowired)
public class SportController {

    private final SportService sportService;

    @GetMapping("/get-all")
    public ResponseEntity<ApiResponse<List<SportDTO>>> getAll(){
        return new ResponseEntity<>(sportService.getAllSports(), HttpStatus.OK);
    }

    @GetMapping("/get-by-id/{id}")
    public ResponseEntity<ApiResponse<SportDTO>> getById(@PathVariable Long id){
        return new ResponseEntity<>(sportService.getById(id), HttpStatus.OK);
    }

    @PostMapping("/create")
    public ResponseEntity<ApiResponse<SportDTO>> create(@RequestBody SportDTO sportDTO){
        return new ResponseEntity<>(sportService.create(sportDTO), HttpStatus.OK);
    }

    @PutMapping("/update-by-id")
    public ResponseEntity<ApiResponse<SportDTO>> update(@RequestBody SportDTO sportDTO){
        System.out.println("Aqui va el request: "+sportDTO);

        return new ResponseEntity<>(sportService.update(sportDTO), HttpStatus.OK);
    }


}
