package com.pricepal.backend.service.item.impl;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.pricepal.backend.web.dto.PriceDto;
import com.pricepal.backend.service.item.PriceQueryService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import java.time.Duration;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class PriceQueryServiceImpl implements PriceQueryService {
    private final WebClient geminiWebClient;
    private final ObjectMapper mapper;

    @Override
    public List<PriceDto> getPriceList(String country) {
        String prompt = """
            User is traveling to "%s".
            Provide 3 famous items and their average price in KRW and USD as JSON array:
            [
              {"name":"A Bottle of Water","priceKRW":"800 KRW","priceUSD":"0.54 USD"},
              …
            ]
            """.formatted(country);

        JsonNode body = mapper.createObjectNode()
                .put("model","gemini-1.5")
                .set("messages", mapper.createArrayNode()
                        .add(mapper.createObjectNode()
                                .put("role","user")
                                .put("content",prompt))
                );

        String raw = geminiWebClient.post()
                .uri("/v1/chat/completions")
                .bodyValue(body)
                .retrieve()
                .bodyToMono(String.class)
                .block(Duration.ofSeconds(10));

        try {
            JsonNode content = mapper.readTree(raw)
                    .path("choices").get(0)
                    .path("message").path("content");
            return mapper.readValue(
                    content.asText(),
                    mapper.getTypeFactory().constructCollectionType(List.class, PriceDto.class)
            );
        } catch (Exception e) {
            throw new RuntimeException("Gemini 가격정보 파싱 실패", e);
        }
    }
}