package com.G7Match.rdg7.services;

import com.G7Match.rdg7.model.UserModel;
import com.G7Match.rdg7.repository.UserRepository;
import org.apache.catalina.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserService {

    private final UserRepository userRepository;

    @Autowired
    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public UserModel findById(Long id){
        return userRepository.findById(id).orElseThrow();
    }

    public List<UserModel> getAllClubs() {
        return userRepository.findAll();
    }

    public UserModel save(UserModel user) {
        return userRepository.save(user);
    }

    public void deleteById(Long id) {
        userRepository.deleteById(id);
    }

    public UserModel updateUser(Long id, UserModel userDetails) {
        return userRepository.findById(id).map(user -> {
            user.setEmail(userDetails.getEmail());
            user.setPassword(userDetails.getPassword());
            user.setPhone(userDetails.getPhone());
            return userRepository.save(user);
        }).orElseThrow(() -> new RuntimeException("User not found with id " + id));
    }
}
