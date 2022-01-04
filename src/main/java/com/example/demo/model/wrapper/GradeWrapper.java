package com.example.demo.model.wrapper;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.Digits;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class GradeWrapper {
    @Digits(integer = 2, fraction = 2, message = "نمره باید از ۰ تا ۲۰ باشد و نهایتا دو رقم اعشار باشد")
    @Max(value = 20, message = "نمره باید از ۰ تا ۲۰ باشد")
    @Min(value = 0, message = "نمره باید از ۰ تا ۲۰ باشد")
    private Float grade = null;

}
