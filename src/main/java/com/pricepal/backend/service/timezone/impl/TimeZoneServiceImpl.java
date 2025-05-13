package com.pricepal.backend.service.timezone.impl;

import com.pricepal.backend.service.timezone.TimeZoneService;
import lombok.RequiredArgsConstructor;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Service
@RequiredArgsConstructor
public class TimeZoneServiceImpl implements TimeZoneService {

    private final WebClient geminiWebClient;
    private final WebClient webClient;

    @Value("${gemini.api.key}")
    private String geminiApiKey;

    @Value("${timezone.api.key}")
    private String timezoneApiKey;

    @Override
    public String getUtcOffset(String input) {

        String prompt = """
                Given a country name or ISO 3166-1 alpha-2 code, return the IANA time zone ID that represents the country's most common time zone.
                
                Examples:
                Input: "KR"
                Output: Asia/Seoul

                Input: "France"
                Output: Europe/Paris

                Input: "US"
                Output: America/New_York

                Now return for: "%s"
                Return only the time zone ID string.
                """.formatted(input);

        String geminiRequest = new JSONObject()
                .put("contents", new org.json.JSONArray()
                        .put(new JSONObject()
                                .put("parts", new org.json.JSONArray()
                                        .put(new JSONObject().put("text", prompt)))))
                .put("generationConfig", new JSONObject()
                        .put("temperature", 0.2)
                        .put("candidateCount", 1)
                        .put("maxOutputTokens", 64))
                .toString();

        String geminiResponse = geminiWebClient.post()
                .uri(uriBuilder -> uriBuilder
                        .queryParam("key", geminiApiKey)
                        .build())
                .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                .bodyValue(geminiRequest)
                .retrieve()
                .bodyToMono(String.class)
                .block();

        String text = new JSONObject(geminiResponse)
                .getJSONArray("candidates")
                .getJSONObject(0)
                .getJSONObject("content")
                .getJSONArray("parts")
                .getJSONObject(0)
                .getString("text")
                .trim();

        Pattern pattern = Pattern.compile("([A-Za-z_]+/[A-Za-z_]+)");
        Matcher matcher = pattern.matcher(text);
        String zoneId = matcher.find() ? matcher.group(1) : text;

        String timezoneResponse = webClient.get()
                .uri(uriBuilder -> uriBuilder
                        .scheme("https")
                        .host("api.timezonedb.com")
                        .path("/v2.1/get-time-zone")
                        .queryParam("key", timezoneApiKey)
                        .queryParam("format", "json")
                        .queryParam("by", "zone")
                        .queryParam("zone", zoneId)
                        .build())
                .retrieve()
                .bodyToMono(String.class)
                .block();

        JSONObject json = new JSONObject(timezoneResponse);
        int offsetSeconds = json.getInt("gmtOffset");
        int offsetHours = offsetSeconds / 3600;

        return offsetHours >= 0 ? "UTC+" + offsetHours : "UTC" + offsetHours;
    }
}
