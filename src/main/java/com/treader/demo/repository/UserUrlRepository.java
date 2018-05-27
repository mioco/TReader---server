package com.treader.demo.repository;

import com.treader.demo.model.UserUrl;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UserUrlRepository extends JpaRepository<UserUrl, Integer> {
    List<UserUrl> findByUserId(Integer userId);
    List<UserUrl> findByUrlId(Integer urlId);
    UserUrl findByUserIdAndUrlId(Integer userId, Integer urlId);
}
