package com.healthforu.recipe.service.elasticsearch.impl;

import co.elastic.apm.api.CaptureSpan;
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
     * 검색 키워드와 프론트엔드로부터 전달받은 주의 식품(caution) 리스트를 바탕으로 최적화된 레시피 목록을 검색합니다.
     * <p>
     * 1. 검색 키워드가 없는 경우 (전체 조회): 전달받은 주의 식품 목록(caution)이 이름이나 재료에 포함된 레시피를
     *    Elasticsearch 쿼리 조건에서 원천 제외(mustNot)하여 안전한 레시피만 반환합니다.
     * 2. 검색 키워드가 있는 경우 (키워드 검색): 이름(가중치 5.0)과 재료(가중치 1.0)를 기준으로 쿼리하되,
     *    주의 식품 포함 여부와 상관없이 검색어와 연관된 레시피를 모두 반환하며, 결과에서 해당 주의 식품 포함 여부(isCaution)를 true로 표시합니다.
     * </p>
     *
     * @param keyword        사용자가 입력한 레시피 검색어 (null 또는 공백 가능)
     * @param diseaseId      사용자가 선택한 질환 식별자 (필요 시 확장용 ObjectId)
     * @param caution        프론트엔드에서 콤마(,)로 구분하여 전달한 주의 식품 명칭 문자열 (null 또는 공백 가능)
     * @param pageable       페이지네이션 정보 (페이지 번호, 페이지 크기, 정렬 등)
     * @return               주의 식품 필터링 또는 주의 표시(isCaution)가 반영된 레시피 페이지(Page) 객체
     */
    @Override
    @CaptureSpan(value = "Elasticsearch-Recipe-Query", type = "db", subtype = "elasticsearch", action = "query") // apm 에이전트가 시각화할 수 있도록 함
    public Page<RecipeResponse> searchRecipes(String keyword, ObjectId diseaseId, String caution, Pageable pageable) {
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

        // 키워드 유무 확인
        boolean hasKeyword = (keyword != null && !keyword.isBlank());

        // 레시피 데이터 포장
        List<RecipeResponse> content = searchHits.getSearchHits().stream()
                .map(hit -> {
                    RecipeDocument doc = hit.getContent();

                    // 키워드가 있을 때만 검색(검색어가 있을 때는, 주의 식품 포함 여부와 상관없이 연관된 레시피를 모두 반환해야 함)
                    boolean isCaution = hasKeyword && cautionList.stream().anyMatch(c ->
                            doc.getRecipeName().contains(c) || doc.getIngredients().contains(c));

                    return new RecipeResponse(
                            doc.getId(),
                            doc.getRecipeName(),
                            null,
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
