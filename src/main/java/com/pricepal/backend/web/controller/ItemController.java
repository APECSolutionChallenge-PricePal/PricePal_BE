package com.pricepal.backend.web.controller;

import com.pricepal.backend.apiPayload.ApiResponse;
import com.pricepal.backend.web.dto.PriceDto;
import com.pricepal.backend.service.item.PriceQueryService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/items/prices")
@RequiredArgsConstructor
public class ItemController {
    private final PriceQueryService priceService;

    @GetMapping
    public ApiResponse<List<PriceDto>> list(@RequestParam(defaultValue="Thailand") String country) {
        return ApiResponse.onSuccess(priceService.getPriceList(country));
    }
}