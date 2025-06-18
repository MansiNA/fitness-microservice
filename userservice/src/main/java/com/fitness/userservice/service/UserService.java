package com.fitness.userservice.service;

import com.fitness.userservice.dto.RegisterRequest;
import com.fitness.userservice.dto.UserResponse;
import com.fitness.userservice.model.User;
import com.fitness.userservice.repository.UserRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class UserService {

    @Autowired
    private UserRepository repository;
    public UserResponse register(RegisterRequest request) {

        if (repository.existsByEmail(request.getEmail())) {
            throw new RuntimeException("Email already exist");
        }


        User user = new User();
        user.setEmail(request.getEmail());
        user.setPassword(request.getPassword());
        user.setFirstName(request.getFirstName());
        user.setLastName(request.getLastName());

        User saveuser = repository.save(user);
        UserResponse userResponse = new UserResponse();
        userResponse.setId(saveuser.getId());
        userResponse.setEmail(saveuser.getEmail());
        userResponse.setPassword(saveuser.getPassword());
        userResponse.setFirstName(saveuser.getFirstName());
        userResponse.setLastName(saveuser.getLastName());
        userResponse.setCreatedAt(saveuser.getCreatedAt());
        userResponse.setUpdatedAt(saveuser.getUpdatedAt());

        return userResponse;
    }

    public UserResponse getUserProfile(String userId) {
        User user = repository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User Not Found"));
        UserResponse userResponse = new UserResponse();
        userResponse.setId(user.getId());
        userResponse.setEmail(user.getEmail());
        userResponse.setPassword(user.getPassword());
        userResponse.setFirstName(user.getFirstName());
        userResponse.setLastName(user.getLastName());
        userResponse.setCreatedAt(user.getCreatedAt());
        userResponse.setUpdatedAt(user.getUpdatedAt());

        return userResponse;
    }

    public Boolean existByUserId(String userId) {
        log.info("calling User Validation API for userId: {}", userId);
        return repository.existsById(userId);
    }
}
