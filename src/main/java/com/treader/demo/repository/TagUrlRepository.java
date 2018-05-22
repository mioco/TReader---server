package com.treader.demo.repository;

import com.treader.demo.model.TagUrl;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TagUrlRepository extends JpaRepository<TagUrl, Integer> {
}
