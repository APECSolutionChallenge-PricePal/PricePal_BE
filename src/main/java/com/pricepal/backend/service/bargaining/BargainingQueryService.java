package com.pricepal.backend.service.bargaining;

import com.pricepal.backend.web.dto.BargainingResponse;

public interface BargainingQueryService {
    BargainingResponse getBargainingTips(String travelCountry);
}
