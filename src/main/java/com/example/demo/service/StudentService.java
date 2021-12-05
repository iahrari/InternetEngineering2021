package com.example.demo.service;

import com.example.demo.model.StudentLesson;
import com.example.demo.model.InstructorCourse;
import com.example.demo.model.Term;

import java.util.List;

public interface StudentService {
    StudentLesson selectUnit(InstructorCourse instructorCourse, Long studentId);
    List<StudentLesson> getLessons(Long studentId, Term term);
}
