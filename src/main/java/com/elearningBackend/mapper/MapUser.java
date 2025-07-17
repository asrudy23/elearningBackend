package com.elearningBackend.mapper;

import com.elearningBackend.dto.UserDTO;
import com.elearningBackend.dto.UserResponse;
import com.elearningBackend.models.User;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

@Component
public class MapUser {

    public UserDTO mapUserToDto(User user) {
        if (user == null) {
            return null;
        }

        return new UserDTO(
                user.getId(),
                user.getFirstName(),
                user.getLastName(),
                user.getEmail(),
                user.getPhone()
        );
    }
    public UserResponse mapToUserResponse(User user) {
        if (user == null) {
            return null;
        }

        return new UserResponse(
                user.getId(),
                user.getFirstName(),
                user.getLastName(),
                user.getEmail(),
                user.getPhone(),
                user.getRoles().stream().map(role -> role.getName().name()).toList()
        );
    }
}
