package com.mybackyard.frontend.service;

import com.mybackyard.frontend.model.Animal;

import java.io.UnsupportedEncodingException;
import java.security.GeneralSecurityException;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutionException;

public interface AnimalService {
    List<Animal> getAnimals() throws ExecutionException, InterruptedException, GeneralSecurityException, UnsupportedEncodingException;
    Animal getAnimal(String id) throws ExecutionException, InterruptedException;
    Animal postAnimal(Animal animal) throws ExecutionException, InterruptedException, GeneralSecurityException, UnsupportedEncodingException;
    Animal patchAnimal(Map patch, String id) throws ExecutionException, InterruptedException, GeneralSecurityException, UnsupportedEncodingException;
    void deleteAnimal(String id) throws ExecutionException, InterruptedException, GeneralSecurityException, UnsupportedEncodingException;
    List<Animal> searchAnimals(String query) throws ExecutionException, InterruptedException, GeneralSecurityException, UnsupportedEncodingException;
}
