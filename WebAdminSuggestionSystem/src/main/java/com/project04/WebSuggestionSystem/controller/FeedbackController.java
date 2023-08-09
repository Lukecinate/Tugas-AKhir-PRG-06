package com.project04.WebSuggestionSystem.controller;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import com.project04.WebSuggestionSystem.repository.FeedbackRepository;
import net.sf.jasperreports.engine.*;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;
import net.sf.jasperreports.engine.design.JasperDesign;
import net.sf.jasperreports.engine.xml.JRXmlLoader;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.ui.ModelMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import com.project04.WebSuggestionSystem.model.Feedback;
import com.project04.WebSuggestionSystem.service.FeedbackService;

import javax.servlet.http.HttpServletResponse;

@RestController
public class FeedbackController {
    @Autowired
    FeedbackService feedbackService;
    @Autowired
    FeedbackRepository feedbackRepository;

    @GetMapping("/getAllFeedback")
    public List<Feedback> getAllFeedback(){
        return feedbackService.getAllFeedback();
    }

    @GetMapping("/getFeedback")
    public Feedback getFeedback(@RequestParam("id") int id){
        return feedbackService.get(id);
    }

    @GetMapping("/getAllFeedbackBefore")
    public List<Feedback> getAllFeedbackBefore(){
        return feedbackService.getAllBeforeFeedback();
    }

    @GetMapping("/getAllFeedbackOngoing")
    public List<Feedback> getAllFeedbackOngoing(){
        return feedbackService.getAllOngoingFeedback();
    }

    @GetMapping("/getAllFeedbackAfter")
    public List<Feedback> getAllFeedbackAfter(){
        return feedbackService.getAllAfterFeedback();
    }

    @GetMapping("/getFeebackByKeywords")
    public List<Feedback> getFeebackByKeywords(@RequestParam("keyword") String keyword){
        return feedbackService.getAllByKeywords(keyword);
    }

    @PutMapping("/doAttemptFeedback")
    public Feedback doAttemptFeedback(@RequestParam("id") int id){
        return feedbackService.doAttempt(id, "Ongoing");
    }

    @PostMapping("/savefeedback")
    public ResponseEntity<String> savefeedback(ModelMap m,  MultipartHttpServletRequest request, @RequestParam("area_id") int area_id, @RequestParam("created_date") @DateTimeFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss") LocalDateTime created_date, @RequestParam("deadline") @DateTimeFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss") LocalDateTime deadline, @RequestParam("pre_status") String pre_status, @RequestParam("suggest_name") String suggest_name, @RequestParam("suggestion") String suggestion, @RequestParam("title") String title) {

        MultiValueMap<String, MultipartFile> multiFileMap = request.getMultiFileMap();
        List<MultipartFile> files = multiFileMap.isEmpty() ? null : multiFileMap.values().iterator().next();
        MultipartFile file = files != null && !files.isEmpty() ? files.get(0) : null;

        // Example of Set Paths
        //Path resourceDirectory = Paths.get("target","classes","static","upload");
        //Path resourceDirectory = Paths.get("src","main","resources","static");

        Path resourceDirectory = Paths.get("src", "main", "resources", "static", "uploads");
        String absolutePath = resourceDirectory.toFile().getAbsolutePath();


        if (file.isEmpty()) {
            return ResponseEntity.badRequest().body("No photo uploaded.");
        }
        try {

            // Generate a unique file name for the uploaded photo
            String fileName = "Syss-Pre-" + UUID.randomUUID().toString() + "." + getFileExtension(file.getOriginalFilename());

            // String fileName = UUID.randomUUID().toString() + "." + getFileExtension(file1.getOriginalFilename());
            Feedback feedback = new Feedback();

            feedback.setArea_id(area_id);
            feedback.setCreatedDate(created_date);
            feedback.setDeadline(deadline);
            feedback.setPrePhoto(fileName);
            feedback.setPreStatus(pre_status);
            feedback.setSuggestName(suggest_name);
            feedback.setSuggestion(suggestion);
            feedback.setTitle(title);
            feedbackService.save(feedback);

            // Save the photo to the upload directory
            Path filePath = Paths.get(absolutePath, fileName);
            Files.copy(file.getInputStream(), filePath, StandardCopyOption.REPLACE_EXISTING);

            // Return the file path or other response as needed
            return ResponseEntity.ok(filePath.toString());
        } catch (IOException e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Failed to upload photo.");
        }
    }

    @PutMapping("/doFinishFeedback")
    public ResponseEntity<String> doFinishFeedback(ModelMap m,  MultipartHttpServletRequest request, @RequestParam("id") int id, @RequestParam("worker_name") String worker_name){

        MultiValueMap<String, MultipartFile> multiFileMap = request.getMultiFileMap();
        List<MultipartFile> files = multiFileMap.isEmpty() ? null : multiFileMap.values().iterator().next();
        MultipartFile file = files != null && !files.isEmpty() ? files.get(0) : null;

        // Example of Set Paths
        //Path resourceDirectory = Paths.get("target","classes","static","upload");
        //Path resourceDirectory = Paths.get("src","main","resources","static");

        Path resourceDirectory = Paths.get("src", "main", "resources", "static", "uploads");
        String absolutePath = resourceDirectory.toFile().getAbsolutePath();


        if (file.isEmpty()) {
            return ResponseEntity.badRequest().body("No photo uploaded.");
        }
        try {

            // Generate a unique file name for the uploaded photo
            String fileName = "Syss-Post-" + UUID.randomUUID().toString() + "." + getFileExtension(file.getOriginalFilename());

            // String fileName = UUID.randomUUID().toString() + "." + getFileExtension(file1.getOriginalFilename());
            Feedback feedback = new Feedback();

            feedback.setId(id);
            feedback.setPostPhoto(fileName);
            feedback.setPostStatus("After");
            feedback.setWorkerName(worker_name);

            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss");
            String formattedDateTime = LocalDateTime.now().format(formatter);

// Parse the formatted string back to LocalDateTime
            LocalDateTime modifiedDate = LocalDateTime.parse(formattedDateTime, formatter);

            feedback.setModifiedDate(modifiedDate);

            feedbackService.update(feedback.getId(), feedback);

            // Save the photo to the upload directory
            Path filePath = Paths.get(absolutePath, fileName);
            Files.copy(file.getInputStream(), filePath, StandardCopyOption.REPLACE_EXISTING);

            // Return the file path or other response as needed
            return ResponseEntity.ok(filePath.toString());

        } catch (IOException e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Failed to upload photo.");
        }
    }

    private String getFileExtension(String filename) {
        if (filename != null && !filename.isEmpty()) {
            int dotIndex = filename.lastIndexOf('.');
            if (dotIndex >= 0 && dotIndex < filename.length() - 1) {
                return filename.substring(dotIndex + 1);
            }
        }
        return "";
    }

    @GetMapping("/countFeedbackBeforeStatus")
    private Long countFeedbackBeforeStatus(){
        return feedbackService.countFeedbackBeforeStatus();
    }

    @GetMapping("/countFeedbackAfterStatus")
    private Long countFeedbackAfterStatus(){
        return feedbackService.countFeedbackAfterStatus();
    }

    @GetMapping("/countFeedbackOngoingStatus")
    private Long countFeedbackOngoingStatus(){
        return feedbackService.countFeedbackOngoingStatus();
    }


    @GetMapping("/getrep")
    private List<Feedback> getrep(){
        return feedbackRepository.findAll();
    }


    @RequestMapping(value = "/reportServices", produces = MediaType.APPLICATION_PDF_VALUE)
    public void gen(HttpServletResponse response) throws JRException, IOException {

        //Feedback feedback = new Feedback();

        //List<Feedback> reports = feedbackService.getAllFeedback();
        List<Feedback> reports = feedbackRepository.findAll();

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