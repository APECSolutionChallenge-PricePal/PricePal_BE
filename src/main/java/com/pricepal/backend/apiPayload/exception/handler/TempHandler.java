package com.pricepal.backend.apiPayload.exception.handler;

import com.pricepal.backend.apiPayload.code.BaseErrorCode;
import com.pricepal.backend.apiPayload.exception.GeneralException;

public class TempHandler extends GeneralException {

    public TempHandler(BaseErrorCode errorCode) {
        super(errorCode);
    }
}