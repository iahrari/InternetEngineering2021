package com.example.demo.service;

import com.example.demo.model.*;

import java.time.LocalDateTime;
import java.util.List;

public interface AdminService {
    void newInstructor(Instructor instructor);
    void newAdmin(Admin admin);
    void newStudent(Student student);
    void saveCourse(Course course);
    void assignInstructor(
            String instructorUsername, Long courseId,
            TimeTable timeTable, Term term,
            LocalDateTime examDate
    );

    void saveTerm(Term term);

    List<Term> getAllTerms();
    Term getTerm();
}
