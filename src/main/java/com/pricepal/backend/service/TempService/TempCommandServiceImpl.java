package com.pricepal.backend.service.TempService;

import com.pricepal.backend.apiPayload.code.status.ErrorStatus;
import com.pricepal.backend.apiPayload.exception.handler.TempHandler;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class TempCommandServiceImpl implements TempQueryService{
    @Override
    public void CheckFlag(Integer flag) {
    }
}
