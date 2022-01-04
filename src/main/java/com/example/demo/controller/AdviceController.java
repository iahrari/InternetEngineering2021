package com.example.demo.controller;

import com.example.demo.model.User;
import com.example.demo.service.AdviceService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ModelAttribute;

import java.util.Date;

@ControllerAdvice
@RequiredArgsConstructor
public class AdviceController {
    private final AdviceService adviceService;

    @ModelAttribute
    public void getCurrentTerm(Model model){
        model.addAttribute("currentTerm",
                adviceService.getCurrentTerm());
        model.addAttribute("now", new Date());
    }

    @ModelAttribute
    public void getCurrentUser(Model model, @AuthenticationPrincipal User user){
        model.addAttribute("user", user);
    }
}
