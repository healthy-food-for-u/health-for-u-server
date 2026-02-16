package com.healthforu.recipe.service.elasticsearch.impl;

import com.healthforu.disease.dto.DiseaseResponse;
import com.healthforu.disease.service.DiseaseService;
import com.healthforu.recipe.domain.RecipeDocument;
import com.healthforu.recipe.dto.RecipeResponse;
import com.healthforu.recipe.service.elasticsearch.RecipeSearchService;
import lombok.RequiredArgsConstructor;
import org.bson.types.ObjectId;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.elasticsearch.client.elc.NativeQuery;
import org.springframework.data.elasticsearch.core.ElasticsearchOperations;
import org.springframework.data.elasticsearch.core.SearchHits;
import org.springframework.stereotype.Service;
import com.healthforu.common.exception.custom.DiseaseNotFoundException;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;


@Service
@RequiredArgsConstructor
public class RecipeSearchServiceImpl implements RecipeSearchService {

    private final ElasticsearchOperations elasticsearchOperations;
    private final DiseaseService diseaseService;

    /**
     * 키워드와 질환 정보를 바탕으로 최적화된 레시피 목록을 검색합니다.
     * <p>
         * 1. 검색 키워드가 없는 경우: 특정 질환의 주의 식품(caution)이 포함된 레시피를
         * 전체 목록에서 제외하여 안전한 레시피만 반환합니다.
         * 2. 검색 키워드가 있는 경우: 이름(가중치 5.0)과 재료(가중치 1.0)를 기준으로 검색하며,
         * 주의 식품 포함 여부와 상관없이 연관된 레시피를 모두 반환하되 주의 표시(isCaution)를 추가합니다.
     * </p>
     *
     * @param keyword        사용자가 입력한 검색어 (null 가능)
     * @param diseaseId      사용자가 선택한 질환 식별자 (주의 식품 조회용)
     * @param pageable       페이지네이션 정보 (페이지 번호, 사이즈 등)
     * @return               검색 결과 및 주의 식품 포함 여부가 담긴 레시피 페이지 객체
     * @throws DiseaseNotFoundException 유효하지 않은 질병 ID일 경우 발생
     */
    @Override
    public Page<RecipeResponse> searchRecipes(String keyword, ObjectId diseaseId, Pageable pageable) {

        DiseaseResponse diseaseResponse = diseaseService.getDisease(diseaseId);
        String caution = (diseaseResponse != null) ? diseaseResponse.caution() : "";

        List<String> cautionList = (caution != null && !caution.isBlank())
                ? Arrays.stream(caution.split(","))
                .map(String::trim)
                .filter(s -> !s.isEmpty())
                .toList()
                : Collections.emptyList();

        // 키워드가 없으면 전체 조회, 있으면 가중치 검색
        NativeQuery query = NativeQuery.builder()
                .withQuery(q -> q.bool(b -> {
                    if (keyword == null || keyword.isBlank()) {
                        b.must(m -> m.matchAll(ma -> ma));

                        for (String item : cautionList) {
                            b.mustNot(mn -> mn.match(m -> m.field("ingredients").query(item)));
                            b.mustNot(mn -> mn.match(m -> m.field("recipeName").query(item)));
                        }
                    } else {
                        b.should(s -> s.match(m -> m.field("recipeName").query(keyword).boost(5.0f)));
                        b.should(s -> s.match(m -> m.field("ingredients").query(keyword).boost(1.0f)));
                        b.minimumShouldMatch("1");
                    }
                    return b;
                }))
                .withPageable(pageable)
                .build();

        SearchHits<RecipeDocument> searchHits = elasticsearchOperations.search(query, RecipeDocument.class);

        // 레시피 데이터 포장
        List<RecipeResponse> content = searchHits.getSearchHits().stream()
                .map(hit -> {
                    RecipeDocument doc = hit.getContent();

                    boolean isCaution = cautionList.stream().anyMatch(c ->
                            doc.getRecipeName().contains(c) || doc.getIngredients().contains(c));

                    return new RecipeResponse(
                            doc.getId(),
                            doc.getRecipeName(),
                            doc.getIngredients(),
                            null,
                            doc.getRecipeThumbnail(),
                            isCaution,
                            false
                    );
                })
                .toList();

        return new PageImpl<>(content, pageable, searchHits.getTotalHits());
    }

}
