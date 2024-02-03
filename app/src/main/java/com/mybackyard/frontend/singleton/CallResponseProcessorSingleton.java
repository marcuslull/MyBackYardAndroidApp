package com.mybackyard.frontend.singleton;

import android.util.Log;

import java.io.IOException;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class CallResponseProcessorSingleton {

    private static CallResponseProcessorSingleton callResponseProcessorSingleton;

    private CallResponseProcessorSingleton(){}

    public static CallResponseProcessorSingleton getInstance() {
        if (callResponseProcessorSingleton == null) {
            callResponseProcessorSingleton = new CallResponseProcessorSingleton();
        }
        return callResponseProcessorSingleton;
    }

    public String get(OkHttpClient client, Request request) {

        try {
            Response response = client.newCall(request).execute();
            if (response.isSuccessful()) {
                if (response.body() != null) {
                    String responseBodyString = response.body().string();
                    response.close();
                    return responseBodyString;
                }
            } else {
                // TODO: Handle failed responses
                Log.e("TAG", "Response failed: " + response.code());
            }
            response.close();
        } catch (IOException e) {
            // TODO: Handle Exceptions
            e.printStackTrace();
        }
        return null;
    }
}


