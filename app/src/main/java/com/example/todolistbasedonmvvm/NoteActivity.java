package com.example.todolistbasedonmvvm;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.example.todolistbasedonmvvm.Adapter.NoteAdapter;
import com.example.todolistbasedonmvvm.Entity.Note;
import com.example.todolistbasedonmvvm.ViewModel.NoteViewModel;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.Observer;
import io.reactivex.Scheduler;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

public class NoteActivity extends AppCompatActivity implements View.OnClickListener, onItemClickListener {
    public static final int ADD_NOTE_REQUEST = 1;
    public static final int EDIT_NOTE_REQUEST = 2;
    private NoteViewModel noteViewModel;
    private NoteAdapter noteAdapter;
    private RecyclerView rvNote;
    private FloatingActionButton fab;
    private List<Note> mNoteList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_note);

        fab = findViewById(R.id.fab);
        rvNote = findViewById(R.id.rv_note);

        fab.setOnClickListener(this);

        LinearLayoutManager manager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        rvNote.setLayoutManager(manager);
        rvNote.setHasFixedSize(true);
        noteAdapter = new NoteAdapter(this);

        noteViewModel = new ViewModelProvider(this).get(NoteViewModel.class);
        noteViewModel.getAllNotes()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(this::handleResponse, this::handleError, this::handleSuccess);

        new ItemTouchHelper(new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT) {
            @Override
            public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
                return false;
            }

            @Override
            public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {
                noteViewModel.delete(noteAdapter.getNoteAt(viewHolder.getAdapterPosition()));
                Toast.makeText(NoteActivity.this, "Delete successfully", Toast.LENGTH_SHORT).show();
            }
        }).attachToRecyclerView(rvNote);
    }

    private void handleResponse(List<Note> noteList) {
        mNoteList = new ArrayList<>(noteList);
        noteAdapter.setNotes(mNoteList);
        rvNote.setAdapter(noteAdapter);
    }

    private void handleError(Throwable error) {
        Toast.makeText(this, "Error: " + error.getLocalizedMessage(), Toast.LENGTH_SHORT).show();
    }

    private void handleSuccess() {
        Toast.makeText(this, "Get data success!", Toast.LENGTH_SHORT).show();
    }
    
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.note_delete_all, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();
        if(id == R.id.notes_delete) {
            Toast.makeText(this, "Delete All note successfully", Toast.LENGTH_SHORT).show();
            noteViewModel.deleteAll();
        }
        return true;
    }

    @Override
    public void onClick(View view) {
        if(view.getId() == R.id.fab) {
            Intent intent = new Intent(this, AddEditNoteActivity.class);
            startActivityForResult(intent, ADD_NOTE_REQUEST);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == ADD_NOTE_REQUEST && resultCode == RESULT_OK) {
            String note_title = data.getStringExtra("note_title");
            String note_description = data.getStringExtra("note_description");
            int note_priority = data.getIntExtra("note_priority", 0);

            noteViewModel.insert(new Note(note_title, note_description, note_priority));
            Toast.makeText(this, "Note saved", Toast.LENGTH_SHORT).show();
        }
        else if (requestCode == EDIT_NOTE_REQUEST && resultCode == RESULT_OK) {
            String note_title = data.getStringExtra("note_title");
            String note_description = data.getStringExtra("note_description");
            int note_priority = data.getIntExtra("note_priority", 0);
            int note_id = data.getIntExtra("id", 0);

            Note note = new Note(note_title, note_description, note_priority);
            note.setId(note_id);
            noteViewModel.update(note);
            Toast.makeText(this, "Note updated", Toast.LENGTH_SHORT).show();
        }
        else {
            Toast.makeText(this, "Note not saved", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onItemClick(Note note) {
        Intent intent = new Intent(this, AddEditNoteActivity.class);
        intent.putExtra("id", note.getId());
        intent.putExtra("title", note.getTitle());
        intent.putExtra("description", note.getDescription());
        intent.putExtra("priority", note.getPriority());
        startActivityForResult(intent, EDIT_NOTE_REQUEST);
    }
}