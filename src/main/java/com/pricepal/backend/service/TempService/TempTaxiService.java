package com.pricepal.backend.service.TempService;

import com.pricepal.backend.apiPayload.ApiResponse;
import com.pricepal.backend.web.dto.TempTaxiRequest;
import com.pricepal.backend.web.dto.TempTaxiResponse;

public interface TempTaxiService {
    ApiResponse<TempTaxiResponse> getGeminiTaxiFare(TempTaxiRequest request);
}
