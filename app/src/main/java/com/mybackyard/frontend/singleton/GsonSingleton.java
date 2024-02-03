package com.mybackyard.frontend.singleton;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

public class GsonSingleton {
    private static final Gson gson = new GsonBuilder().create();

    private GsonSingleton() {}

    public static Gson getGson() {
        return gson;
    }
}
