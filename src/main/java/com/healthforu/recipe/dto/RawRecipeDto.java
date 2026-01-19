package com.healthforu.recipe.dto;

import com.fasterxml.jackson.annotation.JsonAnySetter;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.HashMap;
import java.util.Map;

@Getter
@NoArgsConstructor
public class RawRecipeDto {
    // 기본 필드들 (JSON의 키값과 정확히 일치시켜야 함)
    @JsonProperty("RCP_NM")
    private String RCP_NM;

    @JsonProperty("ATT_FILE_NO_MK")
    private String ATT_FILE_NO_MK;

    @JsonProperty("RCP_PARTS_DTLS")
    private String RCP_PARTS_DTLS;

    // MANUAL01, MANUAL_IMG01 등 동적 필드들을 다 담기 위한 맵
    private Map<String, String> rawFields = new HashMap<>();

    @JsonAnySetter
    public void addRawField(String key, String value) {
        if (key.contains("MANUAL")) {
            rawFields.put(key, value);
        }
    }

    public String getManualTextByStep(int step) {
        return rawFields.get(String.format("MANUAL%02d", step));
    }

    public String getManualImgByStep(int step) {
        return rawFields.get(String.format("MANUAL_IMG%02d", step));
    }
}