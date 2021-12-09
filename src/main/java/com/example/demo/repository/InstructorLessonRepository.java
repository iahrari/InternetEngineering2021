package com.example.demo.repository;

import com.example.demo.model.Course;
import com.example.demo.model.Instructor;
import com.example.demo.model.InstructorCourse;
import com.example.demo.model.Term;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface InstructorLessonRepository
        extends JpaRepository<InstructorCourse, InstructorCourse.TCId> {
    List<InstructorCourse> findByInstructor(Instructor instructor);
    List<InstructorCourse> findByInstructorAndTerm(Instructor instructor, Term term);
    List<InstructorCourse> findByTerm(Term term);
    List<InstructorCourse> findAllByCourseAndTerm(Course course, Term term);
}
