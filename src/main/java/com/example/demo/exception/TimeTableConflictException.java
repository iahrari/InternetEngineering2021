package com.example.demo.exception;


import com.example.demo.model.InstructorCourse;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.RequiredArgsConstructor;

@EqualsAndHashCode(callSuper = false)
@Builder
@Data
@RequiredArgsConstructor
public class TimeTableConflictException extends RuntimeException{
    private final InstructorCourse have;
    private final InstructorCourse wanted;
}
