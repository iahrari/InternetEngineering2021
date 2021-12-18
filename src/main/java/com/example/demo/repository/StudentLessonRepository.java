package com.example.demo.repository;

import com.example.demo.model.Student;
import com.example.demo.model.StudentLesson;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface StudentLessonRepository
        extends JpaRepository<StudentLesson, StudentLesson.SLId> {
    List<StudentLesson> findByStudent(Student student);
}