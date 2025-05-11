package com.pricepal.backend.web.controller;

import com.pricepal.backend.apiPayload.ApiResponse;
import com.pricepal.backend.web.dto.BargainingRequest;
import com.pricepal.backend.web.dto.BargainingResponse;
import com.pricepal.backend.service.bargaining.BargainingQueryService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/bargain")
@RequiredArgsConstructor
public class BargainingController {

    private final BargainingQueryService bargainingService;

    @GetMapping
    public String test() {
        return "서버 잘 돌아가는 중!";
    }

    @PostMapping
    public ApiResponse<BargainingResponse> tips(@RequestBody BargainingRequest request) {
        return ApiResponse.onSuccess(
                bargainingService.getBargainingTips(request.getTravelCountry())
        );
    }
}
