package com.pricepal.backend.service.TempService;

import com.pricepal.backend.apiPayload.ApiResponse;
import com.pricepal.backend.web.dto.TempTaxiRequest;
import com.pricepal.backend.web.dto.TempTaxiResponse;
import io.github.cdimascio.dotenv.Dotenv;
import lombok.RequiredArgsConstructor;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

@Service
@RequiredArgsConstructor
public class TempTaxiServiceImpl implements TempTaxiService {
    private final WebClient webClient;
//    private final Dotenv dotenv = Dotenv.load();
//    private final String geminiApiKey = dotenv.get("GEMINI_API_KEY");

    @Value("${gemini.api.url}")
    private String geminiApiUrl;

    @Value("${gemini.api.key}")
    private String geminiApiKey;

    public ApiResponse<TempTaxiResponse> getGeminiTaxiFare(TempTaxiRequest request) {
        String prompt = generateTaxiPrompt(request.getDistance(), request.getCountry());

        String rawResponse = webClient.post()
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
                .bodyToMono(String.class)
                .block();

        JSONObject responseJson = new JSONObject(rawResponse);
        String guideText = responseJson
                .getJSONArray("candidates")
                .getJSONObject(0)
                .getJSONObject("content")
                .getJSONArray("parts")
                .getJSONObject(0)
                .getString("text");

        String basicFare = "-";
        String estimatedFare = "-";
        String[] lines = guideText.split("\n");
        for (String line : lines) {
            if (line.startsWith("Basic Taxi Fares:")) {
                basicFare = line.replace("Basic Taxi Fares: ", "").trim();
            } else if (line.startsWith("Estimated Fare:")) {
                estimatedFare = line.replace("Estimated Fare: ", "").trim();
            }
        }

        // ✅ TempTaxiResponse 생성
        TempTaxiResponse taxiResponse = TempTaxiResponse.builder()
                .basicFare(basicFare)
                .estimatedFare(estimatedFare)
                .build();

        return ApiResponse.onSuccess(taxiResponse);
    }

    // ✅ 프롬프트 생성 (지정된 형식)
    private String generateTaxiPrompt(String distance, String country) {
        return String.format(
                "You are an assistant that provides taxi fare guides. " +
                        "Please provide the basic taxi fare and estimated fare for a ride in %s for a distance of %s km. " +
                        "If the country uses a fixed rate (no basic fare), show 'Basic Taxi Fares: -'. " +
                        "Format the response strictly as follows:\n\n" +
                        "Basic Taxi Fares: (basic fare or '-')\n" +
                        "Estimated Fare: (calculated fare)\n",
                country, distance
        );
    }
}
