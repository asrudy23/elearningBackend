package com.elearningBackend.controllers;
import com.elearningBackend.dto.*;
import com.elearningBackend.models.User;
import com.elearningBackend.services.UserService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;


import java.security.Principal;
import java.util.List;
import java.util.Optional;

@RestController

public class UserController {
    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }



    @GetMapping("/api/users")
    public ResponseEntity<List<UserResponse>> getAllUsers(Principal principal) {
        return ResponseEntity.ok(userService.findAllUsers(principal));
    }

    @GetMapping("/api/users/{id}")
    public ResponseEntity<UserResponse> getById(@PathVariable Long id) {
        UserResponse user = userService.findById(id);
        return ResponseEntity.ok(user);
    }

    @GetMapping("/api/users/role/{roleId}")
    public ResponseEntity<List<UserResponse>> getByRole(@PathVariable Long roleId) {
        List<UserResponse> users = userService.findByRole(roleId);
        return ResponseEntity.ok(users);
    }

    @PostMapping("/api/users")
    public ResponseEntity<UserResponse> create(@RequestBody @Validated(ValidationGroups.Creation.class) UserRequest userRequest) {
        UserResponse user = userService.createUser(userRequest);
        return ResponseEntity.ok(user);
    }

    @PutMapping("/api/users/{id}")
    public ResponseEntity<UserResponse> updateUser( @PathVariable Long id, @Validated(ValidationGroups.Update.class) @RequestBody UserRequest userRequest) {
        UserResponse updatedUser = userService.updateUser(id, userRequest);
        return ResponseEntity.ok(updatedUser);
    }
    @GetMapping("/api/users/me")
    public ResponseEntity<UserResponse> getMe(Principal principal) {
        UserResponse user = userService.findByEmail(principal.getName());
        return ResponseEntity.ok(user);
    }

    @DeleteMapping("/api/users/{id}")
    public ResponseEntity<String> delete(@PathVariable Long id) {
        userService.deleteUser(id);
        return ResponseEntity.ok("Delete Successfully");
    }


}


