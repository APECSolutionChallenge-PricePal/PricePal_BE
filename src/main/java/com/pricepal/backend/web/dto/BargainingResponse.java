package com.pricepal.backend.web.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.ToString;

import java.util.List;

@Getter
@ToString
@Builder
public class BargainingResponse {
    private final List<BargainingTip> tips;  // ✅ 객체로 바꿈
    private final String summary;
}