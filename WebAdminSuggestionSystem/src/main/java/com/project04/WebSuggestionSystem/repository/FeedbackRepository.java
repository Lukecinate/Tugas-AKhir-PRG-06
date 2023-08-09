package com.project04.WebSuggestionSystem.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.stereotype.Repository;

import com.project04.WebSuggestionSystem.model.Feedback;

@Repository
@EnableJpaRepositories
public interface FeedbackRepository extends JpaRepository<Feedback, Integer> {

    List<Feedback> findAll();

    List<Feedback> findAllByPostStatusIsNullAndPreStatusIsNotNull();

    @Query("SELECT f  FROM Feedback f where f.preStatus='Ongoing'")
    List<Feedback>findReport();

    @Query("SELECT COUNT(f) FROM Feedback f WHERE f.preStatus='Before' AND f.postStatus=NULL")
    Long countFeedbackBeforeStatus();

    @Query("SELECT COUNT(f) FROM Feedback f WHERE f.postStatus='Ongoing'")
    Long countFeedbackOngoingStatus();

    @Query("SELECT COUNT(f) FROM Feedback f WHERE f.postStatus='After'")
    Long countFeedbackAfterStatus();

    @Query("SELECT f FROM Feedback f WHERE f.postStatus LIKE %:keyword%")
    List<Feedback> findAllByPostStatus(String keyword);

    @Query("SELECT f FROM Feedback f WHERE CONCAT(f.postStatus, '', f.preStatus, '', f.workerName, '', f.suggestName, '', f.deadline, '', f.title) LIKE %:keyword%")
    List<Feedback> findAllByKeywords(String keyword);
}
