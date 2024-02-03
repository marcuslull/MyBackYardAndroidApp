package com.mybackyard.frontend;

import static com.mybackyard.frontend.MainActivity.animalService;
import static com.mybackyard.frontend.MainActivity.imageService;
import static com.mybackyard.frontend.MainActivity.noteService;

import android.app.Activity;
import android.app.AlertDialog;
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
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.mybackyard.frontend.model.Animal;
import com.mybackyard.frontend.model.Image;
import com.mybackyard.frontend.model.Note;

import java.io.UnsupportedEncodingException;
import java.security.GeneralSecurityException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;

public class AnimalDetailsFragment extends Fragment {

    private Animal animal = null;

    public AnimalDetailsFragment() {
        super(R.layout.fragment_animal_details);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        Activity activity = getActivity();
        ActionBar actionBar = ((AppCompatActivity)activity).getSupportActionBar();
        if (actionBar != null) {
            actionBar.setTitle("Animal Details");
        }

        Bundle arguments = getArguments();
        if (arguments != null) {
            try {
                animal = animalService.getAnimal(String.valueOf(arguments.getLong("animalId")));
            } catch (ExecutionException | InterruptedException e) {
                throw new RuntimeException(e);
            }
        }

        TextView nameTextView = view.findViewById(R.id.animal_details_name);
        TextView subtypeTextView = view.findViewById(R.id.animal_details_subtype);
        TextView dietTextView = view.findViewById(R.id.animal_details_diet);
        TextView nativeTextView = view.findViewById(R.id.animal_details_native);

        if (animal != null) {
            nameTextView.setText(animal.getName());
            subtypeTextView.setText(animal.getSubType().toString());
            dietTextView.setText(animal.getDietType().toString());
            nativeTextView.setText(animal.getNativeAreaType().toString());
        }

        Button editButton = view.findViewById(R.id.animal_edit_button);
        editButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Fragment fragment = new AddAnimalFragment(); // HERE
                Bundle bundle = new Bundle();
                bundle.putLong("animalId", animal.getId());
                bundle.putBoolean("isFromEdit", true);
                fragment.setArguments(bundle);
                FragmentManager fragmentManager = getParentFragmentManager();
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                fragmentTransaction.replace(R.id.fragmentContainerView, fragment).addToBackStack(null);
                fragmentTransaction.commit();
            }
        });
        Button deleteButton = view.findViewById(R.id.animal_delete_button);
        deleteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(requireContext(), R.style.alert);
                alertDialogBuilder.setTitle("Delete Confirmation");
                alertDialogBuilder.setMessage("This will delete the animal and all associated notes and images.");
                alertDialogBuilder.setPositiveButton("DELETE", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        try {
                            animalService.deleteAnimal(String.valueOf(arguments.getLong("animalId")));
                        } catch (ExecutionException | InterruptedException |
                                 UnsupportedEncodingException | GeneralSecurityException e) {
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
        LinearLayout noteLinearLayout = view.findViewById(R.id.animal_details_notes_layout);
        try {
            notes = noteService.getNotes();
        } catch (ExecutionException | InterruptedException | UnsupportedEncodingException |
                 GeneralSecurityException e) {
            throw new RuntimeException(e);
        }
        for (Note note : notes) {
            if (note.getAnimalId() == animal.getId()) {
                TextView textView = new TextView(getContext());
                textView.setText(note.getComment());
                ImageView icon = new ImageView(requireContext());
                icon.setImageResource(R.drawable.note_outline);
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

        // ----------------IMAGES----------------
        List<Image> images = new ArrayList<>();
        LinearLayout imageLinearLayout = view.findViewById(R.id.animal_details_images_layout);
        try {
            images = imageService.getImages();
        } catch (ExecutionException | InterruptedException | GeneralSecurityException |
                 UnsupportedEncodingException e) {
            throw new RuntimeException(e);
        }
        for (Image image : images) {
            if (image.getAnimalId() == animal.getId()) {
                TextView textView = new TextView(getContext());
                textView.setText(image.getLocation());
                ImageView icon = new ImageView(requireContext());
                icon.setImageResource(R.drawable.image_outline);
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
                        bundle.putLong("imageId", image.getId());
                        Fragment fragment = new ImageDetailsFragment();
                        fragment.setArguments(bundle);
                        FragmentManager fragmentManager = getParentFragmentManager();
                        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                        fragmentTransaction.replace(R.id.fragmentContainerView, fragment).addToBackStack(null);
                        fragmentTransaction.commit();
                    }
                });
                imageLinearLayout.addView(horizontalLayout);
            }
        }

        FloatingActionButton floatingActionButton = view.findViewById(R.id.comment_image_fab);
        floatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PopupMenu popupMenu = new PopupMenu(getContext(), floatingActionButton);
                popupMenu.inflate(R.menu.add_note_image);
                popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem item) {
                        Fragment fragment = null;
                        if (item.getTitle().equals("Note")) {
                            fragment = new AddNoteFragment();
                        }
                        else if (item.getTitle().equals("Image")) {
                            fragment = new AddImageFragment();
                        }
                        Bundle bundle = new Bundle();
                        bundle.putLong("animalId", arguments.getLong("animalId"));
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
