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

    /**
     * 키워드 및 카테고리별 질병 정보를 Elasticsearch를 통해 검색합니다.
     * <p>
     * 모든 카테고리를 순회하며 각 카테고리에 속한 질병 중 키워드와 일치하는 항목을 조회합니다.
     * 검색어가 없는 경우 해당 카테고리의 모든 질병을, 검색어가 있는 경우 검색어와 매칭되는 질병만 반환합니다.
     * </p>
     *
     * @param keyword 사용자가 입력한 검색어 (null 또는 빈 문자열일 경우 전체 조회)
     * @return 카테고리 정보와 해당 카테고리에 속한 질병 리스트(DiseaseResponse)가 포함된 응답 객체 리스트.
     * 검색어가 존재할 경우, 결과가 비어있는 카테고리는 결과 목록에서 제외됩니다.
     */
    @Override
    public List<CategoryWithDiseasesResponse> searchDiseases(String keyword) {
        List<CategoryResponse> categories = categoryService.getAllCategories();

        return categories.stream().map(cat -> {
                    List<DiseaseResponse> diseases;

                    NativeQuery query = NativeQuery.builder()
                            // 특정 카테고리 ID와 일치하는 문서만 필터링 (Term Query)
                            .withQuery(q -> q.bool(b -> {
                                b.must(m -> m.term(t -> t.field("categoryId").value(cat.id().toHexString())));

                                // 키워드 유무에 따른 동적 쿼리 처리
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
