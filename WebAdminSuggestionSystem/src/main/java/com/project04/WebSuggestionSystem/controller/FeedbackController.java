package com.project04.WebSuggestionSystem.controller;

import com.project04.WebSuggestionSystem.model.Feedback;
import com.project04.WebSuggestionSystem.service.FeedbackService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.ui.ModelMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.UUID;

@RestController
public class FeedbackController {
    @Autowired
    FeedbackService feedbackService;

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
    public ResponseEntity<String> savefeedback(ModelMap m,  MultipartHttpServletRequest request, @RequestParam("area_id") int area_id, @RequestParam("created_date") @DateTimeFormat(pattern = "dd-MM-yyyy HH:mm:ss") LocalDateTime created_date, @RequestParam("deadline") @DateTimeFormat(pattern = "dd-MM-yyyy HH:mm:ss") LocalDateTime deadline, @RequestParam("pre_status") String pre_status, @RequestParam("suggest_name") String suggest_name, @RequestParam("suggestion") String suggestion, @RequestParam("title") String title) {

        MultiValueMap<String, MultipartFile> multiFileMap = request.getMultiFileMap();
        List<MultipartFile> files = multiFileMap.isEmpty() ? null : multiFileMap.values().iterator().next();
        MultipartFile file = files != null && !files.isEmpty() ? files.get(0) : null;

        // Example of Set Paths
        //Path resourceDirectory = Paths.get("target","classes","static","upload");
        //Path resourceDirectory = Paths.get("src","main","resources","static");

        Path resourceDirectory = Paths.get("src", "resources", "uploads");
        String absolutePath = resourceDirectory.toFile().getAbsolutePath();

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");


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
            feedback.setDeadline(deadline);//
            feedback.setPrePhoto(fileName);
            feedback.setPreStatus(pre_status);
            feedback.setSuggestName(suggest_name);
            feedback.setSuggestion(suggestion);//
            feedback.setTitle(title);//
            feedback.setModifiedDate(LocalDateTime.parse(LocalDateTime.now().format(formatter)));
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

    private String getFileExtension(String filename) {
        if (filename != null && !filename.isEmpty()) {
            int dotIndex = filename.lastIndexOf('.');
            if (dotIndex >= 0 && dotIndex < filename.length() - 1) {
                return filename.substring(dotIndex + 1);
            }
        }
        return "";
    }

}