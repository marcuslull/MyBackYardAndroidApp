package com.mybackyard.frontend.service;

import com.mybackyard.frontend.model.Note;

import java.io.UnsupportedEncodingException;
import java.security.GeneralSecurityException;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutionException;

public interface NoteService {
    List<Note> getNotes() throws ExecutionException, InterruptedException, GeneralSecurityException, UnsupportedEncodingException;
    Note getNote(String id) throws ExecutionException, InterruptedException;
    Note postNote(Note note) throws ExecutionException, InterruptedException, GeneralSecurityException, UnsupportedEncodingException;
    Note patchNote(Map patch, String id) throws ExecutionException, InterruptedException, GeneralSecurityException, UnsupportedEncodingException;
    void deleteNote(String id) throws ExecutionException, InterruptedException, GeneralSecurityException, UnsupportedEncodingException;
}
