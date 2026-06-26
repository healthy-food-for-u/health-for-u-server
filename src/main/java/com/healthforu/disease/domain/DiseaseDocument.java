package com.healthforu.disease.domain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.Document;
import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.data.elasticsearch.annotations.FieldType;
import org.springframework.data.elasticsearch.annotations.Setting;

/**
 * Elasticsearch의 'diseases' 인덱스와 매핑되는 도메인(Document) 클래스
 * <p>
 * 질환 검색 성능 향상을 위해 노리(nori) 형태소 분석기를 적용하여 질환명 및 주의사항을 인덱싱한다.
 * DB(Elasticsearch) 조회를 위한 전용 엔티티이며, 클라이언트 응답 시에는 {@link com.healthforu.disease.dto.DiseaseResponse}로 변환되어 사용된다.
 * (데이터베이스와 자바 스프링 코드를 이어주는 다리 역할!)
 * </p>
 */
@Builder
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Document(indexName = "diseases")
@Setting(settingPath = "elasticsearch/settings.json")
public class DiseaseDocument {

    @Id
    private String id;

    @Field(type = FieldType.Text, analyzer = "nori")
    private String diseaseName;

    @Field(type = FieldType.Text, analyzer = "nori")
    private String caution;

    private String categoryId;

    public static DiseaseDocument from(Disease disease){
        return DiseaseDocument.builder()
                .id(disease.getId().toHexString())
                .diseaseName(disease.getDiseaseName())
                .caution(disease.getCaution())
                .categoryId(disease.getCategoryId() != null ? disease.getCategoryId().toHexString() : null)
                .build();
    }
}
