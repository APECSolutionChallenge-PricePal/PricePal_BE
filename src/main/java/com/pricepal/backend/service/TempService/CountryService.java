package com.pricepal.backend.service.TempService;

import com.pricepal.backend.apiPayload.ApiResponse;
import com.pricepal.backend.web.dto.CountryFlagApiResponse;
import com.pricepal.backend.web.dto.CountryFlagResponse;
import com.pricepal.backend.web.dto.TempTaxiRequest;

import java.util.List;

public interface CountryService {
    ApiResponse<String> getCountryFlags();
}
