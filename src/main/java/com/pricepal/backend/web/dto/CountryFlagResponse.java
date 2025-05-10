package com.pricepal.backend.web.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CountryFlagResponse {
    @JsonProperty("country_eng_nm")
    private String countryEngNm;

    @JsonProperty("download_url")
    private String downloadUrl;

    @JsonProperty("country_iso_alp2")
    private String countryIsoAlp2;
}
