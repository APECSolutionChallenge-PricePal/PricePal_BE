package com.pricepal.backend.web.controller;

import com.pricepal.backend.apiPayload.ApiResponse;
import com.pricepal.backend.converter.TempConverter;
import com.pricepal.backend.service.TempService.TempQueryService;
import com.pricepal.backend.web.dto.TempRequest;
import com.pricepal.backend.web.dto.TempResponse;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/temp")
//@RequiredArgsConstructor
public class TempRestController {
    private final TempQueryService tempQueryService;
    public TempRestController(@Qualifier("tempQueryServiceImpl") TempQueryService tempQueryService) {
        this.tempQueryService = tempQueryService;
    }

    @PostMapping("/guide")
    public ApiResponse<String> getGeminiGuide(@RequestBody TempRequest request) {
        return tempQueryService.getGeminiGuide(request);
    }

//    @GetMapping("/test")
//    public ApiResponse<TempResponse.TempTestDTO> testAPI(){
//
//        return ApiResponse.onSuccess(TempConverter.toTempTestDTO());
//    }

//    @GetMapping("/exception")
//    public ApiResponse<TempResponse.TempExceptionDTO> exceptionAPI(@RequestParam Integer flag){
//        tempQueryService.CheckFlag(flag);
//        return ApiResponse.onSuccess(TempConverter.toTempExceptionDTO(flag));
//    }
}
