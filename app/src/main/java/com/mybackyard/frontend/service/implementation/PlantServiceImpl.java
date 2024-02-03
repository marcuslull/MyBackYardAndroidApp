package com.mybackyard.frontend.service.implementation;

import static com.mybackyard.frontend.MainActivity.mbyViewModel;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.mybackyard.frontend.model.Plant;
import com.mybackyard.frontend.repository.PlantRepository;
import com.mybackyard.frontend.singleton.GsonSingleton;

import java.io.UnsupportedEncodingException;
import java.security.GeneralSecurityException;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutionException;

public class PlantServiceImpl implements com.mybackyard.frontend.service.PlantService {

    private final PlantRepository plantRepository;
    private final Gson gson = GsonSingleton.getGson();

    public PlantServiceImpl(PlantRepository plantRepository) {
        this.plantRepository = plantRepository;
    }

    @Override
    public List<Plant> getPlants() throws ExecutionException, InterruptedException, GeneralSecurityException, UnsupportedEncodingException {
        if (mbyViewModel.getPlantData().isEmpty()) {
            String retrievedJson = plantRepository.getAll();
            List<Plant> retrievedPlants = gson.fromJson(retrievedJson, new TypeToken<List<Plant>>() {}.getType());
            retrievedPlants.forEach(mbyViewModel::addPlant);
        }
        return mbyViewModel.getPlantData();
    }

    @Override
    public Plant getPlant(String id) throws ExecutionException, InterruptedException {
        List<Plant> plants = mbyViewModel.getPlantData();
        Plant retrievedPlant = null;
        for (Plant plant : plants) {
            if (plant.getId() == Long.parseLong(id)) {
                retrievedPlant = plant;
            }
        }
        return retrievedPlant;
    }

    @Override
    public Plant postPlant(Plant plant) throws ExecutionException, InterruptedException, GeneralSecurityException, UnsupportedEncodingException {
        mbyViewModel.invalidatePlant();
        String serializedPlant = gson.toJson(plant);
        String retrievedJson = plantRepository.save(serializedPlant);
        return gson.fromJson(retrievedJson, new TypeToken<Plant>() {}.getType());
    }

    @Override
    public Plant patchPlant(Map patch, String id) throws ExecutionException, InterruptedException, GeneralSecurityException, UnsupportedEncodingException {
        mbyViewModel.invalidatePlant();
        String serializedPatch = gson.toJson(patch);
        String retrievedJson = plantRepository.patch(serializedPatch, id);
        return gson.fromJson(retrievedJson, new TypeToken<Plant>() {}.getType());
    }

    @Override
    public void deletePlant(String id) throws ExecutionException, InterruptedException, GeneralSecurityException, UnsupportedEncodingException {
        mbyViewModel.invalidatePlant();
        plantRepository.delete(id);
    }

    @Override
    public List<Plant> searchPlant(String query) throws ExecutionException, InterruptedException, GeneralSecurityException, UnsupportedEncodingException {
        String retrievedJson = plantRepository.search(query);
        return gson.fromJson(retrievedJson, new TypeToken<List<Plant>>() {}.getType());
    }
}
