package com.example.demo.service;

import com.example.demo.model.*;

import java.util.List;

public interface InstructorService {
    StudentLesson setGrade(Term term, Long instructorId, String studentUsername, Long courseId, float grade);
    List<InstructorCourse> getLessons(Long teacherId);
    List<InstructorCourse> getTimeTable(Long teacherId, Term term);
    TimeTableDTO getTermTimeTableDTO(Term term, Long instructorId);
    InstructorCourse getInstructorCourse(Long instructorId, Term term, Long courseId);
}
