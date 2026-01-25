package com.healthforu.disease.controller;

import com.healthforu.category.dto.CategoryWithDiseasesResponse;
import com.healthforu.disease.dto.DiseaseResponse;
import com.healthforu.disease.service.DiseaseService;
import lombok.RequiredArgsConstructor;
import org.bson.types.ObjectId;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/diseases")
@RequiredArgsConstructor
public class DiseaseController {

    private final DiseaseService diseaseService;

    @GetMapping("/{diseaseId}")
    public ResponseEntity<DiseaseResponse> getDiseaseDetail(@PathVariable("diseaseId") ObjectId diseaseId){

        return ResponseEntity.ok(diseaseService.getDisease(diseaseId));
    }


    @GetMapping
    public ResponseEntity<List<CategoryWithDiseasesResponse>> getDiseasesByCategories(
            @RequestParam(value = "keyword", required = false) String keyword){

        return ResponseEntity.ok(diseaseService.searchDiseases(keyword));
    }

}
