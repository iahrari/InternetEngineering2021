package com.example.demo.config;

import com.example.demo.model.Roles;
import com.example.demo.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.web.server.ServerHttpSecurity;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.server.SecurityWebFilterChain;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {
    private final UserRepository userRepository;

    @Bean
    public PasswordEncoder passwordEncoder(){
        return new BCryptPasswordEncoder();
    }

    @Bean
    public SecurityFilterChain configure(HttpSecurity http) throws Exception {
        return http.csrf().disable()
                .authorizeRequests()
                    .antMatchers("/admin/**").hasAuthority(Roles.ROLE_ADMIN.getRole())
                    .antMatchers("/student/**")
                        .hasAnyAuthority(Roles.ROLE_STUDENT.getRole())
                    .antMatchers("/instructor/**")
                        .hasAnyAuthority(Roles.ROLE_INSTRUCTOR.getRole())
                    .anyRequest().authenticated()
                .and().formLogin()
                    .successHandler(new LoginAuthenticationSuccessHandler())
                .and().build();
    }

    @Bean
    public UserDetailsService userDetailsService(){
        return u -> userRepository.findByUsername(u)
                .orElseThrow(() -> new UsernameNotFoundException(u));
    }
}
