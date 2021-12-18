package com.example.demo.service;

import com.example.demo.exception.TimeTableConflictException;
import com.example.demo.model.*;
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
    public StudentLesson selectUnit(InstructorCourse.TCId teacherCourseId, Student student) {
        var stu = studentRepository.findById(student.getId())
                .orElseThrow(() -> new HttpClientErrorException(HttpStatus.NOT_FOUND));

        var teacherCourse = instructorLessonRepository.findById(teacherCourseId)
                .orElseThrow(() -> new HttpClientErrorException(HttpStatus.NOT_FOUND));

        StudentLesson.SLId id = StudentLesson.SLId.builder()
                .studentId(student.getId())
                .tcId(teacherCourse.getId())
                .build();

        Optional<StudentLesson> sl = stu.getStudentLessons()
                .stream().filter(l -> l.getInstructorCourse().getTerm().equals(teacherCourse.getTerm()) &&
                        l.getInstructorCourse().getTimeTable().conflict(teacherCourse.getTimeTable()))
                .findAny();

        Optional<StudentLesson> sl2 = stu.getStudentLessons().stream()
                .filter(l -> l.getInstructorCourse().getExamDate()
                        .equals(teacherCourse.getExamDate()))
                .findAny();

        if (sl.isPresent())
            throw TimeTableConflictException.builder()
                    .have(sl.get().getInstructorCourse())
                    .wanted(teacherCourse)
                    .build();

        if (sl2.isPresent())
            throw TimeTableConflictException.builder()
                    .have(sl2.get().getInstructorCourse())
                    .wanted(teacherCourse)
                    .build();
        var sLesson = studentLessonRepository.save(
                StudentLesson.builder()
                        .student(stu)
                        .id(id)
                        .instructorCourse(teacherCourse)
                        .build());

        stu.getStudentLessons().add(sLesson);
        teacherCourse.getStudentLesson().add(sLesson);
        return sLesson;
    }

    @Override
    public List<StudentLesson> getLessons(Long studentId, Term term) {
        return studentRepository.findById(studentId)
                .orElseThrow(() -> new HttpClientErrorException(HttpStatus.NOT_FOUND))
                .getStudentLessons()
                .stream().filter(l -> l.getInstructorCourse().getTerm().equals(term))
                .collect(Collectors.toList());
    }

    @Override
    public List<InstructorCourse> getTermInstructorCourse(Term term, Student student) {
        var stu = studentRepository.findById(student.getId())
                .orElseThrow(() -> new HttpClientErrorException(HttpStatus.NOT_FOUND));

        return instructorLessonRepository.findByTerm(term)
                .stream().filter(c -> stu.getStudentLessons().stream()
                        .filter(sl -> c.getId().getCourseId()
                                .equals(sl.getInstructorCourse().getCourse().getId()))
                        .findAny().isEmpty())
                .collect(Collectors.toList());
    }

    @Override
    public TimeTableDTO getTermTimeTableDTO(Term term, Student student) {
        var stu = studentRepository.findById(student.getId())
                .orElseThrow(() -> new HttpClientErrorException(HttpStatus.NOT_FOUND));

        return TimeTableDTO.convertFromTimeTables(
                stu.getStudentLessons().stream()
                        .map(StudentLesson::getInstructorCourse)
                        .filter(c -> c.getTerm().equals(term))
                        .collect(Collectors.toList())
        );
    }
}
