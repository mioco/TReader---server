package com.treader.demo.repository;

import com.treader.demo.model.UserTag;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface UserTagRepository extends JpaRepository<UserTag, Integer> {
    List<UserTag> findByUserId(Integer userId);
}
