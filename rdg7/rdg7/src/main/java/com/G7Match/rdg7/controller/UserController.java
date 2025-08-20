package com.G7Match.rdg7.controller;


import com.G7Match.rdg7.model.UserModel;
import com.G7Match.rdg7.services.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/user")
@RequiredArgsConstructor(onConstructor_ = @Autowired)
public class UserController {

    private final UserService userService;

    @Autowired
    public UserController(UserService userService){ this.userService = userService; }

    @GetMapping
    public UserModel getById(Long id){
        return userService.findById(id);
    }

    @GetMapping("/all")
    public List<UserModel> getAllUsers() {
        return userService.getAllClubs();
    }

    @PostMapping("/create")
    public UserModel createUser(UserModel userModel) {
        return userService.save(userModel);
    }

    @DeleteMapping("/delete/{id}")
    public void deleteUser(@PathVariable Long id) {
        userService.deleteById(id);
    }

    @PutMapping("/update/{id}")
    public UserModel updateUser(@PathVariable Long id,@RequestBody UserModel userModel) {
        return userService.updateUser(id, userModel);
    }

}
