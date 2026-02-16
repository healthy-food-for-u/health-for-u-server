package com.healthforu.config;

import com.healthforu.disease.domain.Disease;
import com.healthforu.disease.domain.DiseaseDocument;
import com.healthforu.disease.repository.DiseaseRepository;
import com.healthforu.disease.repository.elasticsearch.DiseaseSearchRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.util.List;


@Slf4j
@Component
@RequiredArgsConstructor
public class DiseaseDataInitializer implements CommandLineRunner {

    private final DiseaseRepository mongodiseaseRepository;
    private final DiseaseSearchRepository diseaseSearchRepository;


    @Override
    public void run(String... args) throws Exception {
        if(diseaseSearchRepository.count() > 0){
            log.info("Elasticsearch에 이미 데이터가 존재합니다. 마이그레이션을 건너뜁니다.");
            return;
        }

        log.info("MongoDB에서 Elasticsearch로 데이터 마이그레이션을 시작합니다.");

        List<Disease> allDiseases = mongodiseaseRepository.findAll();

        List<DiseaseDocument> documents = allDiseases.stream()
                .map(DiseaseDocument::from)
                .toList();

        diseaseSearchRepository.saveAll(documents);

    }
}
