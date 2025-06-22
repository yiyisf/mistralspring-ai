package com.example.mistralspringai.controller;

import com.example.mistralspringai.service.EmbeddingService;
import org.springframework.ai.embedding.EmbeddingModel;
import org.springframework.ai.embedding.EmbeddingResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

@RestController
public class EmbeddingController {

    private final EmbeddingModel embeddingModel;

    private final EmbeddingService embeddingService;

    @Autowired
    public EmbeddingController(EmbeddingModel embeddingModel, EmbeddingService embeddingService) {
        this.embeddingModel = embeddingModel;
        this.embeddingService = embeddingService;
    }

    @GetMapping("/ai/embedding")
    public Map embedding(@RequestParam(value = "message", defaultValue = "讲个笑话") String message) {
        var embeddingResponse = this.embeddingModel.embedForResponse(List.of(message));
        System.out.println("embeddingResponse response:" + embeddingResponse);
        return Map.of("embedding", embeddingResponse);
    }

    @GetMapping("/ai/embedding-compare")
    public EmbeddingResponse embeddingCompare(@RequestParam(value = "text1", defaultValue = "讲个笑话1") String text1,
                                              @RequestParam(value = "text2", defaultValue = "讲个笑话2") String text2) {
        return embeddingService.embeddingCompare(text1, text2);
    }
}
