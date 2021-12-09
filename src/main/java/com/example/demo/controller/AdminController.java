package com.example.demo.controller;

import com.example.demo.exception.TimeTableConflictException;
import com.example.demo.model.*;
import com.example.demo.service.AdminService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@Controller
@RequestMapping("/admin")
@RequiredArgsConstructor
public class AdminController {
    private final AdminService service;

    @GetMapping
    public String homePage(){
        return "admin/home";
    }

    @GetMapping("/terms")
    public String newTerm(Model model){
        model.addAttribute("terms", service.getAllTerms());
        model.addAttribute("term",
                Term.builder().termDate(Term.TermDate.builder().build()).build());
        return "admin/newTerm";
    }

    @GetMapping("/admins")
    public String adminForm(Model model){
        model.addAttribute("admins", service.getAllAdmins());
        model.addAttribute("admin", Admin.builder().build());
        return "admin/adminForm";
    }

    @GetMapping("/courses")
    public String courseForm(Model model){
        model.addAttribute("courses", service.getAllCourses());
        model.addAttribute("course", Course.builder().build());
        return "admin/courseForm";
    }

    @GetMapping("/students")
    public String studentForm(Model model){
        model.addAttribute("students", service.getAllStudents());
        model.addAttribute("student", Student.builder().build());
        return "admin/newStudent";
    }

    @GetMapping("/instructors")
    public String instructorForm(Model model){
        model.addAttribute("instructors", service.getAllInstructors());
        model.addAttribute("instructor", Instructor.builder().build());
        return "admin/newInstructorForm";
    }

    @GetMapping("/assign-course/{courseId}")
    public String assignInstructor(Model model, @PathVariable Long courseId, @ModelAttribute("currentTerm") Term term){
        model.addAttribute("courseList", service.getInstructorSpecialCourse(term, courseId));
        model.addAttribute("courseId", courseId);
        model.addAttribute("assign", new AssignInstructorDTO());
        return "admin/instructor-assign";
    }

    @PostMapping("/terms")
    public String saveTerm(@ModelAttribute @Valid Term term, Errors errors){
        try {
            service.saveTerm(term);
        } catch (RuntimeException e){
            errors.rejectValue("enrollStart",
                    "error.term.conflict",
                    "There's a conflict with a term: " + e.getMessage());
            return "admin/newTerm";
        }

        return "redirect:/admin/terms";
    }

    @PostMapping("/admins")
    public String newAdmin(@ModelAttribute @Valid Admin admin, Errors errors){
        if (errors.hasErrors())
            return "admin/adminForm";

        try {
            service.newAdmin(admin);
        } catch (RuntimeException e){
            errors.rejectValue("username", "error.user.username", e.getMessage());
            return "admin/adminForm";
        }
        return "redirect:/admin/admins";
    }

    @PostMapping("/courses")
    public String saveCourse(@ModelAttribute @Valid Course course, Errors errors){
        if (errors.hasErrors())
            return "admin/courseForm";
        service.saveCourse(course);
        return "redirect:/admin/courses";
    }

    @PostMapping("/students")
    public String newStudent(@ModelAttribute @Valid Student student, Errors errors){
        if (errors.hasErrors())
            return "admin/newStudent";

        try {
            service.newStudent(student);
        } catch (RuntimeException e){
            errors.rejectValue("username", "error.user.username", e.getMessage());
            return "admin/newStudent";
        }

        return "redirect:/admin/students";
    }

    @PostMapping("/instructors")
    public String newInstructor(@ModelAttribute @Valid Instructor instructor, Errors errors){
        if (errors.hasErrors())
            return "admin/newInstructorForm";

        try {
            service.newInstructor(instructor);
        } catch (RuntimeException e){
            errors.rejectValue("username", "error.user.username", e.getMessage());
            return "admin/newInstructorForm";
        }

        return "redirect:/admin/instructors";
    }

    @PostMapping("/assign-course/{courseId}")
    public String saveAssignInstructor(@PathVariable Long courseId,
                                       @ModelAttribute("currentTerm") Term currentTerm,
                                       @ModelAttribute("assign") @Valid AssignInstructorDTO assign,
                                       Errors assignErrors){
        if((assign.getExamDate().compareTo(currentTerm.getExamStart()) < 0)
                || (assign.getExamDate().compareTo(currentTerm.getExamEnd()) > 0)){
            assignErrors.rejectValue("examDate", "error.exam.date",
                    "Date of exam should be in exam time");
        }
        if (assignErrors.hasErrors())
            return "admin/instructor-assign";

        try {
            service.assignInstructor(assign.getInstructorId(), courseId,
                    assign.getTimeTable(), currentTerm, assign.getExamDate());
        } catch (TimeTableConflictException e){
            assignErrors.rejectValue("saturday",
                    "error.timeTable",
                    "Instructor has a class at: " + e.getHave() +
                            " but tried to assigned: " + e.getWanted()
            );
            return "admin/instructor-assign";
        }
        return "redirect:/admin";
    }
}
