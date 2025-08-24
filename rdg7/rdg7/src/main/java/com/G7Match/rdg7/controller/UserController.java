package com.G7Match.rdg7.controller;


import com.G7Match.rdg7.Dto.ApiResponse;
import com.G7Match.rdg7.Dto.UsersDTO;
import com.G7Match.rdg7.model.UserModel;
import com.G7Match.rdg7.services.UserService;
import lombok.RequiredArgsConstructor;
import org.apache.catalina.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/user")
@RequiredArgsConstructor(onConstructor_ = @Autowired)
public class UserController {

    private final UserService userService;

    @GetMapping
    public ResponseEntity<ApiResponse<Object>> getById(Long id){

        try {
            UserModel userModel =  userService.findById(id);
            return ResponseEntity.ok(
                    new ApiResponse<>(true, "Usuario consultado exitosamente", userModel)
            );
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(
                    new ApiResponse<>(false, "Error al consultar el usuario: " + e.getMessage(), null)
            );
        }


    }

    @GetMapping("/all")
    public ResponseEntity<ApiResponse<Object>> getAllUsers() {
        try {
            List<UserModel> userModelList = userService.getAllClubs();
            return ResponseEntity.ok(
                    new ApiResponse<>(true, "Lista de usuarios consultada exitosamente", userModelList)
            );
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(
                    new ApiResponse<>(false, "Error al consultar los usuarios: " + e.getMessage(), null)
            );
        }
    }

    @PostMapping("/create")
    public ResponseEntity<ApiResponse<Object>> createUser(@RequestBody UsersDTO usersDTO) {
        try {
            System.out.println("Aqui va la pryueba");

            UserModel savedUser = userService.save(usersDTO);
            return ResponseEntity.ok(
                    new ApiResponse<>(true, "Usuario creado exitosamente", savedUser)
            );
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(
                    new ApiResponse<>(false, "Error al crear el usuario: " + e.getMessage(), null)
            );
        }
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<ApiResponse<Long>> deleteUser(@PathVariable Long id) {
        try {
            userService.deleteById(id);
            return ResponseEntity.ok(
                    new ApiResponse<>(true, "Usuario borrado exitosamente",id)
            );
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(
                    new ApiResponse<>(false, "Error al borrar el usuario: " + e.getMessage(), null)
            );
        }
    }

    @PutMapping("/update/{id}")
    public ResponseEntity<ApiResponse<Long>> updateUser(@PathVariable Long id, @RequestBody UsersDTO usersDTO) {

        try {
            userService.updateUser(id, usersDTO);
            return ResponseEntity.ok(
                    new ApiResponse<>(true, "Usuario actualizado exitosamente",id)
            );
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(
                    new ApiResponse<>(false, "Error al intentar actualizar el usuario: "
                            + e.getMessage(), null)
            );
        }
    }

}
