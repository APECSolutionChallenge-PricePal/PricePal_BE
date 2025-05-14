package com.pricepal.backend.web.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Builder
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class CountryOneResponse {
    String countryEngNm;
    String downloadUrl;
    String countryIsoAlp2;
}
