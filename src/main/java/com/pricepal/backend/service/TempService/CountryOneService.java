package com.pricepal.backend.service.TempService;

import com.pricepal.backend.apiPayload.ApiResponse;
import com.pricepal.backend.web.dto.CountryOneRequest;
import com.pricepal.backend.web.dto.CountryOneResponse;

import java.util.List;

public interface CountryOneService {
    ApiResponse<List<CountryOneResponse>> getCountryOneFlags(CountryOneRequest request);

}
