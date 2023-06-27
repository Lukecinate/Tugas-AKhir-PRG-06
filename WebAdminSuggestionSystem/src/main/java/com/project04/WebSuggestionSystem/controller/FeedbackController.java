package com.project04.WebSuggestionSystem.controller;

import com.project04.WebSuggestionSystem.model.Feedback;
import com.project04.WebSuggestionSystem.model.Result;
import com.project04.WebSuggestionSystem.service.FeedbackService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletResponse;
import java.util.List;

@RestController
public class FeedbackController {
    @Autowired
    FeedbackService feedbackService;
    private boolean isSucceed = true;

    @GetMapping("/getAllFeedback")
    public List<Feedback> getAllFeeback(){
        return feedbackService.getAllFeedback();
    }

    @GetMapping("/getAllFeedbackBefore")
    public List<Feedback> getAllFeebackBefor(){
        return feedbackService.getAllBeforeFeedback();
    }

    @GetMapping("/getAllFeedbackOngoing")
    public List<Feedback> getAllFeebackOngoing(){
        return feedbackService.getAllOngoingFeedback();
    }

    @GetMapping("/getAllFeedbackAfter")
    public List<Feedback> getAllFeebackAfter(){
        return feedbackService.getAllAfterFeedback();
    }

    @PostMapping("/saveFeedback")
    public Object saveFeedback(HttpServletResponse response, @RequestBody Feedback feedbackParam){
        isSucceed = feedbackService.save(feedbackParam);

        if(isSucceed){
            return new Result(200, "Save successfully!");
        }else {
            return new Result(500, "Failure : " + response.toString());
        }
    }
}
