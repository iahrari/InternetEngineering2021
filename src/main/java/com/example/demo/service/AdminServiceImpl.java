package com.example.demo.service;

import com.example.demo.model.*;
import com.example.demo.repository.*;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;

import javax.transaction.Transactional;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class AdminServiceImpl implements AdminService {
    private final UserRepository userRepository;
    private final TermRepository termRepository;
    private final AdminRepository adminRepository;
    private final PasswordEncoder passwordEncoder;
    private final CourseRepository courseRepository;
    private final StudentRepository studentRepository;
    private final InstructorRepository instructorRepository;
    private final InstructorLessonRepository instructorLessonRepository;

    @Override
    public void newInstructor(Instructor instructor) {
        if (userRepository.existsByUsername(instructor.getUsername()))
            throw new RuntimeException(instructor.getUsername() + " is taken");

        userRepository.save(Instructor.builder()
                .family(instructor.getFamily())
                .name(instructor.getName())
                .nationalId(instructor.getNationalId())
                .username(instructor.getUsername())
                .password(passwordEncoder.encode(instructor.getPassword()))
                .build());
    }

    @Override
    public void newAdmin(Admin admin) {
        if (userRepository.existsByUsername(admin.getUsername()))
            throw new RuntimeException(admin.getUsername() + " is taken");

        adminRepository.save(Admin.builder()
                .family(admin.getFamily())
                .name(admin.getName())
                .nationalId(admin.getNationalId())
                .username(admin.getUsername())
                .password(passwordEncoder.encode(admin.getPassword()))
                .build());
    }

    @Override
    public void newStudent(Student student) {
        if (userRepository.existsByUsername(student.getUsername()))
            throw new RuntimeException(student.getUsername() + " is taken");

        studentRepository.save(Student.builder()
                .family(student.getFamily())
                .name(student.getName())
                .nationalId(student.getNationalId())
                .username(student.getUsername())
                .password(passwordEncoder.encode(student.getPassword()))
                .build());
    }

    @Override
    public void saveCourse(Course course) {
        courseRepository.save(course);
    }

    @Override
    @Transactional
    public void assignInstructor(String instructorUsername, Long courseId,
                                 TimeTable timeTable, Term term,
                                 LocalDateTime examDate) {
        Instructor instructor = instructorRepository.findByUsername(instructorUsername)
                .orElseThrow(() -> new HttpClientErrorException(HttpStatus.NOT_FOUND));
        Course course = courseRepository.findById(courseId)
                .orElseThrow(() -> new HttpClientErrorException(HttpStatus.NOT_FOUND));

        InstructorCourse.TCId id = InstructorCourse.TCId.builder()
                .instructorId(instructor.getId())
                .courseId(course.getId())
                .termDate(term.getTermDate()).build();

        InstructorCourse iCourse = InstructorCourse.builder()
                .course(course)
                .examDate(examDate)
                .instructor(instructor)
                .id(id)
                .timeTable(timeTable)
                .term(term)
                .build();

        instructorLessonRepository.save(iCourse);
    }

    @Override
    public void saveTerm(Term term) {
        Optional<RuntimeException> a = termRepository.findAllByEnrollStartBetween(
                term.getEnrollStart(), term.getExamEnd())
                .stream().findAny()
                .map(t -> new RuntimeException(
                        t.getEnrollStart().toString() + " - " + t.getExamEnd().toString()));
        Optional<RuntimeException> b = termRepository.findAllByExamEndBetween(
                term.getEnrollStart(), term.getExamEnd())
                .stream().findAny()
                .map(t -> new RuntimeException(
                    t.getEnrollStart().toString() + " - " + t.getExamEnd().toString()));

        if (a.isPresent()) throw a.get();
        if (b.isPresent()) throw b.get();

        termRepository.save(term);
    }

    @Override
    public List<Term> getAllTerms() {
        return termRepository.findAll();
    }

    @Override
    public Term getTerm() {
        var date = new Date();
        return termRepository.findByEnrollStartBeforeAndExamEndAfter(date, date)
                .orElse(Term.builder()
                        .termDate(Term.TermDate.builder().build())
                        .build());
    }
}
