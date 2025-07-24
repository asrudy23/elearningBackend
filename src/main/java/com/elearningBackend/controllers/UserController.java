package com.elearningBackend.controllers;
import com.elearningBackend.dto.*;
import com.elearningBackend.enumeration.Role;
import com.elearningBackend.services.UserService;

import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;


import java.util.List;

@RestController

public class UserController {
    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }



    @PostMapping("api/users/createUser")
    public ResponseEntity<UserResponse> createUser(@Validated @RequestBody UserRequest userRequest) {

        UserResponse userResponse = userService.createUser(userRequest);
        return ResponseEntity.ok(userResponse);
    }
    @GetMapping("api/admin/users")
    public ResponseEntity<List<UserResponse>> getUsers() {
        return ResponseEntity.ok(userService.getAllUsers());
    }

    @GetMapping("api/admin/users/id/{id}")
    public ResponseEntity<UserResponse> getUserById(@PathVariable Long id) {

        return ResponseEntity.ok(userService.getUserById(id));
    }
    @GetMapping("api/admin/users/role/{role}")
    public ResponseEntity<List<UserResponse>> getUserByRole(@PathVariable Role role) {
        return ResponseEntity.ok(userService.getUsersByRole(role));

    }

    @DeleteMapping("api/admin/users/id/{id}")
    public ResponseEntity<Void> deleteUserById(@PathVariable Long id) {
        userService.deleteUserById(id);
        return ResponseEntity.ok().build();
    }


}


