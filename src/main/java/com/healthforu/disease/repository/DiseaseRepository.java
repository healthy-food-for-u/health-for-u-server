package com.healthforu.disease.repository;

import com.healthforu.disease.domain.Disease;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

public interface DiseaseRepository extends MongoRepository<Disease, String> {
    List<Disease> findDiseasesByCategoryId(String categoryId);

    List<Disease> findByCategoryIdAndDiseaseNameContaining(String categoryId, String keyword);
}
