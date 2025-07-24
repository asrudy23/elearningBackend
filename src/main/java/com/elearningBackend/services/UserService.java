package com.elearningBackend.services;

import com.elearningBackend.dto.UserRequest;
import com.elearningBackend.dto.UserResponse;
import com.elearningBackend.enumeration.Role;
import com.elearningBackend.mapper.MapUser;
import com.elearningBackend.models.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.elearningBackend.repositories.*;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;


@Service
public class UserService {


    private final UserRepository userRepository;
    private final MapUser mapUser;
    private final PasswordEncoder passwordEncoder;
    private static final Logger logger = LoggerFactory.getLogger(UserService.class);

    public UserService(UserRepository userRepository, MapUser mapUser, PasswordEncoder passwordEncoder) {

        this.userRepository = userRepository;
        this.mapUser = mapUser;
        this.passwordEncoder = passwordEncoder;
    }

    public UserResponse createUser(UserRequest userRequest) {


        if (userRepository.existsByEmail(userRequest.email())) {
            throw new RuntimeException("Email address already in use");
        }
            User user = new User();

            user.setEmail(userRequest.email());
            user.setFirstName(userRequest.firstName());
            user.setLastName(userRequest.lastName());
            user.setPassword(userRequest.password());
            user.setRole(userRequest.role());
            user.setHobbies(userRequest.hobbies());
            user.setPhone(userRequest.phone());
            user.setActive(false);
            user.setPassword(passwordEncoder.encode(userRequest.password()));

            User userSaved = userRepository.save(user);

            return mapUser.mapUserToResponse(userSaved);



    }

        public UserResponse getUserById (Long id){
            User user = userRepository.findById(id).orElseThrow(()->new RuntimeException("User not found"));
            return mapUser.mapUserToResponse(user);
        }

        public List<UserResponse> getAllUsers () {
            List<User> users = userRepository.findAll();
            List<UserResponse> userResponses = users.stream().map(user -> mapUser.mapUserToResponse(user)).collect(Collectors.toList());
            return userResponses;
        }

        public List<UserResponse> getUsersByRole (Role role) {
        List<User> users = userRepository.findByRole(role);
        List<UserResponse> userResponses = users.stream().map(user -> mapUser.mapUserToResponse(user)).collect(Collectors.toList());
        return userResponses;
        }
        public void deleteUserById(Long id){
        userRepository.deleteById(id);
        }
    }
