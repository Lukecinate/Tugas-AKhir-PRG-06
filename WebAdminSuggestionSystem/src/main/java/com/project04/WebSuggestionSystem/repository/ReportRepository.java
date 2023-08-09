package com.project04.WebSuggestionSystem.repository;

import com.project04.WebSuggestionSystem.model.Report;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ReportRepository extends JpaRepository<Report,Integer> {
    List<Report>findAll();
}
