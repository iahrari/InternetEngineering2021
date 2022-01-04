package com.example.demo.bootstrap;

import com.example.demo.model.Admin;
import com.example.demo.repository.TermRepository;
import com.example.demo.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;


@Component
@RequiredArgsConstructor
public class Bootstrap {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Bean
    public CommandLineRunner run(){
        return args -> {
            Admin admin = Admin.builder()
                    .name("مصطفی")
                    .username("admin")
                    .family("نظری")
                    .nationalId("9812398312")
                    .password(passwordEncoder.encode("admin"))
                    .build();
            userRepository.save(admin);
        };

    }


}
