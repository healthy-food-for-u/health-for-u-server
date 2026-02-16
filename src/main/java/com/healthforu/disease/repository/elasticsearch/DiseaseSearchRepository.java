package com.healthforu.disease.repository.elasticsearch;

import com.healthforu.disease.domain.DiseaseDocument;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

public interface DiseaseSearchRepository extends ElasticsearchRepository<DiseaseDocument, String> {
}
