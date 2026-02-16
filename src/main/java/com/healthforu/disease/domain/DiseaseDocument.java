package com.healthforu.disease.domain;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.elasticsearch.annotations.Document;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Document(indexName = "diseases")
public class DiseaseDocument {

}
