package com.elearningBackend.dto;

import com.elearningBackend.enumeration.Hobby;
import com.elearningBackend.enumeration.Role;
import jakarta.validation.constraints.*;
import lombok.Getter;
import lombok.Setter;

import java.util.List;
public record UserRequest(
        @NotBlank(groups = ValidationGroups.Creation.class, message = "First name is required")
        String firstName,

        @NotBlank(groups = ValidationGroups.Creation.class, message = "Last name is required")
        String lastName,

        @Email(groups = {ValidationGroups.Creation.class, ValidationGroups.Update.class}, message = "Email should be valid")
        @NotBlank(groups = ValidationGroups.Creation.class, message = "Email is required")
        String email,

        @Pattern(regexp = "^\\+?[0-9\\s-]{10,}$",
        groups = {ValidationGroups.Creation.class, ValidationGroups.Update.class},
        message = "Invalid phone number format")
        @NotBlank(groups = ValidationGroups.Creation.class, message = "Phone is required")
        String phone,

        @NotBlank(groups=ValidationGroups.Creation.class,message = "Password is required")
        String password,

        @NotBlank(groups=ValidationGroups.Creation.class,message = "Role is required")
        Role role,

        List<Hobby> hobbies

){}

