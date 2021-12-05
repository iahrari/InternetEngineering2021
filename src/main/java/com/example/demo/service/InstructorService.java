package com.example.demo.service;

import com.example.demo.model.StudentLesson;
import com.example.demo.model.InstructorCourse;
import com.example.demo.model.Term;

import java.util.List;

public interface InstructorService {
    StudentLesson setGrade(Long studentId, Long courseId, float grade);
    List<InstructorCourse> getLessons(Long teacherId);
    List<InstructorCourse> getTimeTable(Long teacherId, Term term);
}
