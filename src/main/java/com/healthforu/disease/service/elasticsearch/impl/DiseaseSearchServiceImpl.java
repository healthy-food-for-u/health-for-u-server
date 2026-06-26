package com.healthforu.disease.service.elasticsearch.impl;

import co.elastic.apm.api.CaptureSpan;
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
import org.springframework.data.elasticsearch.core.SearchHit;
import org.springframework.data.elasticsearch.core.SearchHits;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.Map;

import java.util.List;
import java.util.stream.Collectors;

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
    @CaptureSpan(value = "Elasticsearch-Disease-Query", type = "db", subtype = "elasticsearch", action = "query") // apm 에이전트가 시각화할 수 있도록 함
    public List<CategoryWithDiseasesResponse> searchDiseases(String keyword) {
        // 일단 키워드 유무에 따라 한 번에 들고올 엘라스틱서치 쿼리를 결정
        NativeQuery query = NativeQuery.builder() // 복잡한 객체의 생성과 조립을 별도의 빌더 객체로 캡슐화
        // 특정 카테고리 ID와 일치하는 문서만 필터링 (Term Query)
        .withQuery(q->q.bool(b->{ // QueryDSL 직접 정의. 복합 조건 걸기위한 bool 상자 열기
            // 키워드 유무에 따른 동적 쿼리 처리
            if(keyword==null || keyword.isBlank()){
                b.must(m->m.matchAll(ma->ma));
            } else {
                b.must(m->m.match(ma->ma.field("diseaseName").query(keyword)));
            }

            return b;
        }))
        .withMaxResults(100)
        .build();

        // 매번 찌르던 N+1 문제를 해결하기 위해 엘라스틱 서치에서 한 번에 싹 다 조회해 옴
        SearchHits<DiseaseDocument> searchHits = elasticsearchOperations.search(query, DiseaseDocument.class);
        // 가져온 질병들을 카테고리 ID별로 그룹핑(Map구조로 변환하여 메모리 준비)
        Map<String, List<DiseaseDocument>> diseaseMapByCategoryId = searchHits.getSearchHits().stream() // DiseaseDocument가 리스트로 감싸져있음
                .map(SearchHit::getContent) // DiseaseDocument 하나 꺼냄
                .collect(Collectors.groupingBy(DiseaseDocument::getCategoryId)); // 카테고리별로 그룹화해서 정리해둠
        // 전체 카테고리 목록 들고옴
        List<CategoryResponse> categories = categoryService.getAllCategories();

        // stream()으로 공장 컨베이어 벨트에 데이터가 하나씩 들어온다고 생각!
        return categories.stream().map(cat->{
            // 카테고리 아이디 string 변환
            String catIdStr = cat.id().toHexString();

            // 엘라스틱 서치로 미리 뽑아온 현재 카테고리 ID에 맞는 질환 데이터가 있다면 들고오고, 없으면 비어있는 리스트 반환 (네트워크 통신 0초!)
            List<DiseaseDocument> docs = diseaseMapByCategoryId.getOrDefault(catIdStr, Collections.emptyList());
            List<DiseaseResponse> diseases = docs.stream()
                    .map(doc -> new DiseaseResponse(
                            doc.getId(),
                            doc.getDiseaseName(),
                            null,
                            null,
                            new ObjectId(doc.getCategoryId())
                    ))
                    .toList();

            // 매핑 공장 안에서 변형된 카테고리별 객체를 하나씩 벨트 위로 던져줌(내부 반환)
            return new CategoryWithDiseasesResponse(
                    cat.id().toHexString(),
                    cat.categoryName(),
                    cat.iconUrl(),
                    diseases
            );

        })
        //  검색어가 있을 때는 결과가 비어있는 카테고리는 제외(조건 필터링 후 차곡차곡 담아둔다)
        .filter(res -> keyword==null || !res.diseases().isEmpty())
        // 최종 반환
        .toList();


    }
}
