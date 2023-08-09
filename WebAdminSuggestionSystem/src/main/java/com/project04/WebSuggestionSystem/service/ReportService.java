package com.project04.WebSuggestionSystem.service;

import com.project04.WebSuggestionSystem.model.Feedback;
import com.project04.WebSuggestionSystem.model.Report;
import com.project04.WebSuggestionSystem.repository.ReportRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ReportService {
    @Autowired
    ReportRepository reportRepository;

    public List<Report> getAllReport(){
        List<Report> res = reportRepository.findAll();
        if (res.isEmpty())
            return null;

        return res;
    }
}
