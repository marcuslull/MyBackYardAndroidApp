package com.mybackyard.frontend.service;

import com.mybackyard.frontend.model.Plant;

import java.io.UnsupportedEncodingException;
import java.security.GeneralSecurityException;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutionException;

public interface PlantService {
    List<Plant> getPlants() throws ExecutionException, InterruptedException, GeneralSecurityException, UnsupportedEncodingException;
    Plant getPlant(String id) throws ExecutionException, InterruptedException;
    Plant postPlant(Plant plant) throws ExecutionException, InterruptedException, GeneralSecurityException, UnsupportedEncodingException;
    Plant patchPlant(Map patch, String id) throws ExecutionException, InterruptedException, GeneralSecurityException, UnsupportedEncodingException;
    void deletePlant(String id) throws ExecutionException, InterruptedException, GeneralSecurityException, UnsupportedEncodingException;
    List<Plant> searchPlant(String query) throws ExecutionException, InterruptedException, GeneralSecurityException, UnsupportedEncodingException;
}
