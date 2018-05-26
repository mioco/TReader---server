package com.treader.demo.repository;

import com.treader.demo.model.WebpageTag;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface WebpageTagRepository extends JpaRepository<WebpageTag, Integer> {
    WebpageTag findByWebpageIdAndTagId(Integer webpageId, Integer tagId);
}
