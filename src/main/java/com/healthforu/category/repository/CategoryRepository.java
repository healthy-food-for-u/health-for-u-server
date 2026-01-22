package com.healthforu.category.repository;

import com.healthforu.category.domain.Category;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;
import java.util.Optional;


public interface CategoryRepository extends MongoRepository<Category, String> {

    List<Category> findAll(Sort sort);

    Optional<Category> findByCategorySlug(String slug);
}
