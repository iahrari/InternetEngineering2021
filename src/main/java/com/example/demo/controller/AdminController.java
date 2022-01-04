package com.example.demo.controller;

import com.example.demo.exception.EntityNotFoundException;
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
        return "common/home";
    }

    @GetMapping("/terms")
    public String newTerm(Model model){
        model.addAttribute("terms", service.getAllTerms());
        model.addAttribute("term",
                Term.builder().termDate(Term.TermDate.builder().build()).build());
        return "admin/newTerm";
    }
//admin/admins
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
    public String saveTerm(@ModelAttribute @Valid Term term,
                           Errors errors, Model model){
        try {
            service.saveTerm(term);
        } catch (RuntimeException e){
            errors.rejectValue("enrollStart",
                    "error.term.conflict",
                    "There's a conflict with a term: " + e.getMessage());
            model.addAttribute("terms", service.getAllTerms());
            return "admin/newTerm";
        }

        return "redirect:/admin/terms";
    }

    @PostMapping("/admins")
    public String newAdmin(@ModelAttribute @Valid Admin admin,
                           Errors errors, Model model){
        if (errors.hasErrors()) {
            model.addAttribute("admins", service.getAllAdmins());
            return "admin/adminForm";
        }

        try {
            service.newAdmin(admin);
        } catch (RuntimeException e){
            model.addAttribute("admins", service.getAllAdmins());
            errors.rejectValue("username", "error.user.username", e.getMessage());
            return "admin/adminForm";
        }
        return "redirect:/admin/admins";
    }

    @PostMapping("/courses")
    public String saveCourse(@ModelAttribute @Valid Course course,
                             Errors errors, Model model){
        if (errors.hasErrors()) {
            model.addAttribute("courses", service.getAllCourses());
            return "admin/courseForm";
        }
        service.saveCourse(course);
        return "redirect:/admin/courses";
    }

    @PostMapping("/students")
    public String newStudent(@ModelAttribute @Valid Student student,
                             Errors errors, Model model){
        if (errors.hasErrors()) {
            model.addAttribute("students", service.getAllStudents());
            return "admin/newStudent";
        }

        try {
            service.newStudent(student);
        } catch (RuntimeException e){
            errors.rejectValue("username", "error.user.username", e.getMessage());
            return "admin/newStudent";
        }

        return "redirect:/admin/students";
    }

    @PostMapping("/instructors")
    public String newInstructor(@ModelAttribute @Valid Instructor instructor,
                                Errors errors,
                                Model model){
        if (errors.hasErrors()) {
            model.addAttribute("instructors", service.getAllInstructors());
            return "admin/newInstructorForm";
        }

        try {
            service.newInstructor(instructor);
        } catch (RuntimeException e){
            errors.rejectValue("username",
                    "error.user.username", e.getMessage());
            model.addAttribute("instructors", service.getAllInstructors());
            return "admin/newInstructorForm";
        }

        return "redirect:/admin/instructors";
    }

    @PostMapping("/assign-course/{courseId}")
    public String saveAssignInstructor(@PathVariable Long courseId,
                                       @ModelAttribute("currentTerm") Term currentTerm,
                                       @ModelAttribute("assign") @Valid AssignInstructorDTO assign,
                                       Errors assignErrors,
                                       Model model){
        if(assign.getExamDate() != null
                && ((assign.getExamDate().compareTo(currentTerm.getExamStart()) < 0)
                || (assign.getExamDate().compareTo(currentTerm.getExamEnd()) > 0))){
            assignErrors.rejectValue("examDate", "error.exam.date",
                    "Date of exam should be in exam time");
        }
        if (assignErrors.hasErrors()) {
            model.addAttribute("courseList", service.getInstructorSpecialCourse(currentTerm, courseId));
            return "admin/instructor-assign";
        }

        try {
            service.assignInstructor(assign.getInstructorId(), courseId,
                    assign.getTimeTable(), currentTerm, assign.getExamDate());
        } catch (TimeTableConflictException e){
            model.addAttribute("courseList", service.getInstructorSpecialCourse(currentTerm, courseId));
            assignErrors.rejectValue("saturday",
                    "error.timeTable",
                    "کلاس " + e.getHave().getCourse().getName() +
                            " با " + e.getWanted().getCourse().getName() + " تداخل دارد"
            );
            return "admin/instructor-assign";
        } catch (EntityNotFoundException e) {
            model.addAttribute("courseList", service.getInstructorSpecialCourse(currentTerm, courseId));
            assignErrors.rejectValue("instructorId", e.getInstructorId() + " پیدا نشد.");
            return "admin/instructor-assign";
        }
        return "redirect:/admin/assign-course/" + courseId;
    }
}
