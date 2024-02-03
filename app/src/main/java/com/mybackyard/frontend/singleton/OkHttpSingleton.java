package com.mybackyard.frontend.singleton;

import okhttp3.OkHttpClient;

public class OkHttpSingleton {

    private static OkHttpClient okHttpClient;

    private OkHttpSingleton() {}

    public static OkHttpClient getInstance() {
        if (okHttpClient == null) {
            okHttpClient = new OkHttpClient.Builder()
                    .build();
        }
        return okHttpClient;
    }
}
