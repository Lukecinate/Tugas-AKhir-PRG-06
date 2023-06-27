package com.project04.WebSuggestionSystem.repository;

import com.project04.WebSuggestionSystem.model.PicArea;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PicAreaRepository extends JpaRepository<PicArea, Integer> {
    List<PicArea> findAll();
    @Query("SELECT COUNT(p) FROM PicArea p")
    Long countData();
}
