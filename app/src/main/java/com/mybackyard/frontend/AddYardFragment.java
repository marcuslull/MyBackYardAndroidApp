package com.mybackyard.frontend;

import static com.mybackyard.frontend.MainActivity.yardService;

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

import com.mybackyard.frontend.model.Yard;
import com.mybackyard.frontend.model.enums.HardinessZone;
import com.mybackyard.frontend.model.enums.SoilType;
import com.mybackyard.frontend.model.enums.SunExposure;
import com.mybackyard.frontend.model.enums.YardSubType;

import java.io.UnsupportedEncodingException;
import java.lang.reflect.Field;
import java.security.GeneralSecurityException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.ExecutionException;

public class AddYardFragment extends Fragment {

    Yard yardFromEdit;
    boolean isFromEdit = false;

    public AddYardFragment() {
        super(R.layout.fragment_add_yard);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        Activity activity = getActivity();
        ActionBar actionBar = ((AppCompatActivity)activity).getSupportActionBar();
        if (actionBar != null) {
            actionBar.setTitle("Add Yard");
        }

        String[] yardSubtypeArray = Arrays.stream(YardSubType.values())
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

        Spinner yardSubtypeSpinner = view.findViewById(R.id.yard_subtype_spinner);
        Spinner hardinessZoneSpinner = view.findViewById(R.id.hardiness_zone_spinner);
        Spinner soilTypeSpinner = view.findViewById(R.id.soil_type_spinner);
        Spinner sunExposureSpinner = view.findViewById(R.id.sun_exposure_spinner);

        ArrayAdapter<String> yardSubtypeAdapter = new ArrayAdapter<>(requireContext(),
                android.R.layout.simple_spinner_item, yardSubtypeArray);
        ArrayAdapter<String> hardinessZoneAdapter = new ArrayAdapter<>(requireContext(),
                android.R.layout.simple_spinner_item, hardinessZoneArray);
        ArrayAdapter<String> soilTypeAdapter = new ArrayAdapter<>(requireContext(),
                android.R.layout.simple_spinner_item, soilTypeArray);
        ArrayAdapter<String> sunExposureAdapter = new ArrayAdapter<>(requireContext(),
                android.R.layout.simple_spinner_item, sunExposureArray);

        yardSubtypeSpinner.setAdapter(yardSubtypeAdapter);
        hardinessZoneSpinner.setAdapter(hardinessZoneAdapter);
        soilTypeSpinner.setAdapter(soilTypeAdapter);
        sunExposureSpinner.setAdapter(sunExposureAdapter);

        Bundle arguments = getArguments(); // HERE

        if (arguments != null) {
            if (arguments.containsKey("isFromEdit")) {
                isFromEdit = true;
                if (actionBar != null) {
                    actionBar.setTitle("Edit Yard");
                }
                try {
                    yardFromEdit = yardService.getYard(String.valueOf(arguments.getLong("yardId")));
                } catch (ExecutionException | InterruptedException e) {
                    throw new RuntimeException(e);
                }
                EditText editText = view.findViewById(R.id.name_edittext);
                editText.setText(yardFromEdit.getName());
                yardSubtypeSpinner.setSelection(yardFromEdit.getYardSubType().ordinal());
                hardinessZoneSpinner.setSelection(yardFromEdit.getHardinessZone().ordinal());
                soilTypeSpinner.setSelection(yardFromEdit.getSoilType().ordinal());
                sunExposureSpinner.setSelection(yardFromEdit.getSunExposure().ordinal());
            }
        }

        Button saveButton = view.findViewById(R.id.save_button);
        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Yard yard = new Yard();
                yard.setName(((EditText) view.findViewById(R.id.name_edittext)).getText().toString());
                yard.setYardSubType(YardSubType.valueOf((String)
                        ((Spinner) view.findViewById(R.id.yard_subtype_spinner)).getSelectedItem()));
                yard.setHardinessZone(HardinessZone.valueOf((String)
                        ((Spinner) view.findViewById(R.id.hardiness_zone_spinner)).getSelectedItem()));
                yard.setSoilType(SoilType.valueOf((String)
                        ((Spinner) view.findViewById(R.id.soil_type_spinner)).getSelectedItem()));
                yard.setSunExposure(SunExposure.valueOf((String)
                        ((Spinner) view.findViewById(R.id.sun_exposure_spinner)).getSelectedItem()));

                try {
                    if (isFromEdit) { // HERE
                        Map<String, String> patch = new HashMap<>();
                        for (Field field : yardFromEdit.getClass().getDeclaredFields()) {
                            field.setAccessible(true);
                            if (field.get(yard) != field.get(yardFromEdit)) {
                                if (field.getName().equals("name") ||
                                        field.getName().equals("hardinessZone") ||
                                        field.getName().equals("soilType") ||
                                        field.getName().equals("sunExposure") ||
                                        field.getName().equals("yardSubType"))
                                {
                                    patch.put(field.getName(), Objects.requireNonNull(field
                                            .get(yard)).toString());
                                }
                            }
                        }
                        yardService.patchYard(patch, String.valueOf(yardFromEdit.getId()));
                        yardService.getYards(); // refresh the animals so there is something to popBack to
                    }
                    else { yardService.postYard(yard); }
                } catch (ExecutionException | InterruptedException | IllegalAccessException |
                         UnsupportedEncodingException | GeneralSecurityException e) {
                    throw new RuntimeException(e);
                }

                getParentFragmentManager().popBackStack();
            }
        });
    }
}
