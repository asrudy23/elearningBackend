package com.elearningBackend.dto;

import java.util.List;

public class JwtResponse {
    private String token;
    private List<String> roles;

    public JwtResponse(String token, List<String> roles) {
        this.token = token;
        this.roles = roles;
    }

    // Getters et Setters
    public String getToken() {
        return token;
    }

    public List<String> getRoles() {
        return roles;
    }
}