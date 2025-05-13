package com.pricepal.backend.service.TempService;

import com.pricepal.backend.apiPayload.ApiResponse;
import com.pricepal.backend.service.item.UnsplashService;
import com.pricepal.backend.web.dto.TempRequest;
import com.pricepal.backend.web.dto.TempResponse;
import io.github.cdimascio.dotenv.Dotenv;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class TempQueryServiceImpl implements TempQueryService {
//    @Override
//    public void CheckFlag(Integer flag) {
//        if (flag == 1)
//            throw new TempHandler(ErrorStatus.TEMP_EXCEPTION);
//    }

    private final WebClient webClient;
    private final UnsplashService unsplashService;
    private final Dotenv dotenv = Dotenv.load();
    private final String geminiApiKey = dotenv.get("GEMINI_API_KEY");


    @Value("${gemini.api.url}")
    private String geminiApiUrl;

//    @Value("${gemini.api.key}")
//    private String geminiApiKey;

    public ApiResponse<List<TempResponse>> getGeminiGuide(TempRequest request) {
        String prompt = generatePrompt(request.getItemName(), request.getCountry());

        String guide = webClient.post()
                .uri(geminiApiUrl + "?key=" + geminiApiKey)
                .header("Content-Type", "application/json")
                .bodyValue(new JSONObject()
                        .put("contents", new JSONObject()
                                .put("parts", new JSONObject()
                                        .put("text", prompt)
                                )
                        )
                        .toString())
                .retrieve()
                .onStatus(
                        status -> status.is4xxClientError() || status.is5xxServerError(),
                        response -> {
                            System.out.println("❌ Error: " + response.statusCode());
                            return response.bodyToMono(String.class).map(error -> {
                                System.out.println("Error Response: " + error);
                                return new IllegalStateException("Error from Gemini API: " + error);
                            });
                        }
                )
                .bodyToMono(String.class)
                .block();
        JSONObject responseJson = new JSONObject(guide);
        String guideText = responseJson
                .getJSONArray("candidates")
                .getJSONObject(0)
                .getJSONObject("content")
                .getJSONArray("parts")
                .getJSONObject(0)
                .getString("text");

        List<TempResponse> results = new ArrayList<>();
        String imageUrl = unsplashService.fetchImageUrl(request.getItemName());

        results.add(TempResponse.builder()
                .priceText(guideText)
                .image(imageUrl)
                .build());

        return ApiResponse.onSuccess(results);
    }

    // ✅ 동적으로 프롬프트 생성
    private String generatePrompt(String itemName, String country) {
        return String.format(
                "You are an assistant that provides price guides for products in different countries. " +
                        "Please create a price guide for the product \"%s\" in \"%s\". " +
                        "Your response must strictly follow the format below without any additional text or explanation:\n\n" +
                        "\"%s %s Price Guide:\n\n" +
                        "🟢 Great Purchase\n" +
                        "Typical low price range specific to this product and country.\n" +
                        "Example: Discount stores, bulk purchase.\n\n" +
                        "🟡 Fair Price\n" +
                        "Standard market price for this product in this country.\n" +
                        "Example: Supermarkets, online retailers.\n\n" +
                        "🔴 Pricey\n" +
                        "Higher price range, limited edition or premium version.\n" +
                        "Example: Specialty stores, exclusive items.\n\n" +
                        "🎯 Tips\n" +
                        "Check: size, brand, packaging, limited edition.\n" +
                        "Look for: sales, promotions.\"",
                itemName, country, country, itemName
        );
    }
}
