package com.kplian.bucket.api.service;

import jakarta.enterprise.context.ApplicationScoped;


import java.text.MessageFormat;
import java.util.Locale;
import java.util.MissingResourceException;
import java.util.ResourceBundle;

@ApplicationScoped
public class I18nService {

    private static final String BUNDLE_NAME = "i18N.message";
    private static final Locale DEFAULT_LOCALE = Locale.of("es", "ES");
    private final ThreadLocal<Locale> localeHolder = new ThreadLocal<>();

    public void setLocale(Locale locale) {
        localeHolder.set(locale != null ? locale : DEFAULT_LOCALE);
    }

    public void setLocaleFromLanguageTag(String tag) {
        if (tag == null || tag.trim().isEmpty()) {
            setLocale(DEFAULT_LOCALE);
        } else {
            setLocale(Locale.forLanguageTag(tag.split(",")[0].trim()));
        }
    }

    public void clearLocale() {
        localeHolder.remove();
    }

    public Locale getLocale() {
        Locale loc = localeHolder.get();
        return loc != null ? loc : DEFAULT_LOCALE;
    }

    public String get(String key, Object... params) {
        try {
            ResourceBundle bundle = ResourceBundle.getBundle(BUNDLE_NAME, getLocale());
            String message = bundle.getString(key);
            if (params != null && params.length > 0) {
                return MessageFormat.format(message, params);
            }
            return message;
        } catch (MissingResourceException e) {
            return key;
        }
    }

    public boolean hasKey(String key, Locale locale) {
        try {
            ResourceBundle bundle = ResourceBundle.getBundle(BUNDLE_NAME, locale != null ? locale : DEFAULT_LOCALE);
            bundle.getString(key);
            return true;
        } catch (MissingResourceException e) {
            return false;
        }
    }
}
