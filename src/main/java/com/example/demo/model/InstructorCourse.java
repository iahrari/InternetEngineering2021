package com.example.demo.model;

import lombok.*;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.*;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.Set;

@Entity
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Data
@EqualsAndHashCode(exclude = {"timeTable", "instructor", "course"})
public class InstructorCourse {
    @EmbeddedId
    private TCId id;

    @ManyToOne
    @MapsId("termDate")
    private Term term;

    @ManyToOne(fetch = FetchType.EAGER)
    @MapsId("instructorId")
    private Instructor instructor;

    @ManyToOne(fetch = FetchType.EAGER)
    @MapsId("courseId")
    private Course course;

    @OneToMany(fetch = FetchType.EAGER)
    private Set<StudentLesson> studentLesson;

    @DateTimeFormat(pattern = "yyyy-MM-dd'T'HH:mm")
    private Date examDate;

    @Embedded
    private TimeTable timeTable;

    @Embeddable
    @Data
    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    public static class TCId implements Serializable {
        private Long instructorId;
        private Long courseId;
        private Term.TermDate termDate;
    }
}