package com.project04.WebSuggestionSystem.controller;

import com.project04.WebSuggestionSystem.model.Feedback;
import com.project04.WebSuggestionSystem.model.PicArea;
import com.project04.WebSuggestionSystem.model.Result;
import com.project04.WebSuggestionSystem.service.FeedbackService;
import com.project04.WebSuggestionSystem.service.PicAreaService;

import org.apache.commons.io.FilenameUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.ui.ModelMap;
import org.springframework.util.MultiValueMap;
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
import org.springframework.web.multipart.MultipartHttpServletRequest;

import javax.servlet.http.HttpServletResponse;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.UUID;

@RestController
public class FeedbackController {
    private static final String UPLOAD_DIR = "uploads/";
    private String filePathToString;
    
    @Autowired
    FeedbackService feedbackService;

    @Autowired
    PicAreaService picAreaService;

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
        // PicArea area = picAreaService.getPicArea(feedbackParam.getAreaId());
        feedbackParam.setPrePhoto(getFilePath());
        // feedbackParam.setArea_id(area);
        return feedbackService.save(feedbackParam);
    }

    @PutMapping("/updateFeedback")
    public Feedback updateFeedback(HttpServletResponse response, @RequestParam("id") int id, @RequestBody Feedback feedbackParam){
        return feedbackService.update(id, feedbackParam);
    } 


    @PostMapping("/upload")
    public ResponseEntity<String> uploadImage(ModelMap m,  MultipartHttpServletRequest request, @RequestParam("area_id") int area_id, @RequestParam("created_date") @DateTimeFormat(pattern = "dd-MM-yyyy HH:mm:ss") LocalDateTime created_date, @RequestParam("deadline") @DateTimeFormat(pattern = "dd-MM-yyyy HH:mm:ss") LocalDateTime deadline, @RequestParam("pre_status") String pre_status, @RequestParam("suggest_name") String suggest_name, @RequestParam("suggestion") String suggestion, @RequestParam("title") String title) {
        
        MultiValueMap<String, MultipartFile> multiValueMap = request.getMultiFileMap();
        List<MultipartFile> files = multiValueMap.isEmpty() ? null : multiValueMap.values().iterator().next();
        MultipartFile file = files != null && !files.isEmpty() ? files.get(0) : null;

        if(file.isEmpty())
            return ResponseEntity.badRequest()
                .body("No image file is uploaded");
        

        try {
            // Create the upload directory if it doesn't exist
            createUploadDirectoryIfNotExists();

            // Save the uploaded file to the upload directory
            String fileName = "SSys_" + LocalDate.now().toString() + "_" + System.currentTimeMillis() + "_" + file.getOriginalFilename();
            Path filePath = Path.of(UPLOAD_DIR + fileName);
            setFilePath(filePath.toString());
            
            Feedback feedback = new Feedback();
            PicArea area = picAreaService.getPicArea(area_id);

            feedback.setArea_id(area);
            feedback.setCreatedDate(created_date);
            feedback.setDeadline(deadline);
            feedback.setPrePhoto(getFilePath());
            feedback.setPreStatus(pre_status);
            feedback.setSuggestName(suggest_name);
            feedback.setSuggestion(suggestion);
            feedback.setTitle(title);
            feedbackService.save(feedback);
            
            Files.copy(file.getInputStream(), filePath, StandardCopyOption.REPLACE_EXISTING);
            
            // Return a success message
            return ResponseEntity.ok("File uploaded successfully: " + getFilePath() + "/" + fileName);
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
