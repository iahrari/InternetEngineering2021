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
import java.util.Date;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class AssignInstructorDTO {
    @NotBlank(message = "شناسه کاربری استاد نمی‌تواند خالی باشد")
    private String instructorId;

    @Min(value = 8, message = "ساعت کلاس باید از ۸ تا ۲۰ و زوج باشد")
    @Max(value = 18, message = "ساعت کلاس باید از ۸ تا ۲۰ و زوج باشد")
    @IsEven(message = "ساعت کلاس باید از ۸ تا ۲۰ و زوج باشد")
    private Short saturday;

    @Min(value = 8, message = "ساعت کلاس باید از ۸ تا ۲۰ و زوج باشد")
    @Max(value = 18, message = "ساعت کلاس باید از ۸ تا ۲۰ و زوج باشد")
    @IsEven(message = "ساعت کلاس باید از ۸ تا ۲۰ و زوج باشد")
    private Short sunday;

    @Min(value = 8, message = "ساعت کلاس باید از ۸ تا ۲۰ و زوج باشد")
    @Max(value = 18, message = "ساعت کلاس باید از ۸ تا ۲۰ و زوج باشد")
    @IsEven(message = "ساعت کلاس باید از ۸ تا ۲۰ و زوج باشد")
    private Short monday;

    @Min(value = 8, message = "ساعت کلاس باید از ۸ تا ۲۰ و زوج باشد")
    @Max(value = 18, message = "ساعت کلاس باید از ۸ تا ۲۰ و زوج باشد")
    @IsEven(message = "ساعت کلاس باید از ۸ تا ۲۰ و زوج باشد")
    private Short tuesday;

    @Min(value = 8, message = "ساعت کلاس باید از ۸ تا ۲۰ و زوج باشد")
    @Max(value = 18, message = "ساعت کلاس باید از ۸ تا ۲۰ و زوج باشد")
    @IsEven(message = "ساعت کلاس باید از ۸ تا ۲۰ و زوج باشد")
    private Short wednesday;

    @Min(value = 8, message = "ساعت کلاس باید از ۸ تا ۲۰ و زوج باشد")
    @Max(value = 18, message = "ساعت کلاس باید از ۸ تا ۲۰ و زوج باشد")
    @IsEven(message = "ساعت کلاس باید از ۸ تا ۲۰ و زوج باشد")
    private Short thursday;

    @DateTimeFormat(pattern = "yyyy-MM-dd'T'HH:mm")
    @NotNull(message = "تاریخ امتحان نمی‌تواند خالی باشد.")
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
