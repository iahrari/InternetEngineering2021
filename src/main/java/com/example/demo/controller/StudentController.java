package com.example.demo.controller;

import com.example.demo.exception.TimeTableConflictException;
import com.example.demo.model.InstructorCourse;
import com.example.demo.model.Student;
import com.example.demo.model.StudentLesson;
import com.example.demo.model.Term;
import com.example.demo.service.StudentService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.Comparator;
import java.util.Date;
import java.util.stream.Collectors;

@Controller
@RequestMapping("/student")
@RequiredArgsConstructor
public class StudentController {
    private final StudentService service;

    @GetMapping
    public String home(){
        return "common/home";
    }

    @GetMapping("/enrollment")
    public String enrollForm(Model model,
                             @AuthenticationPrincipal Student student,
                             @ModelAttribute("currentTerm") Term term,
                             @ModelAttribute("now") Date now){
        if(term.getEnrollStart().compareTo(now) > 0
                || term.getEnrollEnd().compareTo(now) < 0) {
            model.addAttribute("type", "انتخاب واحد");
            return "common/NotStarted";
        }
        model.addAttribute("lessonList", service.getTermInstructorCourse(term, student));
        model.addAttribute("err", null);
        return "student/enrollment";
    }

    @GetMapping("/enrollment/{courseId}/{instructorId}")
    public String enroll(@AuthenticationPrincipal Student student,
                         @PathVariable("courseId") Long courseId,
                         @PathVariable("instructorId") Long instructorId,
                         @ModelAttribute("currentTerm") Term term,
                         @ModelAttribute("now") Date now,
                         Model model){
        if(term.getEnrollStart().compareTo(now) > 0
                || term.getEnrollEnd().compareTo(now) < 0) {
            model.addAttribute("type", "انتخاب واحد");
            return "common/NotStarted";
        }
        try {
            service.selectUnit(
                    InstructorCourse.TCId.builder()
                            .courseId(courseId)
                            .instructorId(instructorId)
                            .termDate(term.getTermDate())
                            .build(),
                    student);
        } catch (TimeTableConflictException e){
            var a = "کلاس " + e.getHave().getCourse().getName() +
                    " با " + e.getWanted().getCourse().getName() + " تداخل دارد";
            model.addAttribute("err", a);
            model.addAttribute("lessonList", service.getTermInstructorCourse(term, student));
            return "student/enrollment";
        }
        return "redirect:/student/enrollment";
    }

    @GetMapping("/time-table")
    public String getTimeTable(@AuthenticationPrincipal Student student,
                               @ModelAttribute("currentTerm") Term term,
                               Model model){
        model.addAttribute("timeTable",
                service.getTermTimeTableDTO(term, student));
        return "common/timeTable";
    }

    @GetMapping("/exam-time-table")
    public String getExamTimeTable(@AuthenticationPrincipal Student student,
                                   @ModelAttribute("currentTerm") Term term,
                                   Model model){
        var exams = service.getLessons(student.getId(), term)
                .stream().map(StudentLesson::getInstructorCourse)
                .sorted(Comparator.comparingDouble(i -> i.getExamDate().getTime()))
                .collect(Collectors.toList());

        model.addAttribute("exams", exams);
        return "common/examTimeTable";
    }

    @GetMapping("/grades")
    public String grades(@AuthenticationPrincipal Student student,
                         @ModelAttribute("currentTerm") Term term,
                         @ModelAttribute("now") Date now,
                         Model model){
        if(term.getExamStart().compareTo(now) > 0) {
            model.addAttribute("type", "امتحانات");
            return "common/NotStarted";
        }

        model.addAttribute("grades", service.getLessons(student.getId(), term));
        return "student/grades";
    }
}
