package com.kplian.bucket.domain.exception;

import com.kplian.bucket.api.service.I18nService;

public class I18nBusinessException extends BusinessException {

    public I18nBusinessException(I18nService i18nService, String i18nKey, String code, Object... params) {
        super(i18nService.get(i18nKey, params), code);
    }
}
