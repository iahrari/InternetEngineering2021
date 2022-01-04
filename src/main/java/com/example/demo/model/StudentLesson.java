package com.example.demo.model;

import lombok.*;

import javax.persistence.*;
import javax.validation.constraints.Digits;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import java.io.Serializable;

@Entity
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(exclude = "student")
public class StudentLesson {
    @EmbeddedId
    private SLId id;

    @ManyToOne(fetch = FetchType.EAGER)
    @MapsId("tcId")
    private InstructorCourse instructorCourse;

    @ManyToOne
    @MapsId("studentId")
    private Student student;

    @Digits(integer = 2, fraction = 2, message = "نمره باید از ۰ تا ۲۰ باشد و نهایتا دو رقم اعشار باشد")
    @Max(value = 20, message = "نمره باید از ۰ تا ۲۰ باشد")
    @Min(value = 0, message = "نمره باید از ۰ تا ۲۰ باشد")
    @Builder.Default
    private Float grade = null;

    @Embeddable
    @Data
    @NoArgsConstructor
    @Builder
    @AllArgsConstructor
    public static class SLId implements Serializable {
        private InstructorCourse.TCId tcId;
        private Long studentId;
    }
}
