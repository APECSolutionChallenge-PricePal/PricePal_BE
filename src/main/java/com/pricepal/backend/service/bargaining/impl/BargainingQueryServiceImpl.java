package com.pricepal.backend.service.bargaining.impl;

import com.pricepal.backend.web.dto.BargainingResponse;
import com.pricepal.backend.service.bargaining.BargainingQueryService;
import io.github.cdimascio.dotenv.Dotenv;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.net.URI;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class BargainingQueryServiceImpl implements BargainingQueryService {
    private final WebClient geminiWebClient;

    @Value("${gemini.api.url}")
    private String geminiApiUrl;

    @Value("${gemini.api.key}")
    private String geminiApiKey;

    @PostConstruct
    public void print() {
        System.out.println("▶ Gemini endpoint = " + geminiApiUrl);
    }

    @Override
    public BargainingResponse getBargainingTips(String travelCountry) {
        // 1) 프롬프트 생성
        String prompt = """
        You are a travel bargaining assistant. 
        Given a country name, return a JSON object with two keys: "tips" and "summary".

        Each element in "tips" must follow this rule:
        - For English-speaking countries: { "english": "..." }
        - For non-English countries using Latin script: { "local": "...", "english": "..." }
        - For non-English countries using non-Latin script: { "local": "...", "romanization": "...", "english": "..." }

        Examples:

        Case A: Input: "United Kingdom"
        Output:
        {
        "tips": [
         {"english":"Can you do any better on the price?"},
         {"english":"Could you throw something in?"}
         ],
         "summary": "British people often negotiate softly and politely."
        }

        Case B: Input: "Thailand"
        Output:
        {
        "tips": [
            {"local":"ลดราคาได้ไหม", "romanization":"lot-ra-kha dai mai", "english":"Can you give a discount?"}
        ],
        "summary": "Polite and soft negotiation is preferred."
        }

        Now generate for "%s".
        Return only a valid JSON object with keys `tips` and `summary`. Do not add explanations.
        """.formatted(travelCountry);


        JSONObject requestJson = new JSONObject()
                .put("contents", new JSONArray()
                        .put(new JSONObject()
                                .put("parts", new JSONArray()
                                        .put(new JSONObject().put("text", prompt))
                                )
                        )
                )
                .put("generationConfig", new JSONObject()
                        .put("temperature", 0)
                        .put("candidateCount", 1)
                        .put("maxOutputTokens", 512)
                );


        // (선택) 디버그용
        System.out.println("▶ Request JSON = " + requestJson.toString());
        System.out.println("GEMINI_API_KEY = [" + System.getenv("GEMINI_API_KEY") + "]");

        String raw = geminiWebClient.post()
                .uri(uriBuilder -> uriBuilder
                        .queryParam("key", geminiApiKey)
                        .build()
                )
                .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                .bodyValue(requestJson.toString())  // 여긴 그대로 ok
                .retrieve()
                .bodyToMono(String.class)
                .block();


        // 4) 응답 파싱: candidates → content → parts → text
        JSONObject root = new JSONObject(raw);
        String inner = root
                .getJSONArray("candidates")
                .getJSONObject(0)
                .getJSONObject("content")
                .getJSONArray("parts")
                .getJSONObject(0)
                .getString("text");

        JSONObject data = new JSONObject(inner);
        JSONArray tipsArr = data.getJSONArray("tips");
        List<String> tips = new ArrayList<>();
        for (int i = 0; i < tipsArr.length(); i++) {
            JSONObject tip = tipsArr.getJSONObject(i);
            tips.add(tip.has("local") ? tip.toString() : tip.getString("english"));
        }
        String summary = data.getString("summary");

        // 5) DTO 반환
        return BargainingResponse.builder()
                .tips(tips)
                .summary(summary)
                .build();
    }
}