package com.project04.WebSuggestionSystem.repository;

import com.project04.WebSuggestionSystem.model.Feedback;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@EnableJpaRepositories
public interface FeedbackRepository extends JpaRepository<Feedback, Integer> {

    List<Feedback> findAll();
    
    @Query("SELECT f FROM Feedback f ORDER BY f.createdDate ASC")
    List<Feedback> findAllByPreStatusOrderByCreatedDate();

    List<Feedback> findAllByPostStatusIsNull();
    
    @Query("SELECT f FROM Feedback f WHERE f.postStatus LIKE %:keyword%")
    List<Feedback> findAllByPostStatus(String keyword);

    @Query("SELECT f FROM Feedback f WHERE CONCAT(f.postStatus, '', f.preStatus, '', f.workerName, '', f.suggestName, '', f.deadline, '', f.title) LIKE %:keyword%")
    List<Feedback> findAllByKeywords(String keyword);
}
