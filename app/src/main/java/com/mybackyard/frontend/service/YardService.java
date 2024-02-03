package com.mybackyard.frontend.service;

import com.mybackyard.frontend.model.Yard;

import java.io.UnsupportedEncodingException;
import java.security.GeneralSecurityException;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutionException;

public interface YardService {
    List<Yard> getYards() throws ExecutionException, InterruptedException, GeneralSecurityException, UnsupportedEncodingException;
    Yard getYard(String id) throws ExecutionException, InterruptedException;
    Yard postYard(Yard yard) throws ExecutionException, InterruptedException, GeneralSecurityException, UnsupportedEncodingException;
    Yard patchYard(Map patch, String id) throws ExecutionException, InterruptedException, GeneralSecurityException, UnsupportedEncodingException;
    void deleteYard(String id) throws ExecutionException, InterruptedException, GeneralSecurityException, UnsupportedEncodingException;
    List<Yard> searchYards(String query) throws ExecutionException, InterruptedException, GeneralSecurityException, UnsupportedEncodingException;
}
