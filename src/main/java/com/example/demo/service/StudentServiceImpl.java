package com.example.demo.service;

import com.example.demo.exception.TimeTableConflictException;
import com.example.demo.model.Student;
import com.example.demo.model.StudentLesson;
import com.example.demo.model.InstructorCourse;
import com.example.demo.model.Term;
import com.example.demo.repository.StudentLessonRepository;
import com.example.demo.repository.StudentRepository;
import com.example.demo.repository.InstructorLessonRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;

import javax.transaction.Transactional;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class StudentServiceImpl implements StudentService {
    private final StudentRepository studentRepository;
    private final InstructorLessonRepository instructorLessonRepository;
    private final StudentLessonRepository studentLessonRepository;

    @Override
    @Transactional
    public StudentLesson selectUnit(InstructorCourse teacherCourse, Long studentId) {
        StudentLesson.SLId id = StudentLesson.SLId.builder()
                .studentId(studentId)
                .tcId(teacherCourse.getId())
                .build();

        Student student = studentRepository.findById(studentId)
                .orElseThrow(() -> new HttpClientErrorException(HttpStatus.NOT_FOUND));

        Optional<StudentLesson> sl = student.getStudentLessons()
                .stream().filter(l -> l.getInstructorCourse().getTerm().equals(teacherCourse.getTerm()) &&
                        l.getInstructorCourse().getTimeTable().conflict(teacherCourse.getTimeTable()))
                .findAny();

        if (sl.isPresent())
            throw TimeTableConflictException.builder()
                    .have(sl.get().getInstructorCourse())
                    .wanted(teacherCourse)
                    .build();

        return studentLessonRepository.save(
                StudentLesson.builder()
                        .student(student)
                        .id(id)
                        .instructorCourse(teacherCourse)
                        .build()
        );
    }

    @Override
    public List<StudentLesson> getLessons(Long studentId, Term term) {
        return studentRepository.findById(studentId)
                .orElseThrow(() -> new HttpClientErrorException(HttpStatus.NOT_FOUND))
                .getStudentLessons()
                .stream().filter(l -> l.getInstructorCourse().getTerm().equals(term))
                .collect(Collectors.toList());
    }
}
