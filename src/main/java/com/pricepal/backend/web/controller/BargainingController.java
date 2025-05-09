package com.pricepal.backend.web.controller;

import com.pricepal.backend.apiPayload.ApiResponse;
import com.pricepal.backend.web.dto.BargainingResponse;
import com.pricepal.backend.service.bargaining.BargainingQueryService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/bargaining")
@RequiredArgsConstructor
public class BargainingController {
    private final BargainingQueryService bargainingService;

    @GetMapping
    public ApiResponse<BargainingResponse> tips(
            @RequestParam("travelCountry") String travelCountry
    ) {
        return ApiResponse.onSuccess(
                bargainingService.getBargainingTips(travelCountry)
        );
    }
}