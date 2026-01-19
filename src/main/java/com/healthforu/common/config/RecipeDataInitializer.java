package com.healthforu.common.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.healthforu.recipe.domain.Recipe;
import com.healthforu.recipe.dto.RawRecipeDto;
import com.healthforu.recipe.repository.RecipeRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

/**
 * 식품안전나라 공공 API를 통해 레시피 데이터를 수집하고
 * MongoDB에 최초 한 번만 벌크 삽입하는 서비스 클래스입니다.
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class RecipeDataInitializer {

    private final RecipeRepository recipeRepository;
    private final RestTemplate restTemplate;
    private final Environment env;

    /**
     * 외부 API로부터 데이터를 수집하여 엔티티로 변환 후
     * 데이터베이스에 일괄 저장하는 메인 프로세스를 실행합니다.
     */
    @Transactional
    public void importRecipesFromExternalApi() {
        List<RawRecipeDto> externalRecipes = fetchRecipesFromApi();

        if (externalRecipes.isEmpty()) {
            return;
        }

        List<Recipe> recipesToInsert = externalRecipes.stream()
                .map(this::convertToEntity)
                .collect(Collectors.toList());

        recipeRepository.bulkInsertRecipes(recipesToInsert);
    }

    /**
     * 수집된 원본 DTO(RawRecipeDto)를 도메인 엔티티(Recipe)로 변환합니다.
     * @param r 원본 데이터 DTO
     * @return 가공된 Recipe 엔티티
     */
    private Recipe convertToEntity(RawRecipeDto r) {
        return Recipe.builder()
                .recipeName(r.getRCP_NM())
                .recipeThumbnail(r.getATT_FILE_NO_MK())
                .ingredients(r.getRCP_PARTS_DTLS())
                .manualSteps(extractStructuredManual(r))
                .build();
    }

    /**
     * 비정형화된 API 원본 텍스트에서 조리 단계별 설명과 이미지를 추출합니다.
     * 정규식을 사용하여 텍스트 끝의 불필요한 문자를 정제합니다.
     * @param recipe 원본 데이터 DTO
     * @return 정제된 조리 단계 리스트 (ManualStep)
     */
    private List<Recipe.ManualStep> extractStructuredManual(RawRecipeDto recipe) {
        List<Recipe.ManualStep> steps = new ArrayList<>();
        Pattern cleanupPattern = Pattern.compile("(?<=\\.)[a-z]$", Pattern.CASE_INSENSITIVE);

        for (int step = 1; step <= 20; step++) {
            String stepText = recipe.getManualTextByStep(step);
            String stepImg = recipe.getManualImgByStep(step);

            if (stepText == null || stepText.trim().isEmpty()) break;

            String cleanedText = cleanupPattern.matcher(stepText.trim()).replaceAll("").trim();

            steps.add(new Recipe.ManualStep(step, cleanedText, stepImg != null ? stepImg.trim() : ""));
        }
        return steps;
    }

    /**
     * 식품안전나라 Open API를 호출하여 원본 레시피 데이터를 가져옵니다.
     * 환경 설정 파일에서 인증 정보를 읽어오며, Jackson을 사용하여 결과를 DTO로 매핑합니다.
     * 추후, api 통신만을 담당하는 Client를 만들 예정입니다.
     * @return 수집된 RawRecipeDto 리스트
     */
    private List<RawRecipeDto> fetchRecipesFromApi(){
        String authKey = env.getProperty("AUTH_KEY");
        String serviceId = env.getProperty("SERVICE_ID");
        String dataType = env.getProperty("DATA_TYPE");
        Integer startIdx = 1;
        Integer endIdx = 100;
        String uri = String.format("http://openapi.foodsafetykorea.go.kr/api/%s/%s/%s/%d/%d",
                authKey, serviceId, dataType, startIdx, endIdx);
        try{
            Map<String, Object> response = restTemplate.getForObject(uri, Map.class);

            if(response != null && response.containsKey("COOKRCP01")){
                Map<String, Object> cookRecipe = (Map<String, Object>) response.get("COOKRCP01");
                List<Map<String, Object>> rowList = (List<Map<String, Object>>) cookRecipe.get("row");

                ObjectMapper mapper = new ObjectMapper();
                return rowList.stream()
                        .map(row -> mapper.convertValue(row, RawRecipeDto.class))
                        .collect(Collectors.toList());
            }
        } catch(Exception e){
            log.error("레시피 API 호출 중 오류 발생 : ", e.getMessage());
        }

        return new ArrayList<>();


    }
}