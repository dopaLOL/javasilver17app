package com.example.java;

import com.contentful.java.cda.CDAClient;

public class ContentfulManager {
    private static final String ACCESS_TOKEN = BuildConfig.CONTENTFUL_API_KEY;
    private static final String SPACE_ID = BuildConfig.CONTENTFUL_SPACE_ID;

    private static CDAClient client;

    public static CDAClient getClient() {
        if (client == null) {
            client = CDAClient.builder()
                    .setSpace(SPACE_ID)
                    .setToken(ACCESS_TOKEN)
                    .build();
        }
        return client;
    }
}
