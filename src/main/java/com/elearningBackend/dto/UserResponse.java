package com.elearningBackend.dto;

import com.elearningBackend.enumeration.Hobby;
import com.elearningBackend.enumeration.Role;

import java.util.List;

public record UserResponse(
        Long id,
        String firstName,
        String lastName,
        String email,
        String phone,
        Role role,
        List<Hobby> hobbies,
        boolean isActive) {}
