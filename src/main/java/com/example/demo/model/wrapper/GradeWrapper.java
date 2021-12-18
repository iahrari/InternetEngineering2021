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
    @Digits(integer = 2, fraction = 2)
    @Max(20)
    @Min(0)
    private Float grade = null;

}
