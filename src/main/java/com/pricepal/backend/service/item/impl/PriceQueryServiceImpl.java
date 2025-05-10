package com.pricepal.backend.service.item.impl;

import com.pricepal.backend.service.item.PriceQueryService;
import com.pricepal.backend.service.item.UnsplashService;
import com.pricepal.backend.web.dto.PriceDto;
import lombok.RequiredArgsConstructor;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class PriceQueryServiceImpl implements PriceQueryService {
    private final WebClient geminiWebClient;
    private final UnsplashService unsplashService;

    @Value("${gemini.api.key}")
    private String geminiApiKey;

    @Override
    public List<PriceDto> getPriceList(String userCountry, String travelCountry) {
        String prompt = """
                    You are a travel assistant.
                    Given the user's country and the travel destination, return 3 common daily items
                    that are popular in the travel country. For each item, return:
                      - name
                      - price in user's country (with currency symbol)
                      - price in travel country (with currency symbol)
                      - imagePrompt (to describe the item visually with white background)
                
                    The item name should be specific and descriptive to help search for clear product images.
                    Avoid vague or generic names like "water" or "snack".
                    Use names like "a bottle of mineral water", "a bag of potato chips", or "a can of soda".
                
                    Return only a valid JSON array. Example:
                    [
                      {
                        "name": "Bottle of Water",
                        "userPrice": "$1.00",
                        "travelPrice": "€0.80",
                        "imagePrompt": "a bottle of mineral water on white background"
                      },
                      ...
                    ]
                    Return only a valid JSON array. Do not include any explanation or text outside of the array.
                
                    User country: %s
                    Travel country: %s
                """.formatted(userCountry, travelCountry);

        JSONObject requestJson = new JSONObject()
                .put("contents", new JSONArray()
                        .put(new JSONObject()
                                .put("parts", new JSONArray()
                                        .put(new JSONObject().put("text", prompt))
                                )
                        )
                )
                .put("generationConfig", new JSONObject()
                        .put("temperature", 0.4)
                        .put("candidateCount", 1)
                        .put("maxOutputTokens", 512)
                );

        String raw = geminiWebClient.post()
                .uri(uriBuilder -> uriBuilder
                        .queryParam("key", geminiApiKey)
                        .build())
                .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                .bodyValue(requestJson.toString())
                .retrieve()
                .bodyToMono(String.class)
                .block();

        JSONObject root = new JSONObject(raw);

        // Gemini 응답 파싱
        String inner = root
                .getJSONArray("candidates")
                .getJSONObject(0)
                .getJSONObject("content")
                .getJSONArray("parts")
                .getJSONObject(0)
                .getString("text")
                .trim();  // 혹시 모를 앞뒤 공백 제거

        if (inner.startsWith("```")) {
            inner = inner.replaceAll("(?s)```json\\s*(.*?)\\s*```", "$1").trim();
        }

        // 응답이 배열 형식인지 확인
        if (!inner.startsWith("[")) {
            System.out.println("❌ Gemini 응답이 JSON 배열이 아님: " + inner);
            throw new RuntimeException("Gemini 응답이 JSON 배열 형식이 아님: " + inner);
        }

        JSONArray items = new JSONArray(inner);
        List<PriceDto> results = new ArrayList<>();
        for (int i = 0; i < items.length(); i++) {
            JSONObject item = items.getJSONObject(i);

            String name = item.getString("name");
            String imageUrl = unsplashService.fetchImageUrl(name);  // 이미지 검색

            results.add(PriceDto.builder()
                    .itemName(name)
                    .userPrice(item.getString("userPrice"))
                    .travelPrice(item.getString("travelPrice"))
                    .image(imageUrl)
                    .build());
        }


        return results;
    }
}