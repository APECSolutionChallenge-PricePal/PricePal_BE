package com.pricepal.backend.service.item;

import lombok.RequiredArgsConstructor;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;

@Service
@RequiredArgsConstructor
public class UnsplashService {

    @Value("${unsplash.access-key}")
    private String accessKey;

    private final WebClient webClient = WebClient.create("https://api.unsplash.com");

    public String fetchImageUrl(String query) {
        String encoded = URLEncoder.encode(query, StandardCharsets.UTF_8);
        String response = webClient.get()
                .uri(uriBuilder -> uriBuilder
                        .path("/search/photos")
                        .queryParam("query", encoded)
                        .queryParam("client_id", accessKey)
                        .queryParam("per_page", 1)
                        .build())
                .retrieve()
                .bodyToMono(String.class)
                .block();

        JSONObject json = new JSONObject(response);
        JSONArray results = json.getJSONArray("results");

        if (results.length() > 0) {
            return results.getJSONObject(0)
                    .getJSONObject("urls")
                    .getString("regular");
        }

        // 결과 없으면 mock 이미지
        return "https://dummyimage.com/512x512/ffffff/000000&text=" + encoded;
    }
}
