package com.mybackyard.frontend;

import static com.mybackyard.frontend.MainActivity.imageService;

import android.Manifest;
import android.app.Activity;
import android.content.DialogInterface;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.mybackyard.frontend.model.Image;

import java.io.UnsupportedEncodingException;
import java.security.GeneralSecurityException;
import java.util.concurrent.ExecutionException;

public class ImageDetailsFragment extends Fragment {

    private Image image = null;

    public ImageDetailsFragment() {
        super(R.layout.fragment_image_details);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        Activity activity = getActivity();
        ActionBar actionBar = ((AppCompatActivity)activity).getSupportActionBar();
        if (actionBar != null) {
            actionBar.setTitle("Image Details");
        }

        Bundle arguments = getArguments();
        if (arguments != null) {
            try {
                image = imageService.getImage(String.valueOf(arguments.getLong("imageId")));
            } catch (ExecutionException | InterruptedException e) {
                throw new RuntimeException(e);
            }
        }

        TextView textView = view.findViewById(R.id.image_details_location);
        if (image != null) textView.setText(image.getLocation());

        Button editButton = view.findViewById(R.id.image_edit_button);
        editButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Fragment fragment = new AddImageFragment();
                Bundle bundle = new Bundle();
                bundle.putLong("imageId", image.getId());
                bundle.putBoolean("isFromEdit", true);
                fragment.setArguments(bundle);
                FragmentManager fragmentManager = getParentFragmentManager();
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                fragmentTransaction.replace(R.id.fragmentContainerView, fragment).addToBackStack(null);
                fragmentTransaction.commit();
            }
        });
        Button deleteButton = view.findViewById(R.id.image_delete_button);
        deleteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(requireContext(), R.style.alert);
                alertDialogBuilder.setTitle("Delete Confirmation");
                alertDialogBuilder.setMessage("This will delete the Image.");
                alertDialogBuilder.setPositiveButton("DELETE", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        try {
                            imageService.deleteImage(String.valueOf(arguments.getLong("imageId")));
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

        if (ContextCompat.checkSelfPermission(requireContext(), Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(requireActivity(), new String[]{Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE}, 0);
            // TODO: Camera integration
            System.out.println("Competed the if");
        }
    }
}
