package com.treader.demo.repository;

import org.springframework.data.repository.CrudRepository;
import com.treader.demo.model.User_url;

// This will be AUTO IMPLEMENTED by Spring into a Bean called userRepository
// CRUD refers Create, Read, Update, Delete

public interface User_urlRepository extends CrudRepository<User_url, String> {
    User_url findByUserId(long id);
    User_url findByUrlId(long id);
}
