package com.mybackyard.frontend;

import static com.mybackyard.frontend.MainActivity.noteService;

import android.app.Activity;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.mybackyard.frontend.model.Note;

import java.io.UnsupportedEncodingException;
import java.security.GeneralSecurityException;
import java.util.concurrent.ExecutionException;

public class NoteDetailsFragment extends Fragment {

    private Note note = null;

    public NoteDetailsFragment() {
        super(R.layout.fragment_note_details);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        Activity activity = getActivity();
        ActionBar actionBar = ((AppCompatActivity)activity).getSupportActionBar();
        if (actionBar != null) {
            actionBar.setTitle("Note Details");
        }


        Bundle arguments = getArguments();
        if (arguments != null) {
            try {
                note = noteService.getNote(String.valueOf(arguments.getLong("noteId")));
            } catch (ExecutionException | InterruptedException e) {
                throw new RuntimeException(e);
            }
        }

        TextView textView = view.findViewById(R.id.note_details_comment);
        if (note != null) textView.setText(note.getComment());

        Button editButton = view.findViewById(R.id.note_edit_button);
        editButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Fragment fragment = new AddNoteFragment(); // HERE
                Bundle bundle = new Bundle();
                bundle.putLong("noteId", note.getId());
                bundle.putBoolean("isFromEdit", true);
                fragment.setArguments(bundle);
                FragmentManager fragmentManager = getParentFragmentManager();
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                fragmentTransaction.replace(R.id.fragmentContainerView, fragment).addToBackStack(null);
                fragmentTransaction.commit();
            }
        });
        Button deleteButton = view.findViewById(R.id.note_delete_button);
        deleteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(requireContext(), R.style.alert);
                alertDialogBuilder.setTitle("Delete Confirmation");
                alertDialogBuilder.setMessage("This will delete the note.");
                alertDialogBuilder.setPositiveButton("DELETE", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        try {
                            noteService.deleteNote(String.valueOf(arguments.getLong("noteId")));
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
    }
}
