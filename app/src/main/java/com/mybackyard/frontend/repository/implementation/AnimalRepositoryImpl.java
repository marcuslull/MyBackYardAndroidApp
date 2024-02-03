package com.mybackyard.frontend.repository.implementation;

import static com.mybackyard.frontend.MainActivity.decrypt;
import static com.mybackyard.frontend.MainActivity.encryptedApiKey;
import static com.mybackyard.frontend.MainActivity.protectionParametersPassword;

import android.util.Base64;

import com.mybackyard.frontend.singleton.CallResponseProcessorSingleton;
import com.mybackyard.frontend.singleton.OkHttpSingleton;
import com.mybackyard.frontend.singleton.ThreadExecutorSingleton;

import java.io.UnsupportedEncodingException;
import java.security.GeneralSecurityException;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Future;

import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;

public class AnimalRepositoryImpl implements com.mybackyard.frontend.repository.AnimalRepository {

    private final OkHttpClient client = OkHttpSingleton.getInstance();
    private final ExecutorService executor = ThreadExecutorSingleton.getSingleThread();
    private final CallResponseProcessorSingleton callResponse = CallResponseProcessorSingleton.getInstance();
//    private final String baseUrl = "http://192.168.0.139:8080/api/animal";
    private final String baseUrl = "https://mybackyard-54fdfelt3a-uw.a.run.app/api/animal";
    private final String apiKeyName = "X-API-KEY";
    private final MediaType mediaType = MediaType.parse("application/json; charset=utf-8");


    @Override
    public String getAll() throws ExecutionException, InterruptedException, GeneralSecurityException, UnsupportedEncodingException {
        Request request = new Request.Builder()
                .url(baseUrl)
                .header(apiKeyName, decrypt(Base64.decode(encryptedApiKey, Base64.DEFAULT), protectionParametersPassword))
                .build();

        Future<String> future = executor.submit(() -> callResponse.get(client, request));
        return future.get();
    }

    @Override
    public String get(String id) throws ExecutionException, InterruptedException, GeneralSecurityException, UnsupportedEncodingException {
        Request request = new Request.Builder()
                .url(baseUrl + "/" + id)
                .header(apiKeyName, decrypt(Base64.decode(encryptedApiKey, Base64.DEFAULT), protectionParametersPassword))
                .build();

        Future<String> future = executor.submit(() -> callResponse.get(client, request));
        return future.get();
    }

    @Override
    public String save(String animal) throws ExecutionException, InterruptedException, GeneralSecurityException, UnsupportedEncodingException {
        Request request = new Request.Builder()
                .url(baseUrl)
                .header(apiKeyName, decrypt(Base64.decode(encryptedApiKey, Base64.DEFAULT), protectionParametersPassword))
                .post(RequestBody.create(animal, mediaType))
                .build();

        Future<String> future = executor.submit(() -> callResponse.get(client, request));
        return future.get();
    }

    @Override
    public String patch(String patch, String id) throws ExecutionException, InterruptedException, GeneralSecurityException, UnsupportedEncodingException {
        Request request = new Request.Builder()
                .url(baseUrl + "/" + id)
                .header(apiKeyName, decrypt(Base64.decode(encryptedApiKey, Base64.DEFAULT), protectionParametersPassword))
                .patch(RequestBody.create(patch, mediaType))
                .build();

        Future<String> future = executor.submit(() -> callResponse.get(client, request));
        return future.get();
    }

    @Override
    public void delete(String id) throws ExecutionException, InterruptedException, GeneralSecurityException, UnsupportedEncodingException {
        Request request = new Request.Builder()
                .url(baseUrl + "/" + id)
                .header(apiKeyName, decrypt(Base64.decode(encryptedApiKey, Base64.DEFAULT), protectionParametersPassword))
                .delete()
                .build();

        executor.submit(() -> callResponse.get(client, request));
    }

    @Override
    public String search(String query) throws ExecutionException, InterruptedException, GeneralSecurityException, UnsupportedEncodingException {
        Request request = new Request.Builder()
                .url(baseUrl + "/search?name=" + query)
                .header(apiKeyName, decrypt(Base64.decode(encryptedApiKey, Base64.DEFAULT), protectionParametersPassword))
                .get()
                .build();

        Future<String> future = executor.submit(() -> callResponse.get(client, request));
        return future.get();
    }
}
