package com.project04.WebSuggestionSystem.controller;

import com.project04.WebSuggestionSystem.model.Feedback;
import com.project04.WebSuggestionSystem.model.Result;
import com.project04.WebSuggestionSystem.service.FeedbackService;

import org.apache.commons.io.FilenameUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@RestController
public class FeedbackController {
    private static final String UPLOAD_DIR = "uploads/";
    private String filePathToString;
    
    @Autowired
    FeedbackService feedbackService;

    @GetMapping("/getAllFeedback")
    public List<Feedback> getAllFeedback(){
        return feedbackService.getAllFeedback();
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

    @RequestMapping(value = "/saveFeedback", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public Feedback saveFeedback(HttpServletResponse response, @RequestBody Feedback feedbackParam){
        feedbackParam.setPrePhoto(getFilePath());
        return feedbackService.save(feedbackParam);
    }



    @PutMapping("/updateFeedback")
    public Feedback updateFeedback(HttpServletResponse response, @RequestParam("id") int id, @RequestBody Feedback feedbackParam){
        return feedbackService.update(id, feedbackParam);
    } 


    @PostMapping("/upload")
    public ResponseEntity<String> uploadImage(@RequestParam("file") MultipartFile file) {
        try {
            // Create the upload directory if it doesn't exist
            createUploadDirectoryIfNotExists();

            // Save the uploaded file to the upload directory
            String fileName = "SSys_" + LocalDate.now().toString() + "_" + System.currentTimeMillis() + "_" + file.getOriginalFilename();
            Path filePath = Path.of(UPLOAD_DIR + fileName);
            setFilePath(filePath.toString());
            System.out.println(getFilePath());
            Files.copy(file.getInputStream(), filePath, StandardCopyOption.REPLACE_EXISTING);

            // Return a success message
            return ResponseEntity.ok("File uploaded successfully: " + fileName);
        } catch (IOException e) {
            e.printStackTrace();
            // Return an error message
            return ResponseEntity.status(500).body("Failed to upload the file: " + e.getMessage());
        }
    }

    private void setFilePath(String path){
        this.filePathToString = path;
    }

    private String getFilePath(){
        return this.filePathToString;
    }

    private void createUploadDirectoryIfNotExists() throws IOException {
        Path uploadPath = Path.of(UPLOAD_DIR);
        if (!Files.exists(uploadPath)) {
            Files.createDirectory(uploadPath);
        }
    }

}
