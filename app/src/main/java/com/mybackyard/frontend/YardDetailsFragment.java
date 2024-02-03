package com.mybackyard.frontend;

import static com.mybackyard.frontend.MainActivity.animalService;
import static com.mybackyard.frontend.MainActivity.noteService;
import static com.mybackyard.frontend.MainActivity.plantService;
import static com.mybackyard.frontend.MainActivity.yardService;

import android.app.Activity;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupMenu;
import android.widget.TextView;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.mybackyard.frontend.model.Animal;
import com.mybackyard.frontend.model.Note;
import com.mybackyard.frontend.model.Plant;
import com.mybackyard.frontend.model.Yard;

import java.io.UnsupportedEncodingException;
import java.security.GeneralSecurityException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;

public class YardDetailsFragment extends Fragment {

    private Yard yard = null;

    public YardDetailsFragment() {
        super(R.layout.fragment_yard_details);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        Activity activity = getActivity();
        ActionBar actionBar = ((AppCompatActivity)activity).getSupportActionBar();
        if (actionBar != null) {
            actionBar.setTitle("Yard Details");
        }

        Bundle arguments = getArguments();
        if (arguments != null) {
            try {
                yard = yardService.getYard(String.valueOf(arguments.getLong("yardId")));
            } catch (ExecutionException | InterruptedException e) {
                throw new RuntimeException(e);
            }
        }

        TextView nametextView = view.findViewById(R.id.yard_details_name);
        TextView subtypetextView = view.findViewById(R.id.yard_details_subtype);
        TextView hardinesstextView = view.findViewById(R.id.yard_details_hardiness_zone);
        TextView soiltypetextView = view.findViewById(R.id.yard_details_soil_type);
        TextView sunExposuretextView = view.findViewById(R.id.yard_details_sun_exposure);

        nametextView.setText(yard.getName());
        subtypetextView.setText(yard.getYardSubType().toString());
        hardinesstextView.setText(yard.getHardinessZone().toString());
        soiltypetextView.setText(yard.getSoilType().toString());
        sunExposuretextView.setText(yard.getSunExposure().toString());

        Button editButton = view.findViewById(R.id.yard_edit_button);
        editButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Fragment fragment = new AddYardFragment(); // HERE
                Bundle bundle = new Bundle();
                bundle.putLong("yardId", yard.getId());
                bundle.putBoolean("isFromEdit", true);
                fragment.setArguments(bundle);
                FragmentManager fragmentManager = getParentFragmentManager();
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                fragmentTransaction.replace(R.id.fragmentContainerView, fragment).addToBackStack(null);
                fragmentTransaction.commit();
            }
        });
        Button deleteButton = view.findViewById(R.id.yard_delete_button);
        deleteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(requireContext(), R.style.alert);
                alertDialogBuilder.setTitle("Delete Confirmation");
                alertDialogBuilder.setMessage("This will delete the yard and all associated notes, plants, and animals");
                alertDialogBuilder.setPositiveButton("DELETE", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        try {
                            yardService.deleteYard(String.valueOf(yard.getId()));
                        } catch (ExecutionException | InterruptedException |
                                 GeneralSecurityException | UnsupportedEncodingException e) {
                            throw new RuntimeException(e);
                        }
                        getParentFragmentManager().popBackStack();
                    }
                });
                alertDialogBuilder.setNegativeButton("CANCEL", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });
                AlertDialog alertDialog = alertDialogBuilder.create();
                alertDialog.show();
            }
        });


        // ----------------NOTES----------------
        List<Note> notes = new ArrayList<>();
        LinearLayout noteLinearLayout = view.findViewById(R.id.yard_details_note_layout);
        try {
            notes = noteService.getNotes();
        } catch (ExecutionException | InterruptedException | GeneralSecurityException |
                 UnsupportedEncodingException e) {
            throw new RuntimeException(e);
        }
        for (Note note : notes) {
            if (note.getYardId() == yard.getId()) {
                TextView textView = new TextView(getContext());
                textView.setText(note.getComment());
                ImageView icon = new ImageView(requireContext());
                icon.setImageResource(R.drawable.note_outline);
                View spacer = new View(requireContext());
                spacer.setLayoutParams(new LinearLayout.LayoutParams(0,0,1));
                LinearLayout horizontalLayout = new LinearLayout(requireContext());
                horizontalLayout.setOrientation(LinearLayout.HORIZONTAL);
                horizontalLayout.setMinimumHeight(100);
                horizontalLayout.setGravity(Gravity.END);
                horizontalLayout.addView(textView);
                horizontalLayout.addView(spacer);
                horizontalLayout.addView(icon);
                horizontalLayout.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Bundle bundle = new Bundle();
                        bundle.putLong("noteId", note.getId());
                        Fragment fragment = new NoteDetailsFragment();
                        fragment.setArguments(bundle);
                        FragmentManager fragmentManager = getParentFragmentManager();
                        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                        fragmentTransaction.replace(R.id.fragmentContainerView, fragment).addToBackStack(null);
                        fragmentTransaction.commit();
                    }
                });
                noteLinearLayout.addView(horizontalLayout);
            }
        }

        // ----------------PLANTS----------------
        List<Plant> plants = new ArrayList<>();
        LinearLayout plantLinearLayout = view.findViewById(R.id.yard_details_plant_layout);
        try {
            plants = plantService.getPlants();
        } catch (ExecutionException | InterruptedException | GeneralSecurityException |
                 UnsupportedEncodingException e) {
            throw new RuntimeException(e);
        }
        for (Plant plant : plants) {
            if (plant.getYardId() == yard.getId()) {
                TextView textView = new TextView(getContext());
                textView.setText(plant.getName());
                ImageView icon = new ImageView(requireContext());
                icon.setImageResource(R.drawable.sprout_outline);
                View spacer = new View(requireContext());
                spacer.setLayoutParams(new LinearLayout.LayoutParams(0,0,1));
                LinearLayout horizontalLayout = new LinearLayout(requireContext());
                horizontalLayout.setOrientation(LinearLayout.HORIZONTAL);
                horizontalLayout.setMinimumHeight(100);
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
                plantLinearLayout.addView(horizontalLayout);
            }
        }

        // ----------------ANIMALS----------------
        List<Animal> animals = new ArrayList<>();
        LinearLayout animalLinearLayout = view.findViewById(R.id.yard_details_animal_layout);
        try {
            animals = animalService.getAnimals();
        } catch (ExecutionException | InterruptedException | UnsupportedEncodingException |
                 GeneralSecurityException e) {
            throw new RuntimeException(e);
        }
        for (Animal animal : animals) {
            if (animal.getYardId() == yard.getId()) {
                TextView textView = new TextView(getContext());
                textView.setText(animal.getName());
                ImageView icon = new ImageView(requireContext());
                icon.setImageResource(R.drawable.sheep);
                View spacer = new View(requireContext());
                spacer.setLayoutParams(new LinearLayout.LayoutParams(0,0,1));
                LinearLayout horizontalLayout = new LinearLayout(requireContext());
                horizontalLayout.setOrientation(LinearLayout.HORIZONTAL);
                horizontalLayout.setMinimumHeight(100);
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
                animalLinearLayout.addView(horizontalLayout);
            }
        }

        FloatingActionButton floatingActionButton = view.findViewById(R.id.comment_plant_animal_fab);
        floatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PopupMenu popupMenu = new PopupMenu(getContext(), floatingActionButton);
                popupMenu.inflate(R.menu.add_menu);
                popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem item) {
                        Fragment fragment = null;
                        if (item.getTitle().equals("Note")) {
                            fragment = new AddNoteFragment();
                        }
                        else if (item.getTitle().equals("Plant")) {
                            fragment = new AddPlantFragment();
                        }
                        else if (item.getTitle().equals("Animal")) {
                            fragment = new AddAnimalFragment();
                        }
                        Bundle bundle = new Bundle();
                        bundle.putLong("yardId", yard.getId());
                        fragment.setArguments(bundle);
                        FragmentManager fragmentManager = getParentFragmentManager();
                        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                        fragmentTransaction.replace(R.id.fragmentContainerView, fragment).addToBackStack(null);
                        fragmentTransaction.commit();
                        return true;
                    }
                });
                popupMenu.show();
            }
        });
    }
}
