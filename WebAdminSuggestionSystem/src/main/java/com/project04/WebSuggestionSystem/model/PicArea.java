package com.project04.WebSuggestionSystem.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

import javax.persistence.*;

@Data
@Entity
@Table(name = "pic_area")
@JsonIgnoreProperties({"hibernateLazyInitializer"})
public class PicArea {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    public int id;

    @Column(name = "area")
    public String area;

    @Column(name = "pic_name")
    public String picName;

    public PicArea() {

    }

    public PicArea(int id, String area, String picName) {
        this.id = id;
        this.area = area;
        this.picName = picName;
    }
}
