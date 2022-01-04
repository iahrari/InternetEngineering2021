package com.example.demo.model;

import lombok.*;
import org.hibernate.annotations.NaturalId;
import org.springframework.security.core.userdetails.UserDetails;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;

@Entity
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@Data
@AllArgsConstructor
@NoArgsConstructor
public abstract class User implements UserDetails {
    @Id
    @GeneratedValue
    private Long id;

    @NaturalId
    @NotBlank(message = "شناسه کاربر نمی‌تواند خالی باشد")
    @Column(unique = true)
    private String username;

    @NaturalId
    @NotBlank(message = "کد ملی نمی‌تواند خالی باشد")
    @Column(unique = true)
    private String nationalId;

    @NotBlank(message = "نام نمی‌تواند خالی باشد")
    private String name;
    @NotBlank(message = "نام خانوادگی نمی‌تواند خالی باشد")
    private String family;
    @NotBlank(message = "رمز عبور نمی‌تواند خالی باشد")
    private String password;

    public User(String username, String name, String family, String password, String nationalId) {
        this.username = username;
        this.name = name;
        this.family = family;
        this.password = password;
        this.nationalId = nationalId;
    }

    @Override
    public String getUsername() {
        return username;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }
}
