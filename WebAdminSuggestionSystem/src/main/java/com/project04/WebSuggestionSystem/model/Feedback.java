package com.project04.WebSuggestionSystem.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.Date;

@Data
@Entity
@Table(name = "feedback")
@JsonIgnoreProperties({"hibernateLazyInitializer"})
public class Feedback {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    public int id;

    @ManyToOne
    @JoinColumn(name = "area_id")
    private PicArea area;

    @Column(name = "created_date", nullable = false)
    private LocalDateTime createdDate;

    @Column(name = "deadline", nullable = false)
    private LocalDateTime deadline;

    @Column(name = "post_photo")
    private String postPhoto;

    @Column(name = "pre_photo", nullable = false)
    private String prePhoto;

    @Column(name = "pre_status", nullable = false)
    private String preStatus;

    @Column(name = "post_status")
    private String postStatus;

    @Column(name = "suggest_name", nullable = false)
    private String suggestName;

    @Column(name = "suggestion", nullable = false)
    private String suggestion;

    @Column(nullable = false)
    private String title;

    @Column(name = "worker_name")
    private String workerName;

    @Column(name = "modified_date")
    private LocalDateTime modifiedDate;

}
