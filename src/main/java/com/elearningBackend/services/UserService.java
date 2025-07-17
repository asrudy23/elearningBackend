package com.elearningBackend.services;

import com.elearningBackend.mapper.MapUser;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.security.Principal;
import java.util.*;
import java.util.stream.Collectors;
import com.elearningBackend.dto.*;
import com.elearningBackend.exception.ResourceNotFoundException;
import com.elearningBackend.models.*;
import com.elearningBackend.enumeration.RoleName;
import com.elearningBackend.repositories.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class UserService {


    private final UserRepository userRepository;
    private final RoleRepository roleRepository;

    private final PasswordEncoder passwordEncoder;
    private final MapUser mapUser;
    private static final Logger logger = LoggerFactory.getLogger(UserService.class);

    public UserService(UserRepository userRepository, RoleRepository roleRepository, PasswordEncoder passwordEncoder, MapUser mapUser) {

        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
        this.mapUser = mapUser;
        this.passwordEncoder = passwordEncoder;
    }

    public void addUser(User user) {
        userRepository.save(user);
    }

    public Iterable<User> getUsers() {
        return userRepository.findAll();
    }

    public boolean adminExist() {
        Role admin = new Role();
        admin.setName(RoleName.ADMIN);
        for (User user : userRepository.findAll()) {
            for (Role role : user.getRoles()) {
                if (role.getName().equals(RoleName.ADMIN)) {
                    return true;
                }
            }
        }
        return false;
    }

    public List<UserResponse> findAllUsers(Principal principal) {

        User user = userRepository.findByEmail(principal.getName())
                .orElseThrow(() -> new ResourceNotFoundException("Vous n'êtes pas connecté"));
        List<RoleName> roles = user.getRoles().stream().map(Role::getName).toList();

        List<User> users = userRepository.findAll();
        return users.stream().map(mapUser::mapToUserResponse).toList();

    }


    public UserResponse findById(Long id) {
        User user = userRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("User not found"));
        return mapUser.mapToUserResponse(user);
    }

    public UserResponse findByEmail(String email) {
        User user = userRepository.findByEmail(email).orElseThrow(() -> new ResourceNotFoundException("User not found"));
        return mapUser.mapToUserResponse(user);
    }

    public List<UserResponse> findByRole(Long roleId) {
        Role role = roleRepository.findById(roleId).orElseThrow(() -> new ResourceNotFoundException("Role not found"));
        List<User> users = userRepository.findByRolesContains(role);
        return users.stream().map(mapUser::mapToUserResponse).toList();
    }

    @Transactional
    public UserResponse createUser(UserRequest request) {
        // Validation de l'email (plus simple)
        if (userRepository.existsByEmail(request.getEmail())) {
            throw new IllegalArgumentException("Email already in use by an active or not user");
        }

        User user = new User();
        user.setFirstName(request.getFirstName());
        user.setLastName(request.getLastName());
        user.setEmail(request.getEmail());
        user.setPhone(request.getPhone());
        Set<Role> assignedRoles = new HashSet<>();

        if (request.getRole() != null) {
            RoleName requestedRoleName = request.getRole();

            if (requestedRoleName.equals(RoleName.ADMIN)) {
                Role adminRole = roleRepository.findByName(RoleName.ADMIN)
                        .orElseThrow(() -> new ResourceNotFoundException("Role Admin not found"));
                //team.setCoordinator(user);
                //teamRepository.save(team);
                assignedRoles.add(adminRole);
            }

            if (requestedRoleName.equals(RoleName.STUDENT)) {

                Role studentRole = roleRepository.findByName(RoleName.STUDENT)
                        .orElseThrow(() -> new ResourceNotFoundException("Role Student not found"));
                //laboratory.setDirector(user);
                //laboratoryRepository.save(laboratory);
                assignedRoles.add(studentRole);
            }
            if (requestedRoleName.equals(RoleName.TEACHER)) {

                Role studentRole = roleRepository.findByName(RoleName.TEACHER)
                        .orElseThrow(() -> new ResourceNotFoundException("Role Student not found"));
                //laboratory.setDirector(user);
                //laboratoryRepository.save(laboratory);
                assignedRoles.add(studentRole);
            }
        }


    // Ajout du rôle MEMBER par défaut
    Role userRole = roleRepository.findByName(RoleName.USER)
            .orElseThrow(() -> new ResourceNotFoundException("Role User not found"));
        assignedRoles.add(userRole);
        user.setRoles(assignedRoles);

    User savedUser = userRepository.save(user);


        return mapUser.mapToUserResponse(savedUser);
    }


    @Transactional
    public UserResponse updateUser(Long userId, UserRequest request) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User not found or already deleted with ID: " + userId));


        // --- Mises à jour des champs simples ---
        if (request.getFirstName() != null && !user.getFirstName().equals(request.getFirstName())) {
            user.setFirstName(request.getFirstName());
        }
        if (request.getLastName() != null && !user.getLastName().equals(request.getLastName())) {
            user.setLastName(request.getLastName());
        }
        if (request.getPhone() != null && !user.getPhone().equals(request.getPhone())) {
            user.setPhone(request.getPhone());
        }

        // Mise à jour de l'email avec validation d'unicité
        if (request.getEmail() != null && !request.getEmail().equalsIgnoreCase(user.getEmail())) {
            Optional<User> existingUserOpt = userRepository.findByEmail(request.getEmail());
            if (existingUserOpt.isPresent() && !existingUserOpt.get().getId().equals(userId)) {
                throw new IllegalArgumentException("Email '" + request.getEmail() + "' is already in use by another active user.");
            }
            user.setEmail(request.getEmail());
        }
        User updatedUser = userRepository.save(user);
        return mapUser.mapToUserResponse(updatedUser);

    }
    public void deleteUser(Long userId) {
        User user = userRepository.findById(userId).orElseThrow(() -> new ResourceNotFoundException("User not found"));
        userRepository.delete(user);
    }

}
