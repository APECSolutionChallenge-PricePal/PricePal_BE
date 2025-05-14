package com.pricepal.backend.service.TempService;

import com.pricepal.backend.apiPayload.ApiResponse;
import com.pricepal.backend.web.dto.CountryOneResponse;
import lombok.RequiredArgsConstructor;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import java.net.URI;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class CountryServiceImpl implements CountryService {
    private final WebClient webClient;

    private final CountryPriceCodeService countryPriceCodeService;
    @Value("${external.api.key}")
    private String serviceKey;

    @Override
    public ApiResponse<List<CountryOneResponse>> getCountryFlags() {
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

        JSONObject responseJson = new JSONObject(externalApiResponse);
        JSONArray items = responseJson.getJSONObject("response")
                .getJSONObject("body")
                .getJSONObject("items")
                .getJSONArray("item");

        List<CountryOneResponse> result = new ArrayList<>();
        for (int i = 0; i < items.length(); i++) {
            JSONObject item = items.getJSONObject(i);
            String countryEngNm = item.optString("country_eng_nm", "");
            String downloadUrl = item.optString("download_url", "");
            String countryIsoAlp2 = item.optString("country_iso_alp2", "");
            //String countryPriceCode = countryPriceCodeService.getGeminiPriceCode(countryEngNm);
            result.add(CountryOneResponse.builder()
                    .countryEngNm(countryEngNm)
                    .downloadUrl(downloadUrl)
                    .countryIsoAlp2(countryIsoAlp2)
                    .build()
            );
        }

        return ApiResponse.onSuccess(result);
    }
}


