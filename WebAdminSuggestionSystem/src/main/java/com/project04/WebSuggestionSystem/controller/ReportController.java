package com.project04.WebSuggestionSystem.controller;

import com.project04.WebSuggestionSystem.model.Feedback;
import com.project04.WebSuggestionSystem.model.Report;
import com.project04.WebSuggestionSystem.repository.ReportRepository;
import com.project04.WebSuggestionSystem.service.ReportService;
import net.sf.jasperreports.engine.*;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;
import net.sf.jasperreports.engine.design.JasperDesign;
import net.sf.jasperreports.engine.xml.JRXmlLoader;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
public class ReportController {

    @Autowired
    ReportRepository reportRepository;

    @Autowired
    ReportService reportService;

    @GetMapping("/getAllReport")
    public List<Report> getAllFeedback(){
        return reportService.getAllReport();
    }


    @RequestMapping(value = "/reportSS", produces = MediaType.APPLICATION_PDF_VALUE)
    public void gen(HttpServletResponse response) throws JRException, IOException {

        //Feedback feedback = new Feedback();

        //List<Feedback> reports = feedbackService.getAllFeedback();
        List<Report> reports = reportService.getAllReport();

        // Compile the Jasper report template
        InputStream reportStream = this.getClass().getResourceAsStream("/reportSS.jrxml");
        JasperDesign jd = JRXmlLoader.load(reportStream);
        JasperReport jr = JasperCompileManager.compileReport(jd);

        //Inisialisasi Untuk get Service
        //  Service service=servicesService.getById(id);
        // Create parameters map and set the parameter value
        Map<String, Object> parameters = new HashMap<>();
//        parameters.put("unit", service.getNama_unit()); // Set the parameter value
//        parameters.put("serialnum", service.getSerialnumber()); // Set the parameter value
//        parameters.put("date", convertToIndonesianDateString(service.getCreate_date())); // Set the parameter value
//        parameters.put("hours",service.getKm_service()); // Set the parameter value

        // Fill the report with da//ta and parameters
        JRBeanCollectionDataSource dataSource = new JRBeanCollectionDataSource(reports);
        JasperPrint jasperPrint = JasperFillManager.fillReport(jr, parameters, dataSource);

        // Export the report to PDF
        byte[] pdfBytes = JasperExportManager.exportReportToPdf(jasperPrint);

        // Set the response content type
        response.setContentType(MediaType.APPLICATION_PDF_VALUE);

        // Set the response header
        response.setHeader("Content-Disposition", "inline; filename=report.pdf");

        // Write the PDF bytes to the response output stream
        OutputStream outputStream = response.getOutputStream();
        outputStream.write(pdfBytes);
        outputStream.flush();
    }
}
