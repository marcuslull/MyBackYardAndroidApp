package com.mybackyard.frontend.service;

import com.mybackyard.frontend.model.Image;

import java.io.UnsupportedEncodingException;
import java.security.GeneralSecurityException;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutionException;

public interface ImageService {
    List<Image> getImages() throws ExecutionException, InterruptedException, GeneralSecurityException, UnsupportedEncodingException;
    Image getImage(String id) throws ExecutionException, InterruptedException;
    Image postImage(Image image) throws ExecutionException, InterruptedException, GeneralSecurityException, UnsupportedEncodingException;
    Image patchImage(Map patch, String id) throws ExecutionException, InterruptedException, GeneralSecurityException, UnsupportedEncodingException;
    void deleteImage(String id) throws ExecutionException, InterruptedException, GeneralSecurityException, UnsupportedEncodingException;
}
