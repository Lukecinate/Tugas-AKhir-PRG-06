package com.project04.WebSuggestionSystem.controller;

import com.project04.WebSuggestionSystem.model.Feedback;
import com.project04.WebSuggestionSystem.model.PicArea;
import com.project04.WebSuggestionSystem.model.Result;
import com.project04.WebSuggestionSystem.service.FeedbackService;
import com.project04.WebSuggestionSystem.service.PicAreaService;

import org.apache.commons.io.FilenameUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
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
    public List<Feedback> getAllFeedback() {
        return feedbackService.getAllFeedback();
    }

    @GetMapping("/getAllFeedbackBefore")
    public List<Feedback> getAllFeedbackBefore() {
        return feedbackService.getAllBeforeFeedback();
    }

    @GetMapping("/getAllFeedbackOngoing")
    public List<Feedback> getAllFeedbackOngoing() {
        return feedbackService.getAllOngoingFeedback();
    }

    @GetMapping("/getAllFeedbackAfter")
    public List<Feedback> getAllFeedbackAfter() {
        return feedbackService.getAllAfterFeedback();
    }

    @GetMapping("/getFeebackByKeywords")
    public List<Feedback> getFeebackByKeywords(@RequestParam("keyword") String keyword) {
        return feedbackService.getAllByKeywords(keyword);
    }

    @RequestMapping(value = "/saveFeedback", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public Feedback saveFeedback(HttpServletResponse response, @RequestBody Feedback feedbackParam) {
        // PicArea area = picAreaService.getPicArea(feedbackParam.getAreaId());
        // feedbackParam.setPrePhoto(getFilePath());
        // feedbackParam.setArea_id(area);
        return feedbackService.save(feedbackParam);
    }

    @PutMapping("/updateFeedback")
    public Feedback updateFeedback(HttpServletResponse response, @RequestParam("id") int id,
            @RequestBody Feedback feedbackParam) {
        return feedbackService.update(id, feedbackParam);
    }

    @PostMapping("/savefeedback")
    public ResponseEntity<String> savefeedback(ModelMap m,  MultipartHttpServletRequest request, @RequestParam("area_id") int area_id, @RequestParam("created_date") LocalDateTime created_dateStr, @RequestParam("deadline") LocalDateTime deadlineStr, @RequestParam("pre_status") String pre_status, @RequestParam("suggest_name") String suggest_name, @RequestParam("suggestion") String suggestion, @RequestParam("title") String title) {
        MultiValueMap<String, MultipartFile> multiFileMap = request.getMultiFileMap();
        List<MultipartFile> files = multiFileMap.isEmpty() ? null : multiFileMap.values().iterator().next();
        MultipartFile file = files != null && !files.isEmpty() ? files.get(0) : null;
//
//        Path resourceDirectory = Paths.get("target","classes","static","upload");

        //Path resourceDirectory = Paths.get("src","main","resources","static");
        Path resourceDirectory = Paths.get("target","classes","static","upload");
        String absolutePath = resourceDirectory.toFile().getAbsolutePath();

       // Path resourceDirectory1 = Paths.get("src","main","resources","static");
        //String absolutePath1 = resourceDirectory.toFile().getAbsolutePath();

//        MultiValueMap<String, MultipartFile> multiFileMap1 = request.getMultiFileMap();
//        List<MultipartFile> files1 = multiFileMap1.isEmpty() ? null : multiFileMap1.values().iterator().next();
//        MultipartFile file1 = files1 != null && !files1.isEmpty() ? files1.get(0) : null;

//        Path resourceDirectory = Paths.get("target","classes","static","upload");
//        String absolutePath = resourceDirectory.toFile().getAbsolutePath();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm:ss");
        // Parse the string to LocalDateTime using the formatter
//        LocalDateTime dateTime = LocalDateTime.parse(deadline, formatter);
//        Date date = null;
        //LocalDateTime now = LocalDateTime.now();
//
//        try {
//            date = dateFormat.parse(deadline);
//        } catch (ParseException e) {
//            // Handle parsing exception
//            e.printStackTrace();
//        }


        if (file.isEmpty()) {
            return ResponseEntity.badRequest().body("No photo uploaded.");
        }
        try {

            // Generate a unique file name for the uploaded photo
            String fileName = UUID.randomUUID().toString() + "." + getFileExtension1(file.getOriginalFilename());
            // String fileName1 = UUID.randomUUID().toString() + "." + getFileExtension(file1.getOriginalFilename());
            Feedback feedback = new Feedback();
            // get the PicArea
            PicArea areaId = picAreaService.getPicArea(area_id);

            feedback.setArea_id(areaId);
            feedback.setCreatedDate(created_dateStr);
            feedback.setDeadline(deadlineStr);
            feedback.setPrePhoto(fileName);
            feedback.setPreStatus(pre_status);
            feedback.setSuggestName(suggest_name);
            feedback.setSuggestion(suggestion);//
            feedback.setTitle(title);//
            feedbackService.save(feedback);

            //unit.setFoto(fileName);
            //unit.setPdfshopmanual(fileName1);
            //unitService.saveUnit(unit);

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
    
    private String getFileExtension1(String filename) {
        if (filename != null && !filename.isEmpty()) {
            int dotIndex = filename.lastIndexOf('.');
            if (dotIndex >= 0 && dotIndex < filename.length() - 1) {
                return filename.substring(dotIndex + 1);
            }
        }
        return "";
    }


}
