package com.mybackyard.frontend.service.implementation;

import static com.mybackyard.frontend.MainActivity.mbyViewModel;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.mybackyard.frontend.model.Image;
import com.mybackyard.frontend.repository.ImageRepository;
import com.mybackyard.frontend.singleton.GsonSingleton;

import java.io.UnsupportedEncodingException;
import java.security.GeneralSecurityException;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutionException;

public class ImageServiceImpl implements com.mybackyard.frontend.service.ImageService {

    private final ImageRepository imageRepository;
    private final Gson gson = GsonSingleton.getGson();

    public ImageServiceImpl(ImageRepository imageRepository) {
        this.imageRepository = imageRepository;
    }

    @Override
    public List<Image> getImages() throws ExecutionException, InterruptedException, GeneralSecurityException, UnsupportedEncodingException {
        if (mbyViewModel.getImageData().isEmpty()) {
            String retrievedJson = imageRepository.getAll();
            List<Image> retrievedImages = gson.fromJson(retrievedJson, new TypeToken<List<Image>>() {}.getType());
            retrievedImages.forEach(mbyViewModel::addImage);
        }
        return mbyViewModel.getImageData();
    }

    @Override
    public Image getImage(String id) throws ExecutionException, InterruptedException {
        List<Image> images = mbyViewModel.getImageData();
        Image retrievedImage = null;
        for (Image image : images) {
            if (image.getId() == Long.parseLong(id)) {
                retrievedImage = image;
            }
        }
        return retrievedImage;
    }

    @Override
    public Image postImage(Image image) throws ExecutionException, InterruptedException, GeneralSecurityException, UnsupportedEncodingException {
        mbyViewModel.invalidateImage();
        String serializedImage = gson.toJson(image);
        String retrievedJson = imageRepository.save(serializedImage);
        return gson.fromJson(retrievedJson, new TypeToken<Image>() {}.getType());
    }

    @Override
    public Image patchImage(Map patch, String id) throws ExecutionException, InterruptedException, GeneralSecurityException, UnsupportedEncodingException {
        mbyViewModel.invalidateImage();
        String serializedPatch = gson.toJson(patch);
        String retrievedJson = imageRepository.patch(serializedPatch, id);
        return gson.fromJson(retrievedJson, new TypeToken<Image>() {}.getType());
    }

    @Override
    public void deleteImage(String id) throws ExecutionException, InterruptedException, GeneralSecurityException, UnsupportedEncodingException {
        mbyViewModel.invalidateImage();
        imageRepository.delete(id);
    }
}
