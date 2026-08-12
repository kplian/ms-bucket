package com.kplian.bucket.api.config;

import com.kplian.bucket.api.service.I18nService;
import jakarta.inject.Inject;
import jakarta.ws.rs.container.ContainerRequestContext;
import jakarta.ws.rs.container.ContainerRequestFilter;
import jakarta.ws.rs.core.HttpHeaders;
import jakarta.ws.rs.ext.Provider;

@Provider
public class LocaleRequestFilter implements ContainerRequestFilter {

    @Inject
    I18nService i18nService;

    @Override
    public void filter(ContainerRequestContext requestContext) {
        String acceptLanguage = requestContext.getHeaderString(HttpHeaders.ACCEPT_LANGUAGE);
        i18nService.setLocaleFromLanguageTag(acceptLanguage);
    }
}
