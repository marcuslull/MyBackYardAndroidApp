package com.mybackyard.frontend;

import static com.mybackyard.frontend.MainActivity.inputValidator;
import static com.mybackyard.frontend.MainActivity.mbyViewModel;
import static com.mybackyard.frontend.MainActivity.storeApiKey;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

public class AdminFragment extends Fragment {


    public AdminFragment() {
        super(R.layout.fragment_admin);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        Activity activity = getActivity();
        ActionBar actionBar = ((AppCompatActivity)activity).getSupportActionBar();
        if (actionBar != null) {
            actionBar.setTitle("Admin");
        }
        EditText editText = view.findViewById(R.id.addKey_editText);
        Button button = view.findViewById(R.id.api_save_button);

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (editText.getText() != null) {
                    if (inputValidator.isValidAPIKey(editText.getText().toString())) {
                        storeApiKey(editText.getText().toString());
                        mbyViewModel.invalidateYard();
                        Fragment fragment = new YardFragment();
                        FragmentManager fragmentManager = getParentFragmentManager();
                        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                        fragmentTransaction.replace(R.id.fragmentContainerView, fragment).addToBackStack(null);
                        fragmentTransaction.commit();
                    }
                }
            }
        });
    }

}
