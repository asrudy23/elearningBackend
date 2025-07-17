package com.elearningBackend.dto;

import jakarta.validation.groups.Default;

public interface ValidationGroups {
    interface Creation extends Default {}
    interface Update extends Default {}
}
