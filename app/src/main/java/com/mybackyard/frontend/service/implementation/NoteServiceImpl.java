package com.mybackyard.frontend.service.implementation;

import static com.mybackyard.frontend.MainActivity.mbyViewModel;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.mybackyard.frontend.model.Note;
import com.mybackyard.frontend.repository.NoteRepository;
import com.mybackyard.frontend.singleton.GsonSingleton;

import java.io.UnsupportedEncodingException;
import java.security.GeneralSecurityException;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutionException;

public class NoteServiceImpl implements com.mybackyard.frontend.service.NoteService {

    private final NoteRepository noteRepository;
    private final Gson gson = GsonSingleton.getGson();

    public NoteServiceImpl(NoteRepository noteRepository) {
        this.noteRepository = noteRepository;
    }

    @Override
    public List<Note> getNotes() throws ExecutionException, InterruptedException, GeneralSecurityException, UnsupportedEncodingException {
        if (mbyViewModel.getNoteData().isEmpty()) {
            String retrievedJson = noteRepository.getAll();
            List<Note> retrievedNotes = gson.fromJson(retrievedJson, new TypeToken<List<Note>>() {}.getType());
            retrievedNotes.forEach(mbyViewModel::addNote);
        }
        return mbyViewModel.getNoteData();
    }

    @Override
    public Note getNote(String id) throws ExecutionException, InterruptedException {
        List<Note> notes = mbyViewModel.getNoteData();
        Note retrievedNote = null;
        for (Note note : notes) {
            if (note.getId() == Long.parseLong(id)) {
                retrievedNote = note;
            }
        }
        return retrievedNote;
    }

    @Override
    public Note postNote(Note note) throws ExecutionException, InterruptedException, GeneralSecurityException, UnsupportedEncodingException {
        mbyViewModel.invalidateNote();
        String serializedNote = gson.toJson(note);
        String retrievedJson = noteRepository.save(serializedNote);
        return gson.fromJson(retrievedJson, new TypeToken<Note>() {}.getType());
    }

    @Override
    public Note patchNote(Map patch, String id) throws ExecutionException, InterruptedException, GeneralSecurityException, UnsupportedEncodingException {
        mbyViewModel.invalidateNote();
        String serializedPatch = gson.toJson(patch);
        String retrievedJson = noteRepository.patch(serializedPatch, id);
        return gson.fromJson(retrievedJson, new TypeToken<Note>() {}.getType());
    }

    @Override
    public void deleteNote(String id) throws ExecutionException, InterruptedException, GeneralSecurityException, UnsupportedEncodingException {
        mbyViewModel.invalidateNote();
        noteRepository.delete(id);
    }
}
