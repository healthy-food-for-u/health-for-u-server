package com.healthforu.recipe.domain;

import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.List;

/**
 * 레시피 정보를 담는 엔티티 클래스입니다.
 * <p>
 * 이 엔티티의 데이터는 별도의 데이터 수집 스크립트(Node.js)를 통해
 * 외부 공공 API로부터 수집되어 MongoDB에 미리 적재(Pre-populated)되어 있습니다.
 * 따라서 현재 서비스 내에서는 데이터의 무결성을 위해 별도의 생성/수정/삭제 로직 없이
 * 조회(Read) 기능 위주로 구성되어 있습니다.
 */
@Builder
@Document(collection = "recipes")
@Getter
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Recipe {

    @Id
    private String id;

    private String recipeName;

    private String ingredients;

    private List<ManualStep> manualSteps;

    private String recipeThumbnail;

    @Getter
    @AllArgsConstructor
    public static class ManualStep {
        private int stepNumber;
        private String stepDescription;
        private String imageUrl;
    }
}
