package com.elearningBackend.services;

import com.elearningBackend.enumeration.RoleName;
import com.elearningBackend.models.Role;
import com.elearningBackend.repositories.RoleRepository;
import org.springframework.stereotype.Service;

@Service
public class RoleService {

    private final RoleRepository roleRepository;

    public RoleService(RoleRepository roleRepository) {
        this.roleRepository = roleRepository;
    }

    public void saveRole(Role role) {
        roleRepository.save(role);
    }

    public long roleNumber (){
        return roleRepository.count();
    }

    public Role findRoleByName(RoleName name) {
        if (roleRepository.findByName(name).isPresent()) {
            return roleRepository.findByName(name).get();
        }
        else{
            return null;
        }
    }
}
