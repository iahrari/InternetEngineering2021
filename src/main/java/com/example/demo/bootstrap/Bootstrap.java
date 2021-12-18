package com.example.demo.bootstrap;

import com.example.demo.model.Admin;
import com.example.demo.model.Term;
import com.example.demo.repository.TermRepository;
import com.example.demo.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.Calendar;
import java.util.Date;

@Component
@RequiredArgsConstructor
public class Bootstrap {
    private final UserRepository userRepository;
    private final TermRepository termRepository;
    private final PasswordEncoder passwordEncoder;

    @Bean
    public CommandLineRunner run(){
        return args -> {
            Admin admin = Admin.builder()
                    .name("admin")
                    .username("admin")
                    .family("admin")
                    .nationalId("9812398312")
                    .password(passwordEncoder.encode("admin"))
                    .build();
            userRepository.save(admin);

            Calendar endEnroll = Calendar.getInstance();
            endEnroll.add(Calendar.MINUTE, 10);
            Calendar startExam = Calendar.getInstance();
            startExam.add(Calendar.MINUTE, 15);

            Calendar endExam = Calendar.getInstance();
            endExam.add(Calendar.MONTH, 3);
            endExam.add(Calendar.WEEK_OF_MONTH, 2);

            Term term = Term.builder()
                    .enrollStart(new Date())
                    .enrollEnd(endEnroll.getTime())
                    .examStart(startExam.getTime())
                    .examEnd(endExam.getTime())
                    .termDate(Term.TermDate.builder().term(0).year("1400").build())
                    .build();

            termRepository.save(term);
        };

    }


}
