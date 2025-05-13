package com.pricepal.backend.service.TempService;

import com.pricepal.backend.apiPayload.ApiResponse;
import com.pricepal.backend.web.dto.CountryOneRequest;
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
public class CountryOneServiceImpl implements CountryOneService {
    private final WebClient webClient;
    private final CountryPriceCodeService countryPriceCodeService;
    @Value("${external.api.key}")
    private String serviceKey;

    @Override
    public ApiResponse<String> getCountryOneFlags(CountryOneRequest request) {
        Map<String, Object> test = new HashMap<String, Object>();
        test.put("serviceKey", serviceKey);
        test.put("pageNo", 1);
        test.put("numOfRows", 220);
        test.put("cond[country_iso_alp2::EQ]",request.getCountry());

        URI uri = URI.create("https://apis.data.go.kr/1262000/CountryFlagService2/getCountryFlagList2?serviceKey=" + serviceKey + "&pageNo=1&numOfRows=220&cond[country_iso_alp2::EQ]=" + request.getCountry());

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

        List<Map<String, String>> result = new ArrayList<>();
        for (int i = 0; i < items.length(); i++) {
            JSONObject item = items.getJSONObject(i);
            String countryEngNm = item.optString("country_eng_nm", "");
            String downloadUrl = item.optString("download_url", "");
            //String countryPriceCode = countryPriceCodeService.getGeminiPriceCode(countryEngNm);
            result.add(Map.of(
                    "country_eng_nm", countryEngNm,
                    "download_url", downloadUrl
                    //"country_price_code",countryPriceCode
            ));
        }

        return ApiResponse.onSuccess(result.toString());
    }
}
