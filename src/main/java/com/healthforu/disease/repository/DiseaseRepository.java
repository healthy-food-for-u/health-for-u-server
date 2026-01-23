package com.healthforu.disease.repository;

import com.healthforu.disease.domain.Disease;
import com.healthforu.disease.dto.DiseaseResponse;
import org.bson.types.ObjectId;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;
import java.util.Optional;

public interface DiseaseRepository extends MongoRepository<Disease, ObjectId> {
    List<Disease> findByCategoryId(ObjectId categoryId);

    List<Disease> findByCategoryIdAndDiseaseNameContaining(ObjectId categoryId, String keyword);
}
