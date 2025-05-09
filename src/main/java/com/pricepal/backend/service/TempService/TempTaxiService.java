package com.pricepal.backend.service.TempService;

import com.pricepal.backend.apiPayload.ApiResponse;
import com.pricepal.backend.web.dto.TempTaxiRequest;

public interface TempTaxiService {
    ApiResponse<String> getGeminiTaxiFare(TempTaxiRequest request);
}
