package com.example.demo.model;

import lombok.*;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import javax.persistence.*;
import java.util.*;

@EqualsAndHashCode(callSuper = true, exclude = "studentLessons")
@ToString(callSuper = true, exclude = "studentLessons")
@Entity
@Data
@NoArgsConstructor
public class Student extends User {

    @Builder
    public Student(String username, String name, String family, String password, String nationalId) {
        super(username, name, family, password, nationalId);
    }

    @OneToMany(fetch = FetchType.EAGER)
    private List<StudentLesson> studentLessons = new ArrayList<>();

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return Collections.singletonList(new SimpleGrantedAuthority(Roles.ROLE_STUDENT.getRole()));
    }
}
