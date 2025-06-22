package com.example.mistralspringai.service;

import org.springframework.ai.embedding.EmbeddingModel;
import org.springframework.ai.embedding.EmbeddingResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.List;

@Service
public class EmbeddingService {

    private final EmbeddingModel embeddingModel;

    @Autowired
    public EmbeddingService(EmbeddingModel embeddingModel) {
        this.embeddingModel = embeddingModel;

    }

    public EmbeddingResponse embeddingCompare(String text1, String text2) {
        //embedding单个字符串
        float[] embed1 = embeddingModel.embed(text1);
        float[] embed2 = embeddingModel.embed(text2);
        System.out.println("text1:" + Arrays.toString(embed1));
        System.out.println("text2:" + Arrays.toString(embed2));
        //embedding多个字符串
        List<float[]> embedes = embeddingModel.embed(List.of(text1, text2));
        System.out.println("embedes:" + embedes);

        return embeddingModel.embedForResponse(List.of(text1, text2));
    }
}
