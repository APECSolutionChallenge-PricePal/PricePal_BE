package com.pricepal.backend.service.TempService;

import com.pricepal.backend.apiPayload.ApiResponse;
import com.pricepal.backend.web.dto.TempRequest;

public interface TempQueryService {
    ApiResponse<String> getGeminiGuide(TempRequest request);
    //void CheckFlag(Integer flag);
}
