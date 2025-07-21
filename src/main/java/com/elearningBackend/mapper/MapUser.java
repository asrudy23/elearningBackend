package com.elearningBackend.mapper;

import com.elearningBackend.dto.UserResponse;
import com.elearningBackend.models.User;
import org.springframework.stereotype.Component;

@Component
public class MapUser {
    public  UserResponse mapUserToResponse(User user){
        if(user==null)
        {   return null;}

        return  new UserResponse (
                user.getId(),
                user.getFirstName(),
                user.getLastName(),
                user.getEmail(),
                user.getPhone(),
                user.getRole(),
                user.getHobbies(),
                user.isActive()
                );

    }
}
