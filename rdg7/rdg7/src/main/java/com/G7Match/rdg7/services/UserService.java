package com.G7Match.rdg7.services;

import com.G7Match.rdg7.Dto.UsersDTO;
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

    public UserModel save(UsersDTO dto) {
        UserModel user = new UserModel();

        user.setIdentification(dto.getIdentification());
        user.setPasswordHash(dto.getPassword());
        user.setEmail(dto.getEmail());
        user.setFirstName(dto.getFirstName());
        user.setSecondName(dto.getSecondName());
        user.setLastName(dto.getLastName());
        user.setSecondLastName(dto.getSecondLastName());
        user.setPhone(dto.getPhone());

        return userRepository.save(user);
    }
    public void deleteById(Long id) {
        userRepository.deleteById(id);
    }

    public UserModel updateUser(Long id, UsersDTO usersDTO) {
        return userRepository.findById(id).map(user -> {
            user.setEmail(usersDTO.getEmail());
            user.setPasswordHash(usersDTO.getPassword());
            user.setPhone(usersDTO.getPhone());
            return userRepository.save(user);
        }).orElseThrow(() -> new RuntimeException("User not found with id " + id));
    }
}
