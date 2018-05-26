package com.treader.demo.repository;

import com.treader.demo.model.WebPage;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface WebPageRepository extends JpaRepository<WebPage, Integer> {
}
