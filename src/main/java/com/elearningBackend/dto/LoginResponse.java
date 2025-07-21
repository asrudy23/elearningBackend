package com.elearningBackend.dto;

public record LoginResponse(
        String message,
        UserResponse user) {
}
