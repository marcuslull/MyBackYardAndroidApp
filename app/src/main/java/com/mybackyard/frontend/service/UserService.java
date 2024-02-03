package com.mybackyard.frontend.service;

import com.mybackyard.frontend.model.User;

import java.io.UnsupportedEncodingException;
import java.security.GeneralSecurityException;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutionException;

public interface UserService {
    List<User> getUsers() throws ExecutionException, InterruptedException, GeneralSecurityException, UnsupportedEncodingException;
    User getUser(String id) throws ExecutionException, InterruptedException, GeneralSecurityException, UnsupportedEncodingException;
    User postUser(User animal) throws ExecutionException, InterruptedException, GeneralSecurityException, UnsupportedEncodingException;
    User patchUser(Map patch, String id) throws ExecutionException, InterruptedException, GeneralSecurityException, UnsupportedEncodingException;
    void deleteUser(String id) throws ExecutionException, InterruptedException, GeneralSecurityException, UnsupportedEncodingException;
}
