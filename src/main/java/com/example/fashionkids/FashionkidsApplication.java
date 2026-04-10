package com.example.fashionkids;

import com.example.fashionkids.entity.User;
import com.example.fashionkids.repository.UserRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.password.PasswordEncoder;

@SpringBootApplication
public class FashionkidsApplication {

    public static void main(String[] args) {
        SpringApplication.run(FashionkidsApplication.class, args);
    }
    //mvn spring-boot:run
    @Bean
    public CommandLineRunner createAdmin(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        return args -> {
            String adminUsername = "admin";
            if (userRepository.findByUsername(adminUsername) == null) {
                User admin = new User();
                admin.setUsername(adminUsername);
                admin.setPassword(passwordEncoder.encode("123"));
                admin.setRole("ROLE_ADMIN");
                admin.setEmail("admin@example.com");
                admin.setFullName("Administrator");
                userRepository.save(admin);
                System.out.println("Created admin account: admin / 123");
            } else {
                System.out.println("Admin account already exists: admin");
            }
        };
    }
}
