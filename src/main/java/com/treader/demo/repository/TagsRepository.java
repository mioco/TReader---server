package com.treader.demo.repository;

import org.springframework.data.repository.CrudRepository;

import com.treader.demo.model.Tags;

// This will be AUTO IMPLEMENTED by Spring into a Bean called userRepository
// CRUD refers Create, Read, Update, Delete

public interface TagsRepository extends CrudRepository<Tags, Long> {
    Tags findByTag(String tag);
}