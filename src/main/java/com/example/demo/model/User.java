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
    @NotBlank
    @Column(unique = true)
    private String username;

    @NaturalId
    @NotBlank
    @Column(unique = true)
    private String nationalId;

    @NotBlank
    private String name;
    @NotBlank
    private String family;
    @NotBlank
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
