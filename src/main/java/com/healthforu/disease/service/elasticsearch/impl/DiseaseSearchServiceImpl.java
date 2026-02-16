package com.healthforu.disease.service.elasticsearch.impl;

import com.healthforu.category.dto.CategoryResponse;
import com.healthforu.category.dto.CategoryWithDiseasesResponse;
import com.healthforu.category.service.CategoryService;
import com.healthforu.disease.domain.DiseaseDocument;
import com.healthforu.disease.dto.DiseaseResponse;
import com.healthforu.disease.service.elasticsearch.DiseaseSearchService;
import lombok.RequiredArgsConstructor;
import org.bson.types.ObjectId;
import org.springframework.data.elasticsearch.client.elc.NativeQuery;
import org.springframework.data.elasticsearch.core.ElasticsearchOperations;
import org.springframework.data.elasticsearch.core.SearchHits;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class DiseaseSearchServiceImpl implements DiseaseSearchService {

    private final ElasticsearchOperations elasticsearchOperations;
    private final CategoryService categoryService;

    @Override
    public List<CategoryWithDiseasesResponse> searchDiseases(String keyword) {
        List<CategoryResponse> categories = categoryService.getAllCategories();

        return categories.stream().map(cat -> {
                    List<DiseaseResponse> diseases;

                    NativeQuery query = NativeQuery.builder()
                            .withQuery(q -> q.bool(b -> {
                                b.must(m -> m.term(t -> t.field("categoryId").value(cat.id().toHexString())));

                                if (keyword == null || keyword.isBlank()) {
                                    b.must(m -> m.matchAll(ma -> ma));
                                } else {
                                    b.must(m -> m.match(ma -> ma.field("diseaseName").query(keyword)));
                                }
                                return b;
                            }))
                            .build();

                    SearchHits<DiseaseDocument> searchHits = elasticsearchOperations.search(query, DiseaseDocument.class);

                    diseases = searchHits.getSearchHits().stream()
                            .map(hit -> {
                                DiseaseDocument doc = hit.getContent();
                                return new DiseaseResponse(
                                        doc.getId(),
                                        doc.getDiseaseName(),
                                        doc.getCaution(),
                                        null,
                                        new ObjectId(doc.getCategoryId())
                                );
                            })
                            .toList();

                    return new CategoryWithDiseasesResponse(
                            cat.id().toHexString(),
                            cat.categoryName(),
                            cat.iconUrl(),
                            diseases
                    );
                })
                //  검색어가 있을 때는 결과가 비어있는 카테고리는 제외
                .filter(res -> keyword == null || !res.diseases().isEmpty())
                .toList();
    }
}
