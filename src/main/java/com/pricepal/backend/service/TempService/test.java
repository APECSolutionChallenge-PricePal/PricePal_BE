package com.pricepal.backend.service.TempService;

import jakarta.annotation.PostConstruct;
import org.springframework.boot.SpringApplication;
import org.springframework.web.reactive.function.client.WebClient;

import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.net.URI;
import java.util.HashMap;
import java.util.Map;

@SpringBootApplication

public class test {
    private final WebClient webClient;

    public test(WebClient webClient) {
        this.webClient = webClient;
    }

    public static void main(String[] args) {
        SpringApplication.run(test.class, args);
    }

    @PostConstruct
    public void init() {
        System.out.println("Application started successfully!");

//        String countryFlagApiUrl = "https://apis.data.go.kr/1262000/CountryFlagService2/getCountryFlagList2";
        String serviceKey = "WCwXdDTnj5Fm2%2BBNXl8blFfMTTD4bwVCqx6Rp6SzaAEiR4wUgmA0Oy%2F4Jc%2BFCqrZcQHcLNKgT0SOr2OmRV%2Bu7A%3D%3D";

//        String uri = countryFlagApiUrl + "?serviceKey=" + serviceKey + "&pageNo=1&numOfRows=220";
//        System.out.println("🌐 API Request URI: " + uri);

        Map<String, Object> test = new HashMap<String, Object>();
        test.put("serviceKey", serviceKey);
        test.put("pageNo", 1);
        test.put("numOfRows", 220);

        URI uri = URI.create("https://apis.data.go.kr/1262000/CountryFlagService2/getCountryFlagList2?serviceKey=" + serviceKey + "&pageNo=1&numOfRows=220");

        String externalApiResponse = webClient.get()
                .uri(uri)
                .retrieve()
                .bodyToMono(String.class)
                .block();

        System.out.println("🔎 External API Response: " + externalApiResponse);
    }

}
