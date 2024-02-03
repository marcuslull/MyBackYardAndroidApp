package com.mybackyard.frontend;

import static com.mybackyard.frontend.MainActivity.plantService;

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

import com.mybackyard.frontend.model.Plant;
import com.mybackyard.frontend.model.enums.HardinessZone;
import com.mybackyard.frontend.model.enums.NativeAreaType;
import com.mybackyard.frontend.model.enums.PlantSubType;
import com.mybackyard.frontend.model.enums.SoilType;
import com.mybackyard.frontend.model.enums.SunExposure;
import com.mybackyard.frontend.model.enums.WateringFrequency;

import java.io.UnsupportedEncodingException;
import java.lang.reflect.Field;
import java.security.GeneralSecurityException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.ExecutionException;

public class AddPlantFragment extends Fragment {

    Plant plantFromEdit;
    boolean isFromEdit = false;

    public AddPlantFragment() {
        super(R.layout.fragment_add_plant);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        Activity activity = getActivity();
        ActionBar actionBar = ((AppCompatActivity)activity).getSupportActionBar();
        if (actionBar != null) {
            actionBar.setTitle("Add Plant");
        }

        String[] plantSubtypeArray = Arrays.stream(PlantSubType.values())
                .map(Enum::name)
                .toArray(String[]::new);
        String[] hardinessZoneArray = Arrays.stream(HardinessZone.values())
                .map(Enum::name)
                .toArray(String[]::new);
        String[] soilTypeArray = Arrays.stream(SoilType.values())
                .map(Enum::name)
                .toArray(String[]::new);
        String[] sunExposureArray = Arrays.stream(SunExposure.values())
                .map(Enum::name)
                .toArray(String[]::new);
        String[] nativeAreaArray = Arrays.stream(NativeAreaType.values())
                .map(Enum::name)
                .toArray(String[]::new);
        String[] wateringFrequencyArray = Arrays.stream(WateringFrequency.values())
                .map(Enum::name)
                .toArray(String[]::new);

        Spinner plantSubtypeSpinner = view.findViewById(R.id.plant_subtype_spinner);
        Spinner hardinessZoneSpinner = view.findViewById(R.id.plant_hardiness_zone_spinner);
        Spinner soilTypeSpinner = view.findViewById(R.id.plant_soil_type_spinner);
        Spinner sunExposureSpinner = view.findViewById(R.id.plant_sun_exposure_spinner);
        Spinner nativeAreaSpinner = view.findViewById(R.id.plant_native_area_spinner);
        Spinner wateringFrequencySpinner = view.findViewById(R.id.plant_watering_frequency_spinner);

        ArrayAdapter<String> plantSubtypeAdapter = new ArrayAdapter<>(requireContext(),
                android.R.layout.simple_spinner_item, plantSubtypeArray);
        ArrayAdapter<String> hardinessZoneAdapter = new ArrayAdapter<>(requireContext(),
                android.R.layout.simple_spinner_item, hardinessZoneArray);
        ArrayAdapter<String> soilTypeAdapter = new ArrayAdapter<>(requireContext(),
                android.R.layout.simple_spinner_item, soilTypeArray);
        ArrayAdapter<String> sunExposureAdapter = new ArrayAdapter<>(requireContext(),
                android.R.layout.simple_spinner_item, sunExposureArray);
        ArrayAdapter<String> nativeAreaAdapter = new ArrayAdapter<>(requireContext(),
                android.R.layout.simple_spinner_item, nativeAreaArray);
        ArrayAdapter<String> wateringFrequencyAdapter = new ArrayAdapter<>(requireContext(),
                android.R.layout.simple_spinner_item, wateringFrequencyArray);

        plantSubtypeSpinner.setAdapter(plantSubtypeAdapter);
        hardinessZoneSpinner.setAdapter(hardinessZoneAdapter);
        soilTypeSpinner.setAdapter(soilTypeAdapter);
        sunExposureSpinner.setAdapter(sunExposureAdapter);
        nativeAreaSpinner.setAdapter(nativeAreaAdapter);
        wateringFrequencySpinner.setAdapter(wateringFrequencyAdapter);

        Bundle arguments = getArguments(); // HERE

        if (arguments != null) {
            if (arguments.containsKey("isFromEdit")) {
                isFromEdit = true;
                if (actionBar != null) {
                    actionBar.setTitle("Edit Plant");
                }
                try {
                    plantFromEdit = plantService.getPlant(String.valueOf(arguments.getLong("plantId")));
                } catch (ExecutionException | InterruptedException e) {
                    throw new RuntimeException(e);
                }
                EditText editText = view.findViewById(R.id.plant_name_edittext);
                editText.setText(plantFromEdit.getName());
                plantSubtypeSpinner.setSelection(plantFromEdit.getPlantSubType().ordinal());
                hardinessZoneSpinner.setSelection(plantFromEdit.getHardinessZone().ordinal());
                soilTypeSpinner.setSelection(plantFromEdit.getSoilType().ordinal());
                sunExposureSpinner.setSelection(plantFromEdit.getSunExposure().ordinal());
                nativeAreaSpinner.setSelection(plantFromEdit.getNativeAreaType().ordinal());
                wateringFrequencySpinner.setSelection(plantFromEdit.getWateringFrequency().ordinal());
            }
        }

        Button saveButton = view.findViewById(R.id.plant_save_button);
        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                long yardId = 0;
                Bundle arguments = getArguments();
                if (arguments != null) {
                    yardId = arguments.getLong("yardId");
                }

                Plant plant = new Plant();
                plant.setYardId(yardId);
                plant.setName(((EditText) view.findViewById(R.id.plant_name_edittext)).getText().toString());
                plant.setPlantSubType(PlantSubType.valueOf((String)
                        ((Spinner) view.findViewById(R.id.plant_subtype_spinner)).getSelectedItem()));
                plant.setHardinessZone(HardinessZone.valueOf((String)
                        ((Spinner) view.findViewById(R.id.plant_hardiness_zone_spinner)).getSelectedItem()));
                plant.setSoilType(SoilType.valueOf((String)
                        ((Spinner) view.findViewById(R.id.plant_soil_type_spinner)).getSelectedItem()));
                plant.setSunExposure(SunExposure.valueOf((String)
                        ((Spinner) view.findViewById(R.id.plant_sun_exposure_spinner)).getSelectedItem()));
                plant.setNativeAreaType(NativeAreaType.valueOf((String)
                        ((Spinner) view.findViewById(R.id.plant_native_area_spinner)).getSelectedItem()));
                plant.setWateringFrequency(WateringFrequency.valueOf((String)
                        ((Spinner) view.findViewById(R.id.plant_watering_frequency_spinner)).getSelectedItem()));

                try {
                    if (isFromEdit) { // HERE
                        Map<String, String> patch = new HashMap<>();
                        for (Field field : plantFromEdit.getClass().getDeclaredFields()) {
                            field.setAccessible(true);
                            if (field.get(plant) != field.get(plantFromEdit)) {
                                if (field.getName().equals("name") ||
                                        field.getName().equals("plantSubType") ||
                                        field.getName().equals("hardinessZone") ||
                                        field.getName().equals("nativeAreaType") ||
                                        field.getName().equals("soilType") ||
                                        field.getName().equals("sunExposure") ||
                                        field.getName().equals("wateringFrequency"))
                                {
                                    patch.put(field.getName(), Objects.requireNonNull(field
                                            .get(plant)).toString());
                                }
                            }
                        }
                        plantService.patchPlant(patch, String.valueOf(plantFromEdit.getId()));
                        plantService.getPlants(); // refresh the plants so there is something to popBack to
                    }
                    else { plantService.postPlant(plant); }
                } catch (ExecutionException | InterruptedException | IllegalAccessException |
                         GeneralSecurityException | UnsupportedEncodingException e) {
                    throw new RuntimeException(e);
                }

                getParentFragmentManager().popBackStack();
            }
        });
    }
}
