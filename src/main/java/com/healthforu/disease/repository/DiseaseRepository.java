package com.healthforu.disease.repository;

import com.healthforu.disease.domain.Disease;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface DiseaseRepository extends MongoRepository<Disease, String> {
}
