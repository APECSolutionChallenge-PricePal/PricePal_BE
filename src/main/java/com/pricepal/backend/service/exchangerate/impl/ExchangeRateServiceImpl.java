package com.pricepal.backend.service.exchangerate.impl;

import com.pricepal.backend.service.TempService.CountryPriceCodeService;
import com.pricepal.backend.service.exchangerate.ExchangeRateService;
import lombok.RequiredArgsConstructor;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

@Service
@RequiredArgsConstructor
public class ExchangeRateServiceImpl implements ExchangeRateService {

    @Value("${exchange.api.url}")
    private String apiUrl;

    @Value("${exchange.api.key}")
    private String apiKey;

    private final WebClient webClient;
    private final CountryPriceCodeService countryPriceCodeService;

    @Override
    public String getRate(String base, String target) {
        String baseCode = countryPriceCodeService.getGeminiPriceCode(base);
        String targetCode = countryPriceCodeService.getGeminiPriceCode(target);

        String uri = String.format("%s/pair/%s/%s", apiUrl, baseCode, targetCode);  // e.g. https://v6.exchangerate-api.com/v6/YOUR_API_KEY/pair/KRW/USD

        String response = webClient.get()
                .uri(uriBuilder -> uriBuilder
                        .scheme("https")
                        .host("v6.exchangerate-api.com")
                        .path("/v6/" + apiKey + "/pair/" + baseCode + "/" + targetCode)
                        .build()
                )
                .retrieve()
                .bodyToMono(String.class)
                .block();

        JSONObject json = new JSONObject(response);
        double rate = json.getDouble("conversion_rate");

        return String.format("1 %s = %.6f %s", base, rate, target);
    }
}

