package com.example.demo.service;

import com.example.demo.model.Term;
import com.example.demo.repository.TermRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AdviceServiceImpl implements AdviceService {
    private final TermRepository termRepository;

    @Override
    public Term getCurrentTerm() {
        return termRepository.findFirstByOrderByEnrollEndDesc()
                .orElse(Term.builder().build());
    }
}
