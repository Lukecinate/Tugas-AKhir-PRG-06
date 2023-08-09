package com.project04.WebSuggestionSystem.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import org.hibernate.annotations.Immutable;

import javax.persistence.*;
import java.time.LocalDateTime;


@Entity
@Immutable
public class Report {
//        @Id
//        @Column(name = "id")
//        public int id;


        @Column(name = "area")
        private String area_id;

        @Column(name = "post_photo")
        private String postPhoto;

        @Column(name = "pre_photo")
        private String prePhoto;

        @Column(name = "suggestion", nullable = false)
        private String suggestion;

        @Id

        @Column(name = "title", nullable = false)
        private String title;

        @Column(name = "worker_name")
        private String workerName;

        @Column(name = "modified_date")
        private LocalDateTime modifiedDate;

        public Report() {
        }

        public Report( String area_id, String postPhoto, String prePhoto, String suggestion, String title, String workerName, LocalDateTime modifiedDate) {
               // this.id = id;
                this.area_id = area_id;
                this.postPhoto = postPhoto;
                this.prePhoto = prePhoto;
                this.suggestion = suggestion;
                this.title = title;
                this.workerName = workerName;
                this.modifiedDate = modifiedDate;
        }



        public String getArea_id() {
                return area_id;
        }

        public void setArea_id(String area_id) {
                this.area_id = area_id;
        }

        public String getPostPhoto() {
                return postPhoto;
        }

        public void setPostPhoto(String postPhoto) {
                this.postPhoto = postPhoto;
        }

        public String getPrePhoto() {
                return prePhoto;
        }

        public void setPrePhoto(String prePhoto) {
                this.prePhoto = prePhoto;
        }

        public String getSuggestion() {
                return suggestion;
        }

        public void setSuggestion(String suggestion) {
                this.suggestion = suggestion;
        }

        public String getTitle() {
                return title;
        }

        public void setTitle(String title) {
                this.title = title;
        }

        public String getWorkerName() {
                return workerName;
        }

        public void setWorkerName(String workerName) {
                this.workerName = workerName;
        }

        public LocalDateTime getModifiedDate() {
                return modifiedDate;
        }

        public void setModifiedDate(LocalDateTime modifiedDate) {
                this.modifiedDate = modifiedDate;
        }
}
