package com.pricepal.backend.web.controller;

import com.pricepal.backend.apiPayload.ApiResponse;
import com.pricepal.backend.service.TempService.TempTaxiService;
import com.pricepal.backend.web.dto.TempTaxiRequest;
import com.pricepal.backend.web.dto.TempTaxiResponse;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/temp")
public class TempTaxiRestController  {
    private final TempTaxiService tempTaxiGeminiService;

    public TempTaxiRestController(@Qualifier("tempTaxiServiceImpl") TempTaxiService tempTaxiService) {
        this.tempTaxiGeminiService = tempTaxiService;
    }

    @PostMapping("/taxifare")
    public ApiResponse<TempTaxiResponse> getGeminiTaxiFare(
            @RequestBody TempTaxiRequest request) {
        return tempTaxiGeminiService.getGeminiTaxiFare(request);
    }
}
