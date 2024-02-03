package com.mybackyard.frontend.service.implementation;

import static com.mybackyard.frontend.MainActivity.mbyViewModel;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.mybackyard.frontend.model.Yard;
import com.mybackyard.frontend.repository.YardRepository;
import com.mybackyard.frontend.singleton.GsonSingleton;

import java.io.UnsupportedEncodingException;
import java.security.GeneralSecurityException;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutionException;

public class YardServiceImpl implements com.mybackyard.frontend.service.YardService {

    private final YardRepository yardRepository;
    private final Gson gson = GsonSingleton.getGson();

    public YardServiceImpl(YardRepository yardRepository) {
        this.yardRepository = yardRepository;
    }

    @Override
    public List<Yard> getYards() throws ExecutionException, InterruptedException, GeneralSecurityException, UnsupportedEncodingException {
        if (mbyViewModel.getYardData().isEmpty()) {
            String retrievedJson = yardRepository.getAll();
            List<Yard> retrievedYards = gson.fromJson(retrievedJson, new TypeToken<List<Yard>>() {}.getType());
            if (retrievedYards != null) {
                retrievedYards.forEach(mbyViewModel::addYard);
            }
            return retrievedYards;
        }
        return mbyViewModel.getYardData();
    }

    @Override
    public Yard getYard(String id) throws ExecutionException, InterruptedException {
        List<Yard> yards = mbyViewModel.getYardData();
        Yard retrievedYard = null;
        for (Yard yard : yards) {
            if (yard.getId() == Long.parseLong(id)) {
                retrievedYard = yard;
            }
        }
        return retrievedYard;
    }

    @Override
    public Yard postYard(Yard yard) throws ExecutionException, InterruptedException, GeneralSecurityException, UnsupportedEncodingException {
        // TODO: This needs to be fixed in the backend
        yard.setUserId(1);

        mbyViewModel.invalidateYard();
        String serializedYard = gson.toJson(yard);
        String retrievedJson = yardRepository.save(serializedYard);
        return gson.fromJson(retrievedJson, new TypeToken<Yard>() {}.getType());
    }

    @Override
    public Yard patchYard(Map patch, String id) throws ExecutionException, InterruptedException, GeneralSecurityException, UnsupportedEncodingException {
        mbyViewModel.invalidateYard();
        String serializedPatch = gson.toJson(patch);
        String retrievedJson = yardRepository.patch(serializedPatch, id);
        return gson.fromJson(retrievedJson, new TypeToken<Yard>() {}.getType());
    }

    @Override
    public void deleteYard(String id) throws ExecutionException, InterruptedException, GeneralSecurityException, UnsupportedEncodingException {
        mbyViewModel.invalidateYard();
        yardRepository.delete(id);
    }

    @Override
    public List<Yard> searchYards(String query) throws ExecutionException, InterruptedException, GeneralSecurityException, UnsupportedEncodingException {
        String retrievedJson = yardRepository.search(query);
        return gson.fromJson(retrievedJson, new TypeToken<List<Yard>>() {}.getType());
    }
}
