package com.project04.WebSuggestionSystem.service;

import com.project04.WebSuggestionSystem.model.Feedback;
import com.project04.WebSuggestionSystem.repository.FeedbackRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class FeedbackService {
    @Autowired
    FeedbackRepository feedbackRepository;


    public Feedback save(Feedback feedback){
        Feedback res = feedbackRepository.save(feedback);
        if(res == null){
            return null;
        }

        return res;
    }

    public List<Feedback> getAllFeedback(){
        List<Feedback> res = feedbackRepository.findAll();
        if (res.isEmpty())
            return null;

        return res;
    }

    public List<Feedback> getAllBeforeFeedback(){
        List<Feedback> res = feedbackRepository.findAllByPreStatusOrderByCreatedDate();

        if (res.isEmpty())
            return null;

        return res;
    }

    public List<Feedback> getAllOngoingFeedback(){
        List<Feedback> res = feedbackRepository.findAllByPostStatus("ongoing");

        if (res.isEmpty())
            return null;

        return res;
    }

    public List<Feedback> getAllAfterFeedback(){
        List<Feedback> res = feedbackRepository.findAllByPostStatus("after");

        if (res.isEmpty())
            return null;

        return res;
    }

    public Feedback update(int id, Feedback feedback){
        Feedback res = feedbackRepository.findById(id).orElse(null);
        
        if(res == null){
            return null;
        }

        return save(res);
    }

    public List<Feedback> getAllByKeywords(String keywords){
        return feedbackRepository.findAllByKeywords(keywords);
    }
}
