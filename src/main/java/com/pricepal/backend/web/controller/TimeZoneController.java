package com.pricepal.backend.web.controller;

import com.pricepal.backend.apiPayload.ApiResponse;
import com.pricepal.backend.service.timezone.TimeZoneService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/timezone")
@RequiredArgsConstructor
public class TimeZoneController {

    private final TimeZoneService timeZoneService;

    @PostMapping
    public ApiResponse<String> getUtcOffset(@RequestBody Map<String, String> request) {
        String input = request.get("country");
        String offset = timeZoneService.getUtcOffset(input);
        return ApiResponse.onSuccess(offset);
    }
}
