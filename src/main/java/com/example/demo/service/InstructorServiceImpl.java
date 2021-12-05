package com.example.demo.service;

import com.example.demo.model.Student;
import com.example.demo.model.StudentLesson;
import com.example.demo.model.InstructorCourse;
import com.example.demo.model.Term;
import com.example.demo.repository.InstructorRepository;
import com.example.demo.repository.StudentLessonRepository;
import com.example.demo.repository.StudentRepository;
import com.example.demo.repository.InstructorLessonRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;

import javax.transaction.Transactional;
import java.util.List;

@Service
@RequiredArgsConstructor
public class InstructorServiceImpl implements InstructorService {
    private final StudentLessonRepository studentLessonRepository;
    private final InstructorLessonRepository instructorLessonRepository;
    private final InstructorRepository instructorRepository;
    private final StudentRepository studentRepository;

    @Override
    @Transactional
    public StudentLesson setGrade(Long studentId, Long courseId, float grade) {
        Student student = studentRepository.findById(studentId)
                .orElseThrow(() -> new HttpClientErrorException(HttpStatus.NOT_FOUND));

        StudentLesson studentLesson = studentLessonRepository.findByStudent(student).stream().
                filter(sl -> sl.getInstructorCourse().getCourse().getId().equals(courseId)).findAny()
                .orElseThrow(() -> new HttpClientErrorException(HttpStatus.NOT_FOUND));

        return studentLessonRepository.save(studentLesson);
    }

    @Override
    public List<InstructorCourse> getLessons(Long teacherId) {
        return instructorRepository.findById(teacherId)
                .orElseThrow(() -> new HttpClientErrorException(HttpStatus.NOT_FOUND))
                .getInstructorCourse();
    }

    @Override
    public List<InstructorCourse> getTimeTable(Long teacherId, Term term) {
        return instructorLessonRepository.findByInstructorAndTerm(
                instructorRepository.findById(teacherId)
                        .orElseThrow(() -> new HttpClientErrorException(HttpStatus.NOT_FOUND)),
                term
        );
    }
}
