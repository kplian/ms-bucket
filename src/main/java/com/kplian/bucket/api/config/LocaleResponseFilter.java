package com.kplian.bucket.api.config;

import com.kplian.bucket.api.service.I18nService;
import jakarta.inject.Inject;
import jakarta.ws.rs.container.ContainerRequestContext;
import jakarta.ws.rs.container.ContainerResponseContext;
import jakarta.ws.rs.container.ContainerResponseFilter;
import jakarta.ws.rs.ext.Provider;

@Provider
public class LocaleResponseFilter implements ContainerResponseFilter {

    @Inject
    I18nService i18nService;

    @Override
    public void filter(ContainerRequestContext requestContext, ContainerResponseContext responseContext) {
        i18nService.clearLocale();
    }
}
