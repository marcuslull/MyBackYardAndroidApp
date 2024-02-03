package com.mybackyard.frontend;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Base64;
import android.view.Menu;
import android.view.MenuItem;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.mybackyard.frontend.repository.AnimalRepository;
import com.mybackyard.frontend.repository.ImageRepository;
import com.mybackyard.frontend.repository.NoteRepository;
import com.mybackyard.frontend.repository.PlantRepository;
import com.mybackyard.frontend.repository.YardRepository;
import com.mybackyard.frontend.repository.implementation.AnimalRepositoryImpl;
import com.mybackyard.frontend.repository.implementation.ImageRepositoryImpl;
import com.mybackyard.frontend.repository.implementation.NoteRepositoryImpl;
import com.mybackyard.frontend.repository.implementation.PlantRepositoryImpl;
import com.mybackyard.frontend.repository.implementation.YardRepositoryImpl;
import com.mybackyard.frontend.service.AnimalService;
import com.mybackyard.frontend.service.ImageService;
import com.mybackyard.frontend.service.NoteService;
import com.mybackyard.frontend.service.PlantService;
import com.mybackyard.frontend.service.YardService;
import com.mybackyard.frontend.service.implementation.AnimalServiceImpl;
import com.mybackyard.frontend.service.implementation.ImageServiceImpl;
import com.mybackyard.frontend.service.implementation.NoteServiceImpl;
import com.mybackyard.frontend.service.implementation.PlantServiceImpl;
import com.mybackyard.frontend.service.implementation.YardServiceImpl;
import com.mybackyard.frontend.validation.InputValidator;
import com.mybackyard.frontend.validation.InputValidatorImpl;
import com.mybackyard.frontend.viewmodel.MbyViewModel;
import com.mybackyard.frontend.viewmodel.MbyViewModelImpl;

import java.io.UnsupportedEncodingException;
import java.security.GeneralSecurityException;

import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;

public class MainActivity extends AppCompatActivity {

    public static final YardRepository yardRepository = new YardRepositoryImpl();
    public static final YardService yardService = new YardServiceImpl(yardRepository);
    public static final NoteRepository noteRepository = new NoteRepositoryImpl();
    public static final NoteService noteService = new NoteServiceImpl(noteRepository);
    public static final PlantRepository plantRepository = new PlantRepositoryImpl();
    public static final PlantService plantService = new PlantServiceImpl(plantRepository);
    public static final AnimalRepository animalRepository = new AnimalRepositoryImpl();
    public static final AnimalService animalService = new AnimalServiceImpl(animalRepository);
    public static final ImageRepository imageRepository = new ImageRepositoryImpl();
    public static final ImageService imageService = new ImageServiceImpl(imageRepository);
    public static final MbyViewModel mbyViewModel = new MbyViewModelImpl();
    public static final InputValidator inputValidator = new InputValidatorImpl();
    public static String encryptedApiKey;
    public static SharedPreferences sharedPreferences;

    // TODO: best practice - generate this from a device bound key needs to be 16 bytes
    public static String protectionParametersPassword = "7944944393323942";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }

        sharedPreferences = getSharedPreferences("private_prefs", MODE_PRIVATE);
        encryptedApiKey = sharedPreferences.getString("apiKey", null);
        Fragment fragment;
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        if (encryptedApiKey == null) {
            fragment = new AdminFragment();
            fragmentTransaction.add(R.id.fragmentContainerView, fragment).addToBackStack(null);
        }
        else {
            fragment = new YardFragment();
            fragmentTransaction.add(R.id.fragmentContainerView, fragment).addToBackStack(null);
        }
        fragmentTransaction.commit();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem menuItem) {

        Fragment currentFragment = getSupportFragmentManager().findFragmentById(R.id.fragmentContainerView);

        if (menuItem.getItemId() == android.R.id.home) {
            if (currentFragment instanceof YardFragment) {
                finish();
                return true;
            }
            else {
                super.getOnBackPressedDispatcher().onBackPressed();
                return true;
            }
        }
        else {
            Fragment fragment = null;
            if (menuItem.getItemId() == R.id.admin_menuItem) {
                fragment = new AdminFragment();
            }
            if (menuItem.getItemId() == R.id.search_menuItem) {
                fragment = new SearchFragment();
            }
            FragmentManager fragmentManager = getSupportFragmentManager();
            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
            fragmentTransaction.replace(R.id.fragmentContainerView, fragment).addToBackStack(null);
            fragmentTransaction.commit();
            return true;
        }
    }

    public static void storeApiKey(String apiKey) {
        try {
            byte[] encryptedApiKeyByte = encrypt(apiKey, protectionParametersPassword);
            SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.putString("apiKey", Base64.encodeToString(encryptedApiKeyByte, Base64.DEFAULT));
            editor.apply();
            encryptedApiKey = sharedPreferences.getString("apiKey", null);
        } catch (GeneralSecurityException | UnsupportedEncodingException e) {
            throw new RuntimeException(e);
        }
    }

    public static byte[] encrypt(String data, String key) throws GeneralSecurityException, UnsupportedEncodingException {
        byte[] keyBytes = key.getBytes("UTF-8");
        SecretKeySpec secretKey = new SecretKeySpec(keyBytes, "AES");
        Cipher cipher = Cipher.getInstance("AES/ECB/PKCS5Padding");
        cipher.init(Cipher.ENCRYPT_MODE, secretKey);
        return cipher.doFinal(data.getBytes("UTF-8"));
    }

    public static String decrypt(byte[] encryptedData, String key) throws GeneralSecurityException, UnsupportedEncodingException {
        byte[] keyBytes = key.getBytes("UTF-8");
        SecretKeySpec secretKey = new SecretKeySpec(keyBytes, "AES");
        Cipher cipher = Cipher.getInstance("AES/ECB/PKCS5Padding");
        cipher.init(Cipher.DECRYPT_MODE, secretKey);
        byte[] decryptedData = cipher.doFinal(encryptedData);
        return new String(decryptedData, "UTF-8");
    }
}