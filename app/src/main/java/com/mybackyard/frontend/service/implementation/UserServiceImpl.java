package com.mybackyard.frontend.service.implementation;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.mybackyard.frontend.model.User;
import com.mybackyard.frontend.repository.UserRepository;
import com.mybackyard.frontend.singleton.GsonSingleton;

import java.io.UnsupportedEncodingException;
import java.security.GeneralSecurityException;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutionException;

public class UserServiceImpl implements com.mybackyard.frontend.service.UserService {

    private final UserRepository userRepository;
    private final Gson gson = GsonSingleton.getGson();

    public UserServiceImpl(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public List<User> getUsers() throws ExecutionException, InterruptedException, GeneralSecurityException, UnsupportedEncodingException {
        String retrievedJson = userRepository.getAll();
        return gson.fromJson(retrievedJson, new TypeToken<List<User>>() {}.getType());
    }

    @Override
    public User getUser(String id) throws ExecutionException, InterruptedException, GeneralSecurityException, UnsupportedEncodingException {
        String retrievedJson = userRepository.get(id);
        return gson.fromJson(retrievedJson, new TypeToken<User>() {}.getType());
    }

    @Override
    public User postUser(User user) throws ExecutionException, InterruptedException, GeneralSecurityException, UnsupportedEncodingException {
        String serializedUser = gson.toJson(user);
        String retrievedJson = userRepository.save(serializedUser);
        return gson.fromJson(retrievedJson, new TypeToken<User>() {}.getType());
    }

    @Override
    public User patchUser(Map patch, String id) throws ExecutionException, InterruptedException, GeneralSecurityException, UnsupportedEncodingException {
        String serializedPatch = gson.toJson(patch);
        String retrievedJson = userRepository.patch(serializedPatch, id);
        return gson.fromJson(retrievedJson, new TypeToken<User>() {}.getType());
    }

    @Override
    public void deleteUser(String id) throws ExecutionException, InterruptedException, GeneralSecurityException, UnsupportedEncodingException {
        userRepository.delete(id);
    }
}
