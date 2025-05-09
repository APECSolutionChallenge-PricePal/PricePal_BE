package com.pricepal.backend.web.dto;

import com.fasterxml.jackson.annotation.JsonInclude;

@JsonInclude(JsonInclude.Include.NON_NULL)
public record BargainingTip(
        String romanization,  // 비라틴 스크립트일 때만 값
        String local,         // 현지어 표현
        String english        // 영어 번역
) { }
