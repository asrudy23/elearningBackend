package com.elearningBackend.repositories;

import com.elearningBackend.enumeration.Role;
import com.elearningBackend.models.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByEmail(String email);
    boolean existsByEmail(String email);
    Optional<User> findById(Long id);
    List<User> findByRole(Role role);
}
