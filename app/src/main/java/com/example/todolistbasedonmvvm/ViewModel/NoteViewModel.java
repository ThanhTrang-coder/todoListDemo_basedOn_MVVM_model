package com.example.todolistbasedonmvvm.ViewModel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.example.todolistbasedonmvvm.Entity.Note;
import com.example.todolistbasedonmvvm.Repository.NoteRepository;

import java.util.List;

import io.reactivex.Observable;

public class NoteViewModel extends AndroidViewModel {
    private NoteRepository noteRepository;
    private Observable<List<Note>> allNotes;

    public NoteViewModel(@NonNull Application application) {
        super(application);
        noteRepository = new NoteRepository(application);
        allNotes = noteRepository.getAllNotes();
    }

    public void insert(Note note) {
        noteRepository.insert(note);
    }

    public void update(Note note) {
        noteRepository.update(note);
    }

    public void delete(Note note) {
        noteRepository.delete(note);
    }

    public void deleteAll() {
        noteRepository.deleteAll();
    }

    public Observable<List<Note>> getAllNotes() {
        return allNotes;
    }
}
