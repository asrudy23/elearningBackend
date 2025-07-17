package com.elearningBackend.dto;

import com.elearningBackend.enumeration.RoleName;
import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserRequest {
    @NotBlank(groups = ValidationGroups.Creation.class, message = "First name is required")
    private String firstName;

    @NotBlank(groups = ValidationGroups.Creation.class, message = "Last name is required")
    private String lastName;

    @Email(groups = {ValidationGroups.Creation.class, ValidationGroups.Update.class}, message = "Email should be valid")
    @NotBlank(groups = ValidationGroups.Creation.class, message = "Email is required")
    private String email;

    @Pattern(regexp = "^\\+?[0-9\\s-]{10,}$",
            groups = {ValidationGroups.Creation.class, ValidationGroups.Update.class},
            message = "Invalid phone number format")
    @NotBlank(groups = ValidationGroups.Creation.class, message = "Phone is required")
    private String phone;

    private RoleName role;

}

