package com.pricepal.backend.web.controller;

import com.pricepal.backend.apiPayload.ApiResponse;
import com.pricepal.backend.web.dto.PriceDto;
import com.pricepal.backend.service.item.PriceQueryService;
import com.pricepal.backend.web.dto.PriceRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/prices")
@RequiredArgsConstructor
public class PriceController {
    private final PriceQueryService priceQueryService;

    @PostMapping
    public ApiResponse<List<PriceDto>> list(@RequestBody PriceRequest request) {
        return ApiResponse.onSuccess(
                priceQueryService.getPriceList(request.getUserCountry(), request.getTravelCountry())
        );
    }
}