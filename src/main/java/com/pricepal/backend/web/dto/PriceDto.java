package com.pricepal.backend.web.dto;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class PriceDto {
    private String itemName;
    private String userPrice;     // ex: "800 KRW"
    private String travelPrice;   // ex: "0.54 USD"
    private String image;
}
