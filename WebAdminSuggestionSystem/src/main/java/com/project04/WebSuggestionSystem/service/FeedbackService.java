package com.project04.WebSuggestionSystem.service;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.project04.WebSuggestionSystem.model.Feedback;
import com.project04.WebSuggestionSystem.repository.FeedbackRepository;

@Service
public class FeedbackService {
    @Autowired
    FeedbackRepository feedbackRepository;


    public Feedback get(int id){
        Feedback res = feedbackRepository.findById(id).orElse(null);

        if (res == null) return null;

        return res;
    }

    public Feedback save(Feedback feedback){
        Feedback res = feedbackRepository.save(feedback);

        if(res == null) return null;
        res.setModifiedDate(LocalDateTime.now());

        return res;
    }

    public List<Feedback> getAllFeedback(){
        List<Feedback> res = feedbackRepository.findAll();
        if (res.isEmpty())
            return null;

        return res;
    }

    public List<Feedback> getReport(){
        List<Feedback>rep = feedbackRepository.findReport();
        if(rep.isEmpty())
            return null;

        return rep;
    }

    public List<Feedback> getAllBeforeFeedback(){
        List<Feedback> res = feedbackRepository.findAllByPostStatusIsNullAndPreStatusIsNotNull();

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

        if(res == null) return null;

        res.setPostPhoto(feedback.getPostPhoto());
        res.setPostStatus(feedback.getPostStatus());
        res.setWorkerName(feedback.getWorkerName());

        return save(res);
    }

    public Feedback doAttempt(int id, String postStatus){
        Feedback res = get(id);

        if(res == null) return null;

        res.setPostStatus(postStatus);

        return save(res);
    }

    public List<Feedback> getAllByKeywords(String keywords){
        return feedbackRepository.findAllByKeywords(keywords);
    }

    public Long countFeedbackBeforeStatus(){
        return feedbackRepository.countFeedbackBeforeStatus();
    }

    public Long countFeedbackAfterStatus(){
        return feedbackRepository.countFeedbackAfterStatus();
    }

    public Long countFeedbackOngoingStatus(){
        return feedbackRepository.countFeedbackOngoingStatus();
    }
}
