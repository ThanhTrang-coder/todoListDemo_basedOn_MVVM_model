package com.example.todolistbasedonmvvm.Repository;

import android.app.Application;
import android.widget.Toast;

import com.example.todolistbasedonmvvm.Dao.NoteDao;
import com.example.todolistbasedonmvvm.Database.NoteDatabase;
import com.example.todolistbasedonmvvm.Entity.Note;

import java.util.List;

import io.reactivex.Observable;
import io.reactivex.Observer;
import io.reactivex.Single;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

public class NoteRepository {
    private NoteDao noteDao;
    Application application;
    List<Note> notes;
    private Observable<List<Note>> allNotes;
    private NoteDatabase noteDatabase;
    private CompositeDisposable compositeDisposable;

    public NoteRepository(Application application) {
        noteDatabase = NoteDatabase.getInstance(application);
        noteDao = noteDatabase.getNoteDao();
        allNotes = noteDao.getAllNotes();
        compositeDisposable = new CompositeDisposable();
    }

    public void insert (Note note) {
        compositeDisposable.add(Single.fromCallable(() -> noteDao.insert(note))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(this::insertNote, this::showError));
    }

    public void update (Note note) {
        compositeDisposable.add(Single.fromCallable(() -> noteDao.update(note))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(this::updateNoteSuccess, this::showError));
    }

    public void delete (Note note) {
        compositeDisposable.delete(Single.fromCallable(() -> noteDao.delete(note))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(this::showSuccess, this::showError));
    }

    public void deleteAll() {
        compositeDisposable.addAll(Single.fromCallable(() -> noteDao.deleteAll())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(this::showSuccess, this::showError));
    }

    public Observable<List<Note>> getAllNotes() {
        return allNotes;
    }

    private void insertNote(Long along) {
    }

    private void updateNoteSuccess(Integer integer) {
    }

    private void showSuccess(Integer integer) {

    }

    private void showError(Throwable throwable) {

    }
}
