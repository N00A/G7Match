package com.g7match.rdg7.services;

import com.g7match.rdg7.dto.UsersDTO;
import com.g7match.rdg7.model.UserModel;
import com.g7match.rdg7.repository.UserRepository;
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

    public UserModel findByIdModel(Long id) {
        return userRepository.findById(id).orElseThrow();
    }

    public UserModel findById(Long id) {
        return userRepository.findById(id).orElseThrow();
    }

    public List<UserModel> getAllUsers() {
        return userRepository.findAll();
    }

    public UserModel save(UsersDTO dto) {
        UserModel user = new UserModel();
        mapDTOToUser(dto, user);
        return userRepository.save(user);
    }

    public void deleteById(Long id) {
        userRepository.deleteById(id);
    }

    public UserModel updateUser(Long id, UsersDTO dto) {
        return userRepository.findById(id).map(user -> {
            mapDTOToUser(dto, user);
            return userRepository.save(user);
        }).orElseThrow(() -> new RuntimeException("User not found with id " + id));
    }

    private void mapDTOToUser(UsersDTO dto, UserModel user) {
        user.setIdentification(dto.getIdentification());
        user.setPasswordHash(dto.getPassword());
        user.setEmail(dto.getEmail());
        user.setFirstName(dto.getFirstName());
        user.setSecondName(dto.getSecondName());
        user.setLastName(dto.getLastName());
        user.setSecondLastName(dto.getSecondLastName());
        user.setPhone(dto.getPhone());
        user.setIsActive(dto.getIsActive());
    }
}