package com.mybackyard.frontend;

import static com.mybackyard.frontend.MainActivity.noteService;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import com.mybackyard.frontend.model.Note;

import java.io.UnsupportedEncodingException;
import java.security.GeneralSecurityException;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.ExecutionException;

public class AddNoteFragment extends Fragment {

    Note noteFromEdit;
    boolean isFromEdit = false;

    public AddNoteFragment() {
        super(R.layout.fragment_add_note);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        Activity activity = getActivity();
        ActionBar actionBar = ((AppCompatActivity)activity).getSupportActionBar();
        if (actionBar != null) {
            actionBar.setTitle("Add Note");
        }

        Bundle arguments = getArguments(); // HERE

        if (arguments != null) {
            if (arguments.containsKey("isFromEdit")) {
                isFromEdit = true;
                try {
                    noteFromEdit = noteService.getNote(String.valueOf(arguments.getLong("noteId")));
                } catch (ExecutionException | InterruptedException e) {
                    throw new RuntimeException(e);
                }
                EditText editText = view.findViewById(R.id.note_edittext);
                editText.setText(noteFromEdit.getComment());
            }
        }

        Button button = view.findViewById(R.id.save_note);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Bundle arguments = getArguments();
                Note note = new Note();
                if (arguments != null) {
                    if (arguments.containsKey("yardId")) {
                        note.setYardId(arguments.getLong("yardId"));
                    }
                    else if (arguments.containsKey("plantId")) {
                        note.setPlantId(arguments.getLong("plantId"));
                    }
                    else if (arguments.containsKey("animalId")) {
                        note.setAnimalId(arguments.getLong("animalId"));
                    }
                }
                EditText editText = view.findViewById(R.id.note_edittext);
                note.setComment(editText.getText().toString());
                try {
                    if (isFromEdit) {
                        if (actionBar != null) {
                            actionBar.setTitle("Edit Note");
                        }
                        Map<String, String> patch = new HashMap<>();
                        if (!Objects.equals(note.getComment(), noteFromEdit.getComment())) {
                            patch.put("comment", note.getComment());
                            noteService.patchNote(patch, String.valueOf(noteFromEdit.getId()));
                            noteService.getNotes(); // refresh the cache
                        }
                    }
                    else { noteService.postNote(note); }
                } catch (ExecutionException | InterruptedException | GeneralSecurityException |
                         UnsupportedEncodingException e) {
                    throw new RuntimeException(e);
                }
                getParentFragmentManager().popBackStack();
            }
        });
    }
}
