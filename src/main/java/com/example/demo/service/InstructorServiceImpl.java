package com.example.demo.service;

import com.example.demo.model.*;
import com.example.demo.repository.*;
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
    private final CourseRepository courseRepository;

    @Override
    @Transactional
    public StudentLesson setGrade(Term term, Long instructorId, String studentUsername,
                                  Long courseId, float grade) {
        var student = studentRepository.findByUsername(studentUsername)
                .orElseThrow(() -> new HttpClientErrorException(HttpStatus.NOT_FOUND));
        var id = InstructorCourse.TCId.builder()
                .courseId(courseId)
                .instructorId(instructorId)
                .termDate(term.getTermDate())
                .build();

        var ic = instructorLessonRepository.findById(id)
                .orElseThrow(() -> new HttpClientErrorException(HttpStatus.NOT_FOUND));

        StudentLesson studentLesson = ic.getStudentLesson().stream()
                .filter(sl -> sl.getStudent().getId().equals(student.getId()))
                .findAny()
                .orElseThrow(() -> new HttpClientErrorException(HttpStatus.NOT_FOUND));

        studentLesson.setGrade(grade);

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

    @Override
    public TimeTableDTO getTermTimeTableDTO(Term term, Long instructorId) {
        return TimeTableDTO.convertFromTimeTables(
                instructorLessonRepository.findByInstructorAndTerm(
                        instructorRepository.findById(instructorId)
                                .orElseThrow(() -> new HttpClientErrorException(HttpStatus.NOT_FOUND)),
                        term
                )
        );
    }

    @Override
    public InstructorCourse getInstructorCourse(Long instructorId, Term term, Long courseId) {
        return instructorLessonRepository.findByInstructorAndTermAndCourse(
                instructorRepository.findById(instructorId)
                        .orElseThrow(() -> new HttpClientErrorException(HttpStatus.NOT_FOUND)),
                term,
                courseRepository.findById(courseId)
                        .orElseThrow(() -> new HttpClientErrorException(HttpStatus.NOT_FOUND))
        ).orElseThrow(() -> new HttpClientErrorException(HttpStatus.NOT_FOUND));
    }
}