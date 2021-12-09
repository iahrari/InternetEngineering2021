package com.example.demo.service;

import com.example.demo.model.*;

import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;

public interface AdminService {
    void newInstructor(Instructor instructor);
    void newAdmin(Admin admin);
    void newStudent(Student student);
    void saveCourse(Course course);
    void assignInstructor(
            String instructorUsername, Long courseId,
            TimeTable timeTable, Term term,
            Date examDate
    );

    void saveTerm(Term term);

    List<Term> getAllTerms();
    List<Course> getAllCourses();
    Term getTerm();
    List<Instructor> getAllInstructors();
    List<Student> getAllStudents();
    List<Admin> getAllAdmins();
    List<InstructorCourse> getTermInstructorCourse(Term term);
    List<InstructorCourse> getInstructorSpecialCourse(Term term, Long courseId);
}
