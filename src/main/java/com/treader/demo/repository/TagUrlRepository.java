package com.treader.demo.repository;

import com.treader.demo.model.TagUrl;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TagUrlRepository extends JpaRepository<TagUrl, Integer> {
    TagUrl findByTagIdAndUrlId(Integer tagId, Integer urlId);
    List<TagUrl> findByUrlId(Integer urlId);
}
