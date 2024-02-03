package com.mybackyard.frontend;

import static com.mybackyard.frontend.MainActivity.yardService;

import android.app.Activity;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.mybackyard.frontend.model.Yard;

import java.io.UnsupportedEncodingException;
import java.security.GeneralSecurityException;
import java.util.List;
import java.util.concurrent.ExecutionException;

public class YardFragment extends Fragment {

    public YardFragment() {
        super(R.layout.fragment_yard);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        Activity activity = getActivity();
        ActionBar actionBar = ((AppCompatActivity)activity).getSupportActionBar();
        if (actionBar != null) {
            actionBar.setTitle("My Back Yard");
        }

        LinearLayout linearLayout = view.findViewById(R.id.yard_scrollview);
        List<Yard> yardList = null;
        try {
            yardList = yardService.getYards();
        } catch (ExecutionException | InterruptedException | UnsupportedEncodingException |
                 GeneralSecurityException e) {
            throw new RuntimeException(e);
        }
        if (yardList != null) {
            for (Yard yard : yardList) {
                TextView textView = new TextView(getContext());
                String text = yard.getName();
                textView.setText(text);
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
                linearLayout.addView(horizontalLayout);
            }
        }

        FloatingActionButton floatingActionButton = view.findViewById(R.id.yard_fab);
        floatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Fragment fragment = new AddYardFragment();
                FragmentManager fragmentManager = getParentFragmentManager();
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                fragmentTransaction.replace(R.id.fragmentContainerView, fragment).addToBackStack(null);
                fragmentTransaction.commit();
            }
        });
    }
}
