package com.G7Match.rdg7.services;

import com.G7Match.rdg7.model.UserModel;
import com.G7Match.rdg7.repository.UserRepository;
import org.apache.catalina.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserService {

    private final UserRepository userRepository;

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public UserModel findById(Long id){
        return userRepository.findById(id).orElseThrow();
    }
}
