package com.example.demo.model;

import com.example.demo.model.validation.IsEven;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;
import java.util.Date;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class AssignInstructorDTO {
    @NotBlank
    @NotNull
    private String instructorId;

    @Min(8)
    @Max(18)
    @IsEven
    private Short saturday;

    @Min(8)
    @Max(18)
    @IsEven
    private Short sunday;

    @Min(8)
    @Max(18)
    @IsEven
    private Short monday;

    @Min(8)
    @Max(18)
    @IsEven
    private Short tuesday;

    @Min(8)
    @Max(18)
    @IsEven
    private Short wednesday;

    @Min(8)
    @Max(18)
    @IsEven
    private Short thursday;

    @DateTimeFormat(pattern = "yyyy-MM-dd'T'HH:mm")
    private Date examDate;

    public TimeTable getTimeTable(){
        return TimeTable.builder()
                .saturday(saturday)
                .sunday(sunday)
                .monday(monday)
                .tuesday(tuesday)
                .thursday(tuesday)
                .wednesday(wednesday)
                .thursday(thursday)
                .build();
    }
}
