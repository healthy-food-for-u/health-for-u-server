package com.healthforu.disease.service.impl;

import com.healthforu.category.dto.CategoryResponse;
import com.healthforu.category.dto.CategoryWithDiseasesResponse;
import com.healthforu.category.service.CategoryService;
import com.healthforu.common.exception.custom.DiseaseNotFoundException;
import com.healthforu.disease.domain.Disease;
import com.healthforu.disease.dto.DiseaseResponse;
import com.healthforu.disease.repository.DiseaseRepository;
import com.healthforu.disease.service.DiseaseService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.bson.types.ObjectId;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional(readOnly = true)
public class DiseaseServiceImpl implements DiseaseService {

    private final DiseaseRepository diseaseRepository;
    private final CategoryService categoryService;

    /**
     * 개별 질병 상세 정보
     *
     * @param diseaseId
     */
    @Override
    public DiseaseResponse getDisease(ObjectId diseaseId) {
        Disease disease = diseaseRepository.findById(diseaseId)
                .orElseThrow(() ->  new DiseaseNotFoundException());

        return new DiseaseResponse(
                disease.getId().toHexString(),
                disease.getDiseaseName(),
                disease.getCaution(),
                disease.getFoodCategory(),
                disease.getCategoryId()
        );
    }

    /**
     * 특정 질병 검색
     *
     * @param keyword
     */
    @Override
    public List<CategoryWithDiseasesResponse> searchDiseases(String keyword) {
        List<CategoryResponse> categories = categoryService.getAllCategories();

        log.info("조회된 카테고리 수: {}", categories.size());



        return categories.stream().map(cat -> {
            List<DiseaseResponse> diseases;

                    log.info("카테고리 ID: {} ({})", cat.id(), cat.categoryName());

            if(keyword == null || keyword.isBlank()){
                diseases = diseaseRepository.findByCategoryId(cat.id())
                        .stream().map(DiseaseResponse::from).toList();

                log.info("찾은 질병 수: {}", diseases.size());
            } else{
                diseases = diseaseRepository.findByCategoryIdAndDiseaseNameContaining(cat.id(), keyword)
                        .stream().map(DiseaseResponse::from).toList();
            }

            return new CategoryWithDiseasesResponse(
                    cat.id().toHexString(),
                    cat.categoryName(),
                    cat.iconUrl(),
                    diseases
            );
        })
        .filter(res -> keyword == null || !res.diseases().isEmpty())
        .toList();
    }
}
