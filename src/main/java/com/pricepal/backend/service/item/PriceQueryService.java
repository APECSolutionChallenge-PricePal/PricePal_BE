package com.pricepal.backend.service.item;

import com.pricepal.backend.web.dto.PriceDto;
import java.util.List;

public interface PriceQueryService {
    List<PriceDto> getPriceList(String country);
}