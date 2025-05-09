package com.pricepal.backend.web.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.ToString;

import java.util.List;

@Getter
@ToString
@Builder  // ← 이 한 줄을 추가!
public class BargainingResponse {
    private final List<String> tips;
    private final String summary;
}
