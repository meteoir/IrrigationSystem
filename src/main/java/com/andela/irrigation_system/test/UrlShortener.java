package com.andela.irrigation_system.test;

import java.net.URL;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;

import static java.util.Objects.nonNull;

public class UrlShortener {

    private static final String SHORT_DOMAIN = "http://short.com/";

    private AtomicInteger counter;
    private final Map<String, String> shortenedUrls = new ConcurrentHashMap<>();

    public String shorten(String url, String keyword) {
        if (!validate(url, keyword)) {
            throw new IllegalStateException(String.format("validation failed, url:%s, keyword:%s", url, keyword));
        }
        shortenedUrls.put(keyword, url);
        return getShortenedUrl(keyword);
    }

    private boolean validate(String link, String keyword) {
        return validateUrl(link) && validateKeyword(keyword);
    }

    private boolean validateUrl(String link) {
        try {
            URL url = new URL(link);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    private boolean validateKeyword(String keyword) {
        return nonNull(keyword) && keyword.length() <= 20;
    }

    public String getShortenedUrl(String keyword) {
        return SHORT_DOMAIN + keyword;
    }

    public String getInitialLink(String keyword) {
        return shortenedUrls.get(keyword);
    }
}

