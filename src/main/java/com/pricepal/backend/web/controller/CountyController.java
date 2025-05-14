package com.pricepal.backend.web.controller;

import com.pricepal.backend.apiPayload.ApiResponse;
import com.pricepal.backend.service.TempService.CountryOneService;
import com.pricepal.backend.service.TempService.CountryService;
import com.pricepal.backend.web.dto.CountryOneRequest;
import com.pricepal.backend.web.dto.CountryOneResponse;
import com.pricepal.backend.web.dto.TempTaxiRequest;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/country")
public class CountyController {
    private final CountryService countryService;
    private final CountryOneService countryOneService;

    public CountyController(@Qualifier("countryServiceImpl") CountryService countryService, CountryOneService countryOneService) {
        this.countryService = countryService;
        this.countryOneService = countryOneService;
    }

    @GetMapping("/all")
    public ApiResponse<List<CountryOneResponse>> getCountryFlags() {
        return countryService.getCountryFlags();
    }

    @GetMapping("/one")
    public ApiResponse<List<CountryOneResponse>> getCountryOneFlags(@RequestBody CountryOneRequest request) {
        return countryOneService.getCountryOneFlags(request);
    }

}
