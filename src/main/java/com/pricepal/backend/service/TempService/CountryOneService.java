package com.pricepal.backend.service.TempService;

import com.pricepal.backend.apiPayload.ApiResponse;
import com.pricepal.backend.web.dto.CountryOneRequest;

public interface CountryOneService {
    ApiResponse<String> getCountryOneFlags(CountryOneRequest request);

}
