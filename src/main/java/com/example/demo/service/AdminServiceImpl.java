package com.example.demo.service;

import com.example.demo.exception.EntityNotFoundException;
import com.example.demo.exception.TimeTableConflictException;
import com.example.demo.model.*;
import com.example.demo.repository.*;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;

import javax.transaction.Transactional;
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
            throw new RuntimeException(instructor.getUsername() + " قبلا توسط کاربر دیگری اخذ شده‌است.");

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
            throw new RuntimeException(admin.getUsername() + " قبلا توسط کاربر دیگری اخذ شده‌است.");

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
            throw new RuntimeException(student.getUsername() + " قبلا توسط کاربر دیگری اخذ شده‌است.");

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
                                 Date examDate) {
        Instructor instructor = instructorRepository.findByUsername(instructorUsername)
                .orElseThrow(() -> new EntityNotFoundException(instructorUsername));
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

        var conflict = instructor.getInstructorCourse().stream()
                .filter(t -> t.getTimeTable().conflict(timeTable))
                .findAny();
        if (conflict.isPresent())
            throw new TimeTableConflictException(conflict.get(), iCourse);

        var il = instructorLessonRepository.save(iCourse);
        instructor.getInstructorCourse().add(il);
        course.getInstructorCourse().add(il);
    }

    @Override
    public void saveTerm(Term term) {
        Optional<RuntimeException> a = termRepository.findAllByEnrollStartBetween(
                term.getEnrollStart(), term.getExamEnd())
                .stream().findAny()
                .map(t -> new RuntimeException(
                        t.getTermDate().getYear() + " - " + t.getTermDate().getTerm()));
        Optional<RuntimeException> b = termRepository.findAllByExamEndBetween(
                term.getEnrollStart(), term.getExamEnd())
                .stream().findAny()
                .map(t -> new RuntimeException(
                        t.getTermDate().getYear() + " - " + t.getTermDate().getTerm()));

        if (a.isPresent()) throw a.get();
        if (b.isPresent()) throw b.get();

        termRepository.save(term);
    }

    @Override
    public List<Term> getAllTerms() {
        return termRepository.findAll();
    }

    @Override
    public List<Course> getAllCourses() {
        return courseRepository.findAll();
    }

    @Override
    public Term getTerm() {
        return termRepository.findFirstByOrderByEnrollEndDesc()
                .orElse(Term.builder()
                        .termDate(Term.TermDate.builder().build())
                        .build());
    }

    @Override
    public List<Instructor> getAllInstructors() {
        return instructorRepository.findAll();
    }

    @Override
    public List<Student> getAllStudents() {
        return studentRepository.findAll();
    }

    @Override
    public List<Admin> getAllAdmins() {
        return adminRepository.findAll();
    }

    @Override
    public List<InstructorCourse> getTermInstructorCourse(Term term) {
        return instructorLessonRepository.findByTerm(term);
    }

    @Override
    public List<InstructorCourse> getInstructorSpecialCourse(Term term, Long courseId) {
        return instructorLessonRepository.findAllByCourseAndTerm(
                courseRepository.findById(courseId)
                        .orElseThrow(() -> new HttpClientErrorException(HttpStatus.NOT_FOUND)),
                term
        );
    }
}
