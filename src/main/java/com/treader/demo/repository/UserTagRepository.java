package com.treader.demo.repository;

import com.treader.demo.model.UserTag;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UserTagRepository extends JpaRepository<UserTag, Integer> {
    List<UserTag> findByUserId(Integer userId);
}
