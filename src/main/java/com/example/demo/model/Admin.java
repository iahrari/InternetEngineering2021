package com.example.demo.model;

import lombok.*;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import javax.persistence.Entity;
import java.util.Collection;
import java.util.Collections;

@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
@Entity(name = "admins")
@NoArgsConstructor
@Data
public class Admin extends User {

    @Builder
    public Admin(String username, String name, String family, String password, String nationalId) {
        super(username, name, family, password, nationalId);
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return Collections.singletonList(new SimpleGrantedAuthority(Roles.ROLE_ADMIN.getRole()));
    }
}
