package com.example.demo.controller;

import com.example.demo.model.*;
import com.example.demo.model.wrapper.Wrapper;
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
@SessionAttributes("currentTerm")
public class AdminController {
    private final AdminService service;

    @ModelAttribute("currentTerm")
    private Term currentTerm(){
        return service.getTerm();
    }

    @GetMapping
    public String homePage(){
        return "admin/home";
    }

    @GetMapping("/new-term")
    public String newTerm(Model model){
        model.addAttribute("term",
                Term.builder().termDate(Term.TermDate.builder().build()).build());
        return "admin/newTerm";
    }

    @GetMapping("/new-admin")
    public String adminForm(Model model){
        model.addAttribute("admin", Admin.builder().build());
        return "admin/adminForm";
    }

    @GetMapping("/new-course")
    public String courseForm(Model model){
        model.addAttribute("course", Course.builder().build());
        return "admin/courseForm";
    }

    @GetMapping("/new-student")
    public String studentForm(Model model){
        model.addAttribute("student", Student.builder().build());
        return "admin/newStudent";
    }

    @GetMapping("/new-instructor")
    public String instructorForm(Model model){
        model.addAttribute("instructor", Instructor.builder().build());
        return "admin/newInstructorForm";
    }

    @GetMapping("/assign-course/{courseId}")
    public String assignInstructor(Model model, @PathVariable String courseId){
        model.addAttribute("courseId", courseId);
        model.addAttribute("timeTable", TimeTable.builder().build());
        model.addAttribute("terms", service.getAllTerms());
        model.addAttribute("termDate", Term.TermDate.builder().build());
        model.addAttribute("instructor", Instructor.builder().build());
        model.addAttribute("examDate", new Wrapper());
        return "admin/instructor-assign";
    }

    @PostMapping("/new-term")
    public String saveTerm(@ModelAttribute @Valid Term term, Errors errors){
        try {
            service.saveTerm(term);
        } catch (RuntimeException e){
            errors.rejectValue("enrollStart",
                    "error.term.conflict",
                    "There's a conflict with a term: " + e.getMessage());
            return "admin/newTerm";
        }

        return "redirect:/admin";
    }

    @PostMapping("/new-admin")
    public String newAdmin(@ModelAttribute @Valid Admin admin, Errors errors){
        if (errors.hasErrors())
            return "admin/adminForm";

        try {
            service.newAdmin(admin);
        } catch (RuntimeException e){
            errors.rejectValue("username", "error.user.username", e.getMessage());
            return "admin/adminForm";
        }
        return "redirect:/admin";
    }

    @PostMapping("/new-course")
    public String saveCourse(@ModelAttribute @Valid Course course, Errors errors){
        if (errors.hasErrors())
            return "admin/courseForm";
        service.saveCourse(course);
        return "redirect:/admin";
    }

    @PostMapping("/new-student")
    public String newStudent(@ModelAttribute @Valid Student student, Errors errors){
        if (errors.hasErrors())
            return "admin/newStudent";

        try {
            service.newStudent(student);
        } catch (RuntimeException e){
            errors.rejectValue("username", "error.user.username", e.getMessage());
            return "admin/newStudent";
        }

        return "redirect:/admin";
    }

    @PostMapping("/new-instructor")
    public String newInstructor(@ModelAttribute @Valid Instructor instructor, Errors errors){
        if (errors.hasErrors())
            return "admin/newInstructorForm";

        try {
            service.newInstructor(instructor);
        } catch (RuntimeException e){
            errors.rejectValue("username", "error.user.username", e.getMessage());
            return "admin/newInstructorForm";
        }

        return "redirect:/admin/";
    }

    @PostMapping("/assign-course/{courseId}")
    public String saveAssignInstructor(@PathVariable Long courseId,
                                       @ModelAttribute TimeTable timeTable,// Errors timeError,
                                       @ModelAttribute Instructor instructor,// Errors insError,
                                       @ModelAttribute Wrapper examDate){//, Errors dateError){
//        if (timeError.hasErrors() || insError.hasErrors() || termError.hasErrors() || dateError.hasErrors())
//            return "admin/instructor-assign";
        service.assignInstructor(instructor.getUsername(), courseId,
                timeTable, currentTerm(), examDate.getData());
        return "redirect:/admin";
    }
}
