package com.example.demo.controller;

import com.example.demo.model.Term;
import com.example.demo.repository.TermRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Calendar;
import java.util.Date;

@RestController
@RequiredArgsConstructor
public class GameController {
    private final TermRepository termRepository;

    @GetMapping
    public void check(){

        Calendar endEnroll = Calendar.getInstance();
        endEnroll.add(Calendar.MINUTE, 3);
        Calendar startExam = Calendar.getInstance();
        startExam.add(Calendar.MINUTE, 5);

        Calendar endExam = Calendar.getInstance();
        endExam.add(Calendar.WEEK_OF_MONTH, 2);

        Term term = Term.builder()
                .enrollStart(new Date())
                .enrollEnd(endEnroll.getTime())
                .examStart(startExam.getTime())
                .examEnd(endExam.getTime())
                .termDate(Term.TermDate.builder().term(0).year("1400").build())
                .build();

        termRepository.save(term);
    }
}
