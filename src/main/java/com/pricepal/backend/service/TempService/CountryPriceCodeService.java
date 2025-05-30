package com.pricepal.backend.service.TempService;

import lombok.RequiredArgsConstructor;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

@Service
@RequiredArgsConstructor
public class CountryPriceCodeService {
    private final WebClient webClient;

    @Value("${gemini.api.url}")
    private String geminiApiUrl;

    @Value("${gemini.api.key}")
    private String geminiApiKey;
    
    public String getGeminiPriceCode(String query) {
    String prompt = generateTaxiPrompt(query);

    JSONObject body = new JSONObject()
        .put("contents", new org.json.JSONArray()
            .put(new JSONObject()
                .put("parts", new org.json.JSONArray()
                    .put(new JSONObject()
                        .put("text", prompt)
                    )
                )
            )
        );

    String rawResponse = webClient.post()
            .uri(geminiApiUrl + "?key=" + geminiApiKey)
            .header("Content-Type", "application/json")
            .bodyValue(body.toString())
            .retrieve()
            .bodyToMono(String.class)
            .block();

    JSONObject responseJson = new JSONObject(rawResponse);
    String guideText = responseJson
            .getJSONArray("candidates")
            .getJSONObject(0)
            .getJSONObject("content")
            .getJSONArray("parts")
            .getJSONObject(0)
            .getString("text")
            .trim();
    return guideText;
}

    private String generateTaxiPrompt(String country) {
        return String.format(
                "You are an assistant that provides currency code information. Please provide the currency code for the country %s. \n" +
                        "Format the response strictly as follows:\n\n" +
                        "(currency code only)",
                country
        );
    }
}
