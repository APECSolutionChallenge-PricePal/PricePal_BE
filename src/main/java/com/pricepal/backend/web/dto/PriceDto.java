package com.pricepal.backend.web.dto;

public record PriceDto(
        String name,
        String priceKRW,
        String priceUSD
) { }