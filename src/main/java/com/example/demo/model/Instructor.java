package com.example.demo.model;


import lombok.*;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import javax.persistence.*;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

@Entity(name = "instructors")
@NoArgsConstructor
@Data
@ToString(callSuper = true, exclude = "instructorCourse")
@EqualsAndHashCode(exclude = "instructorCourse", callSuper = true)
public class Instructor extends User {

    @OneToMany(fetch = FetchType.LAZY)
    private List<InstructorCourse> instructorCourse;

    @Builder
    public Instructor(String username, String name, String family, String password, String nationalId) {
        super(username, name, family, password, nationalId);
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return Collections.singletonList(new SimpleGrantedAuthority(Roles.ROLE_INSTRUCTOR.getRole()));
    }

}
