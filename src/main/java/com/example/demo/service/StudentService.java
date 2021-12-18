package com.example.demo.service;

import com.example.demo.model.*;

import java.util.List;

public interface StudentService {
    StudentLesson selectUnit(InstructorCourse.TCId instructorCourse, Student student);
    List<StudentLesson> getLessons(Long studentId, Term term);
    List<InstructorCourse> getTermInstructorCourse(Term term, Student student);
    TimeTableDTO getTermTimeTableDTO(Term term, Student student);
}
