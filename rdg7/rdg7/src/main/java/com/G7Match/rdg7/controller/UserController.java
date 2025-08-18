package com.G7Match.rdg7.controller;


import com.G7Match.rdg7.model.UserModel;
import com.G7Match.rdg7.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/user")
public class UserController {

    private UserService userService;

    @GetMapping
    public UserModel getById(Long id){
        return userService.findById(id);
    }

}
