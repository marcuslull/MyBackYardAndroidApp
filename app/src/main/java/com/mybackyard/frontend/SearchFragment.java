package com.mybackyard.frontend;

import static com.mybackyard.frontend.MainActivity.animalService;
import static com.mybackyard.frontend.MainActivity.inputValidator;
import static com.mybackyard.frontend.MainActivity.plantService;
import static com.mybackyard.frontend.MainActivity.yardService;

import android.app.Activity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Gravity;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.mybackyard.frontend.model.Animal;
import com.mybackyard.frontend.model.Plant;
import com.mybackyard.frontend.model.Yard;

import java.io.UnsupportedEncodingException;
import java.security.GeneralSecurityException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;

public class SearchFragment extends Fragment {

    private List<Yard> foundYards = new ArrayList<>();
    private List<Plant> foundPlants = new ArrayList<>();
    private List<Animal> foundAnimals = new ArrayList<>();

    public SearchFragment() {
        super(R.layout.fragment_search);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        Activity activity = getActivity();
        ActionBar actionBar = ((AppCompatActivity)activity).getSupportActionBar();
        if (actionBar != null) {
            actionBar.setTitle("Search");
        }

        EditText editText = view.findViewById(R.id.search_editText);
        LinearLayout yardLayout = view.findViewById(R.id.yards_search);
        LinearLayout plantLayout = view.findViewById(R.id.plants_search);
        LinearLayout animalLayout = view.findViewById(R.id.animal_search);

        editText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

                String query = (inputValidator.isValidString(String.valueOf(editText.getText()))) ?
                        String.valueOf(editText.getText()) : "";
                foundYards.clear();
                foundPlants.clear();
                foundAnimals.clear();
                yardLayout.removeAllViews();
                plantLayout.removeAllViews();
                animalLayout.removeAllViews();
                try {
                    foundYards = yardService.searchYards(query);
                    foundPlants = plantService.searchPlant(query);
                    foundAnimals = animalService.searchAnimals(query);
                } catch (ExecutionException | InterruptedException | UnsupportedEncodingException |
                         GeneralSecurityException e) {
                    throw new RuntimeException(e);
                }

                // ----------------YARDS----------------
                for (Yard yard : foundYards) {
                    TextView textView = new TextView(requireContext());
                    textView.setText(yard.getName());
                    ImageView icon = new ImageView(requireContext());
                    icon.setImageResource(R.drawable.fence);
                    View spacer = new View(requireContext());
                    spacer.setLayoutParams(new LinearLayout.LayoutParams(0,0,1));
                    LinearLayout horizontalLayout = new LinearLayout(requireContext());
                    horizontalLayout.setOrientation(LinearLayout.HORIZONTAL);
                    horizontalLayout.setMinimumHeight(125);
                    horizontalLayout.setGravity(Gravity.END);
                    horizontalLayout.addView(textView);
                    horizontalLayout.addView(spacer);
                    horizontalLayout.addView(icon);
                    horizontalLayout.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            Bundle bundle = new Bundle();
                            bundle.putLong("yardId", yard.getId());
                            Fragment fragment = new YardDetailsFragment();
                            fragment.setArguments(bundle);
                            FragmentManager fragmentManager = getParentFragmentManager();
                            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                            fragmentTransaction.replace(R.id.fragmentContainerView, fragment).addToBackStack(null);
                            fragmentTransaction.commit();
                        }
                    });
                    yardLayout.addView(horizontalLayout);
                }

                // ----------------PLANTS----------------
                for (Plant plant : foundPlants) {
                    TextView textView = new TextView(requireContext());
                    textView.setText(plant.getName());
                    ImageView icon = new ImageView(requireContext());
                    icon.setImageResource(R.drawable.sprout_outline);
                    View spacer = new View(requireContext());
                    spacer.setLayoutParams(new LinearLayout.LayoutParams(0,0,1));
                    LinearLayout horizontalLayout = new LinearLayout(requireContext());
                    horizontalLayout.setOrientation(LinearLayout.HORIZONTAL);
                    horizontalLayout.setMinimumHeight(125);
                    horizontalLayout.setGravity(Gravity.END);
                    horizontalLayout.addView(textView);
                    horizontalLayout.addView(spacer);
                    horizontalLayout.addView(icon);
                    horizontalLayout.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            Bundle bundle = new Bundle();
                            bundle.putLong("plantId", plant.getId());
                            Fragment fragment = new PlantDetailsFragment();
                            fragment.setArguments(bundle);
                            FragmentManager fragmentManager = getParentFragmentManager();
                            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                            fragmentTransaction.replace(R.id.fragmentContainerView, fragment).addToBackStack(null);
                            fragmentTransaction.commit();
                        }
                    });
                    plantLayout.addView(horizontalLayout);
                }

                // ----------------ANIMALS----------------
                for (Animal animal : foundAnimals) {
                    TextView textView = new TextView(requireContext());
                    textView.setText(animal.getName());
                    ImageView icon = new ImageView(requireContext());
                    icon.setImageResource(R.drawable.sheep);
                    View spacer = new View(requireContext());
                    spacer.setLayoutParams(new LinearLayout.LayoutParams(0,0,1));
                    LinearLayout horizontalLayout = new LinearLayout(requireContext());
                    horizontalLayout.setOrientation(LinearLayout.HORIZONTAL);
                    horizontalLayout.setMinimumHeight(125);
                    horizontalLayout.setGravity(Gravity.END);
                    horizontalLayout.addView(textView);
                    horizontalLayout.addView(spacer);
                    horizontalLayout.addView(icon);
                    horizontalLayout.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            Bundle bundle = new Bundle();
                            bundle.putLong("animalId", animal.getId());
                            Fragment fragment = new AnimalDetailsFragment();
                            fragment.setArguments(bundle);
                            FragmentManager fragmentManager = getParentFragmentManager();
                            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                            fragmentTransaction.replace(R.id.fragmentContainerView, fragment).addToBackStack(null);
                            fragmentTransaction.commit();
                        }
                    });
                    animalLayout.addView(horizontalLayout);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });




    }
}

