package com.treader.demo.repository;

import com.treader.demo.model.WebPage;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface WebPageRepository extends JpaRepository<WebPage, Integer> {

    List<WebPage> findByUrlId(Integer urlId);

    WebPage findByUrl(String url);
}
