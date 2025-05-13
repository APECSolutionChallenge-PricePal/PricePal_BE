package com.pricepal.backend.service.TempService;

import com.pricepal.backend.apiPayload.ApiResponse;
import com.pricepal.backend.web.dto.TempRequest;
import com.pricepal.backend.web.dto.TempResponse;

import java.util.List;

public interface TempQueryService {
    ApiResponse<List<TempResponse>> getGeminiGuide(TempRequest request);
    //void CheckFlag(Integer flag);
}
