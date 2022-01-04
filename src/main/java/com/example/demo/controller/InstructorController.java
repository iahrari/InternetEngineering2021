package com.example.demo.controller;

import com.example.demo.model.Instructor;
import com.example.demo.model.Term;
import com.example.demo.model.wrapper.GradeWrapper;
import com.example.demo.service.InstructorService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.Comparator;
import java.util.Date;
import java.util.stream.Collectors;

@Controller
@RequestMapping("/instructor")
@RequiredArgsConstructor
public class InstructorController {
    private final InstructorService service;

    @GetMapping
    public String home(){
        return "common/home";
    }

    @GetMapping("/time-table")
    public String getTimeTable(@AuthenticationPrincipal Instructor instructor,
                               @ModelAttribute("currentTerm") Term term,
                               Model model){
        model.addAttribute("timeTable",
                service.getTermTimeTableDTO(term, instructor.getId()));
        return "common/timeTable";
    }

    @GetMapping("/exam-time-table")
    public String getExamTimeTable(@AuthenticationPrincipal Instructor instructor,
                                   @ModelAttribute("currentTerm") Term term,
                                   Model model){
        var exams = service.getTimeTable(instructor.getId(), term).stream()
                .sorted(Comparator.comparingDouble(i -> i.getExamDate().getTime()))
                .collect(Collectors.toList());

        model.addAttribute("exams", exams);
        return "common/examTimeTable";
    }

    @GetMapping("/courses")
    public String getGrades(@AuthenticationPrincipal Instructor instructor,
                            @ModelAttribute("currentTerm") Term term,
                            Model model){
        model.addAttribute("courses",
                service.getTimeTable(instructor.getId(), term));
        return "instructor/courses";
    }

    @GetMapping("/courses/{courseId}")
    public String getClassGrades(@AuthenticationPrincipal Instructor instructor,
                            @ModelAttribute("currentTerm") Term term,
                            @ModelAttribute("now") Date now,
                            @PathVariable Long courseId,
                            Model model){
        model.addAttribute("students",
                service.getInstructorCourse(instructor.getId(), term, courseId));
        return "instructor/courseStudents";
    }

    @GetMapping("/courses/{courseId}/{studentUsername}")
    public String getStudentClassGrades(@AuthenticationPrincipal Instructor instructor,
                                 @ModelAttribute("currentTerm") Term term,
                                 @ModelAttribute("now") Date now,
                                 @PathVariable Long courseId,
                                 @PathVariable String studentUsername,
                                 Model model){
        if(term.getExamStart().compareTo(now) > 0) {
            model.addAttribute("type", "امتحانات");
            return "common/NotStarted";
        }

        model.addAttribute("studentUsername", studentUsername);
        model.addAttribute("course",
                service.getInstructorCourse(instructor.getId(), term, courseId)
                        .getCourse());
        model.addAttribute("grade", new GradeWrapper());
        return "instructor/setStudentGrade";
    }

    @PostMapping("/courses/{courseId}/{studentUsername}")
    public String setGrade(@AuthenticationPrincipal Instructor instructor,
                                        @ModelAttribute("currentTerm") Term term,
                                        @ModelAttribute("now") Date now,
                                        @ModelAttribute("grade") @Valid GradeWrapper grade,
                                        Errors gradeErrors,
                                        @PathVariable Long courseId,
                                        @PathVariable String studentUsername,
                                        Model model){
        if(term.getExamStart().compareTo(now) > 0) {
            model.addAttribute("type", "امتحانات");
            return "common/NotStarted";
        }
        if (gradeErrors.hasErrors())
            return "instructor/courseStudents";
        service.setGrade(term, instructor.getId(), studentUsername, courseId, grade.getGrade());
        return "redirect:/instructor/courses/" + courseId;
    }
}
