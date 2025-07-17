package com.elearningBackend;

import com.elearningBackend.enumeration.RoleName;
import com.elearningBackend.models.Role;
import com.elearningBackend.models.User;
import com.elearningBackend.services.RoleService;
import com.elearningBackend.services.UserService;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Set;

@SpringBootApplication
public class ElearningBackendApplication {

	public static void main(String[] args) {
		SpringApplication.run(ElearningBackendApplication.class, args);
	}

	/**
	 * Ce Bean s'exécute au démarrage de l'application pour initialiser les données de base.
	 * Cette version utilise uniquement les méthodes que vous avez fournies.
	 */


}