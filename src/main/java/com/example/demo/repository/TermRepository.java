package com.example.demo.repository;

import com.example.demo.model.Term;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Date;
import java.util.List;
import java.util.Optional;

public interface TermRepository extends JpaRepository<Term, Term.TermDate> {
    List<Term> findAllByEnrollStartBetween(Date first, Date end);
    List<Term> findAllByExamEndBetween(Date first, Date end);
    Optional<Term> findByEnrollStartBeforeAndExamEndAfter(Date enrollStart, Date examEnd);
    Optional<Term> findFirstByOrderByEnrollEndDesc();
//    Optional<Term>
}
