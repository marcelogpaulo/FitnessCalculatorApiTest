package utils;

import constants.Constants;

import java.util.Map;

public class HeaderParams implements Constants {

    public static Map<String, String> getApiKeyHeader() {
        return Map.of(API_KEY_KEY, API_KEY_VALUE);
    }

    public static Map<String, String> getApiHostHeader() {
        return Map.of(API_HOST_KEY, API_HOST_VALUE);
    }

}
