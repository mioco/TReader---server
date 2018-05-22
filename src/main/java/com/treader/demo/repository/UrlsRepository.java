package com.treader.demo.repository;

import org.springframework.data.repository.CrudRepository;

import com.treader.demo.model.Urls;

// This will be AUTO IMPLEMENTED by Spring into a Bean called userRepository
// CRUD refers Create, Read, Update, Delete

public interface UrlsRepository extends CrudRepository<Urls, Long> {
}