package com.pricepal.backend.web.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.util.List;

@Data
public class CountryFlagApiResponse {
    @JsonProperty("resultCode")
    private String resultCode;

    @JsonProperty("resultMsg")
    private String resultMsg;

    @JsonProperty("currentCount")
    private int currentCount;

    @JsonProperty("numOfRows")
    private int numOfRows;

    @JsonProperty("pageNo")
    private int pageNo;

    @JsonProperty("totalCount")
    private int totalCount;

    @JsonProperty("data")
    private List<CountryFlagData> data;


    @Data
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class CountryFlagData {
        @JsonProperty("content_ty")
        private String contentType;

        @JsonProperty("country_eng_nm")
        private String countryEngNm;

        @JsonProperty("country_nm")
        private String countryNm;

        @JsonProperty("country_iso_alp2")
        private String countryIsoAlp2;

        @JsonProperty("download_url")
        private String downloadUrl;

        @JsonProperty("origin_file_nm")
        private String originFileName;
    }
}
