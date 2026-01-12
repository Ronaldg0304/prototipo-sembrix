package com.sena.sembrix.ia.controller;

import com.sena.sembrix.common.web.ApiResponse;
import com.sena.sembrix.common.web.ResponseHelper;
import com.sena.sembrix.ia.service.SembrixAiService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/advisor")
@RequiredArgsConstructor
public class AiAdvisorController {

    private final SembrixAiService aiService;

    @PostMapping("/chat/{inventoryId}")
    public ResponseEntity<ApiResponse<String>> askAdvisor(
            @PathVariable Long inventoryId,
            @RequestBody Map<String, String> request) {

        String question = request.get("question");
        String advice = aiService.getAiAdvice(inventoryId, question);

        return ResponseHelper.ok(advice);
    }
}