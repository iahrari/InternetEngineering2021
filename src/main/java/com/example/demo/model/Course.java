package com.example.demo.model;

import lombok.*;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import java.util.List;

@Data
@Builder
@ToString(exclude = "instructorCourse")
@AllArgsConstructor
@NoArgsConstructor
@Entity
@EqualsAndHashCode(exclude = "instructorCourse")
public class Course {
    @Id
    @GeneratedValue
    private Long id;

    @NotBlank(message = "نام درس نمی‌تواند خالی باشد")
    private String name;

    @OneToMany(fetch = FetchType.LAZY)
    private List<InstructorCourse> instructorCourse;

}
