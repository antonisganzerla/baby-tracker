package com.sgztech;

import com.sgztech.domain.entity.User;
import com.sgztech.domain.entity.UserProfile;
import com.sgztech.domain.repository.UserProfileRepository;
import com.sgztech.domain.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.password.PasswordEncoder;

@SpringBootApplication
public class MainApplication {

    @Bean
    public CommandLineRunner init(
            @Autowired UserProfileRepository userProfileRepository,
            @Autowired UserRepository userRepository,
            @Autowired PasswordEncoder passwordEncoder
    ) {
        return args -> {
            UserProfile adminProfile = new UserProfile(UserProfile.ADMIN);
            userProfileRepository.save(adminProfile);
            userProfileRepository.save(new UserProfile(UserProfile.USER));

            User user = new User();
            user.setName("Admin");
            user.setEmail("admin@admin.com");
            user.setPassword(passwordEncoder.encode("admin1234"));
            user.setUserProfile(adminProfile);
            userRepository.save(user);
        };
    }


    public static void main(String[] args) {
        SpringApplication.run(MainApplication.class, args);
    }
}
