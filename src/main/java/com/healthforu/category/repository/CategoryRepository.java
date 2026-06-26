package com.healthforu.category.repository;

import com.healthforu.category.domain.Category;
import org.bson.types.ObjectId;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;


public interface CategoryRepository extends MongoRepository<Category, ObjectId> {

    List<Category> findAll(Sort sort);

}
