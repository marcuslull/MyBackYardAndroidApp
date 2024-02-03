package com.mybackyard.frontend.repository;

import java.io.UnsupportedEncodingException;
import java.security.GeneralSecurityException;
import java.util.concurrent.ExecutionException;

public interface AnimalRepository {
    String getAll() throws ExecutionException, InterruptedException, GeneralSecurityException, UnsupportedEncodingException;
    String get(String id) throws ExecutionException, InterruptedException, GeneralSecurityException, UnsupportedEncodingException;
    String save(String animal) throws ExecutionException, InterruptedException, GeneralSecurityException, UnsupportedEncodingException;
    String patch(String patch, String id) throws ExecutionException, InterruptedException, GeneralSecurityException, UnsupportedEncodingException;
    void delete(String id) throws ExecutionException, InterruptedException, GeneralSecurityException, UnsupportedEncodingException;
    String search(String query) throws ExecutionException, InterruptedException, GeneralSecurityException, UnsupportedEncodingException;
}
