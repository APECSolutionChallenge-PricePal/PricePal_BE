package com.pricepal.backend.service.exchangerate;

public interface ExchangeRateService {
    String getRate(String base, String target);  // 예: KRW -> USD
}

