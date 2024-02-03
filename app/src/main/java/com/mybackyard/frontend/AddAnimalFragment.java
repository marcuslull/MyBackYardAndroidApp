package com.mybackyard.frontend;

import static com.mybackyard.frontend.MainActivity.animalService;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import com.mybackyard.frontend.model.Animal;
import com.mybackyard.frontend.model.enums.AnimalSubType;
import com.mybackyard.frontend.model.enums.DietType;
import com.mybackyard.frontend.model.enums.NativeAreaType;

import java.io.UnsupportedEncodingException;
import java.lang.reflect.Field;
import java.security.GeneralSecurityException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.ExecutionException;

public class AddAnimalFragment extends Fragment {

    Animal animalFromEdit;
    boolean isFromEdit = false; // HERE

    public AddAnimalFragment() {
        super(R.layout.fragment_add_animal);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        Activity activity = getActivity();
        ActionBar actionBar = ((AppCompatActivity)activity).getSupportActionBar();
        if (actionBar != null) {
            actionBar.setTitle("Add Animal");
        }

        String[] animalSubtypeArray = Arrays.stream(AnimalSubType.values())
                .map(Enum::name)
                .toArray(String[]::new);
        String[] animalDietTypeArray = Arrays.stream(DietType.values())
                .map(Enum::name)
                .toArray(String[]::new);
        String[] nativeAreaTypeArray = Arrays.stream(NativeAreaType.values())
                .map(Enum::name)
                .toArray(String[]::new);

        Spinner animalSubtypeSpinner = view.findViewById(R.id.animal_subtype_spinner);
        Spinner animalDietTypeSpinner = view.findViewById(R.id.animal_diet_type_spinner);
        Spinner nativeAreaTypeSpinner = view.findViewById(R.id.animal_native_area_spinner);

        ArrayAdapter<String> animalSubtypeAdapter = new ArrayAdapter<>(requireContext(),
                android.R.layout.simple_spinner_item, animalSubtypeArray);
        ArrayAdapter<String> animalDietTypeAdapter = new ArrayAdapter<>(requireContext(),
                android.R.layout.simple_spinner_item, animalDietTypeArray);
        ArrayAdapter<String> nativeAreaTypeAdapter = new ArrayAdapter<>(requireContext(),
                android.R.layout.simple_spinner_item, nativeAreaTypeArray);

        animalSubtypeSpinner.setAdapter(animalSubtypeAdapter);
        animalDietTypeSpinner.setAdapter(animalDietTypeAdapter);
        nativeAreaTypeSpinner.setAdapter(nativeAreaTypeAdapter);


        Bundle arguments = getArguments(); // HERE

        if (arguments != null) {
            if (arguments.containsKey("isFromEdit")) {
                isFromEdit = true;
                try {
                    if (actionBar != null) {
                        actionBar.setTitle("Edit Animal");
                    }
                    animalFromEdit = animalService.getAnimal(String.valueOf(arguments.getLong("animalId")));
                } catch (ExecutionException | InterruptedException e) {
                    throw new RuntimeException(e);
                }
                EditText editText = view.findViewById(R.id.animal_name_edittext);
                editText.setText(animalFromEdit.getName());
                animalSubtypeSpinner.setSelection(animalFromEdit.getSubType().ordinal());
                animalDietTypeSpinner.setSelection(animalFromEdit.getDietType().ordinal());
                nativeAreaTypeSpinner.setSelection(animalFromEdit.getNativeAreaType().ordinal());
            }
        }

        Button saveButton = view.findViewById(R.id.animal_save_button);
        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                long yardId = 0;
                Bundle arguments = getArguments();
                if (arguments != null) {
                    yardId = arguments.getLong("yardId");
                }

                Animal animal = new Animal();
                animal.setYardId(yardId);
                animal.setName(((EditText) view.findViewById(R.id.animal_name_edittext)).getText().toString());
                animal.setSubType(AnimalSubType.valueOf((String)
                        ((Spinner) view.findViewById(R.id.animal_subtype_spinner)).getSelectedItem()));
                animal.setDietType(DietType.valueOf((String)
                        ((Spinner) view.findViewById(R.id.animal_diet_type_spinner)).getSelectedItem()));
                animal.setNativeAreaType(NativeAreaType.valueOf((String)
                        ((Spinner) view.findViewById(R.id.animal_native_area_spinner)).getSelectedItem()));

                try {
                    if (isFromEdit) { // HERE
                        Map<String, String> patch = new HashMap<>();
                        for (Field field : animalFromEdit.getClass().getDeclaredFields()) {
                            field.setAccessible(true);
                            if (field.get(animal) != field.get(animalFromEdit)) {
                                if (field.getName().equals("name") ||
                                        field.getName().equals("subType") ||
                                        field.getName().equals("dietType") ||
                                        field.getName().equals("nativeAreaType"))
                                {
                                    patch.put(field.getName(), Objects.requireNonNull(field
                                            .get(animal)).toString());
                                }
                            }
                        }
                        animalService.patchAnimal(patch, String.valueOf(animalFromEdit.getId()));
                        animalService.getAnimals(); // refresh the animals so there is something to popBack to
                    }
                    else { animalService.postAnimal(animal); }
                } catch (ExecutionException | InterruptedException | IllegalAccessException |
                         GeneralSecurityException | UnsupportedEncodingException e) {
                    throw new RuntimeException(e);
                }
                getParentFragmentManager().popBackStack();
            }
        });
    }
}
