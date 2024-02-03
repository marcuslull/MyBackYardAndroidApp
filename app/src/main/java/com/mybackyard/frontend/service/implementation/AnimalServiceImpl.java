package com.mybackyard.frontend.service.implementation;

import static com.mybackyard.frontend.MainActivity.mbyViewModel;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.mybackyard.frontend.model.Animal;
import com.mybackyard.frontend.repository.AnimalRepository;
import com.mybackyard.frontend.singleton.GsonSingleton;

import java.io.UnsupportedEncodingException;
import java.security.GeneralSecurityException;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutionException;


public class AnimalServiceImpl implements com.mybackyard.frontend.service.AnimalService {

    private final AnimalRepository animalRepository;
    private final Gson gson = GsonSingleton.getGson();

    public AnimalServiceImpl(AnimalRepository animalRepository) {
        this.animalRepository = animalRepository;
    }

    @Override
    public List<Animal> getAnimals() throws ExecutionException, InterruptedException, GeneralSecurityException, UnsupportedEncodingException {
        if (mbyViewModel.getAnimalData().isEmpty()) {
            String retrievedJson = animalRepository.getAll();
            List<Animal> retrievedAnimals = gson.fromJson(retrievedJson, new TypeToken<List<Animal>>() {}.getType());
            retrievedAnimals.forEach(mbyViewModel::addAnimal);
            }
        return mbyViewModel.getAnimalData();
    }

    @Override
    public Animal getAnimal(String id) throws ExecutionException, InterruptedException {
        List<Animal> animals = mbyViewModel.getAnimalData();
        Animal retrievedAnimal = null;
        for (Animal animal : animals) {
            if (animal.getId() == Long.parseLong(id)) {
                retrievedAnimal = animal;
            }
        }
        return retrievedAnimal;
    }

    @Override
    public Animal postAnimal(Animal animal) throws ExecutionException, InterruptedException, GeneralSecurityException, UnsupportedEncodingException {
        mbyViewModel.invalidateAnimal();
        String serializedAnimal = gson.toJson(animal);
        String retrievedJson = animalRepository.save(serializedAnimal);
        return gson.fromJson(retrievedJson, new TypeToken<Animal>() {}.getType());
    }

    @Override
    public Animal patchAnimal(Map patch, String id) throws ExecutionException, InterruptedException, GeneralSecurityException, UnsupportedEncodingException {
        mbyViewModel.invalidateAnimal(); // HERE
        String serializedPatch = gson.toJson(patch);
        String retrievedJson = animalRepository.patch(serializedPatch, id);
        return gson.fromJson(retrievedJson, new TypeToken<Animal>() {}.getType());
    }

    @Override
    public void deleteAnimal(String id) throws ExecutionException, InterruptedException, GeneralSecurityException, UnsupportedEncodingException {
        mbyViewModel.invalidateAnimal();
        animalRepository.delete(id);
    }

    @Override
    public List<Animal> searchAnimals(String query) throws ExecutionException, InterruptedException, GeneralSecurityException, UnsupportedEncodingException {
        String retrievedJson = animalRepository.search(query);
        return gson.fromJson(retrievedJson, new TypeToken<List<Animal>>() {}.getType());
    }
}
