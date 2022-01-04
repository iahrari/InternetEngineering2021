package com.example.demo.config;

import com.example.demo.model.Roles;
import com.example.demo.repository.UserRepository;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class SecurityConfig {
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
                    .antMatchers("/CSS/**")
                        .permitAll()
                    .antMatchers("/fonts/**")
                        .permitAll()
                    .anyRequest().authenticated()
                .and().formLogin()
                    .successHandler(new LoginAuthenticationSuccessHandler())
                .and().build();
    }

    @Bean
    public UserDetailsService userDetailsService(UserRepository userRepository){
        return u -> userRepository.findByUsername(u)
                .orElseThrow(() -> new UsernameNotFoundException(u));
    }
}
