package com.example.demo.controller;

import com.example.demo.service.AdviceService;
import lombok.RequiredArgsConstructor;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ModelAttribute;

@ControllerAdvice
@RequiredArgsConstructor
public class AdviceController {
    private final AdviceService adviceService;

    @ModelAttribute
    public void getCurrentTerm(Model model){
        model.addAttribute("currentTerm",
                adviceService.getCurrentTerm());
    }
}
