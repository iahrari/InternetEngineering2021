package com.example.demo.exception;


import com.example.demo.model.InstructorCourse;
import lombok.Builder;
import lombok.RequiredArgsConstructor;

@Builder
@RequiredArgsConstructor
public class TimeTableConflictException extends RuntimeException{
    private final InstructorCourse have;
    private final InstructorCourse wanted;
}
