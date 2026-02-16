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
