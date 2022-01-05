package com.example.demo.bootstrap;

import com.example.demo.model.Admin;
import com.example.demo.model.Instructor;
import com.example.demo.model.Student;
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
            userRepository.save(Admin.builder()
                    .name("مصطفی")
                    .username("admin")
                    .family("نظری")
                    .nationalId("9812398312")
                    .password(passwordEncoder.encode("admin"))
                    .build()
            );

            userRepository.save(Instructor.builder()
                    .family("عبدالهی")
                    .name("علی")
                    .nationalId("034234")
                    .username("ins")
                    .password(passwordEncoder.encode("ins"))
                    .build()
            );

            userRepository.save(Instructor.builder()
                    .family("ولی‌پور مطلق")
                    .name("امیر مهدی")
                    .nationalId("6548794")
                    .username("ins2")
                    .password(passwordEncoder.encode("ins2"))
                    .build()
            );

            userRepository.save(Student.builder()
                    .family("احراری")
                    .name("ایمان")
                    .nationalId("98756416")
                    .username("stu2")
                    .password(passwordEncoder.encode("stu2"))
                    .build()
            );

            userRepository.save(Student.builder()
                    .family("ضابطی")
                    .name("محمد سهیل")
                    .nationalId("894564698")
                    .username("stu")
                    .password(passwordEncoder.encode("stu"))
                    .build()
            );
        };
    }
}
