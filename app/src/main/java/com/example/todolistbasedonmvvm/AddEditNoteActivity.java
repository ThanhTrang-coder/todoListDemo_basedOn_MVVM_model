package com.example.todolistbasedonmvvm;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.NumberPicker;
import android.widget.Toast;

import com.example.todolistbasedonmvvm.Entity.Note;

import java.io.Serializable;

public class AddEditNoteActivity extends AppCompatActivity {
    private EditText edtTitle, edtDescription;
    NumberPicker numberPicker;
    Note note;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_edit_note);

        edtTitle = findViewById(R.id.edtTitle);
        edtDescription = findViewById(R.id.edtDescription);
        numberPicker = findViewById(R.id.numberPicker);
        numberPicker.setMaxValue(10);
        numberPicker.setMinValue(0);

        getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_arrow);

        if(getIntent().hasExtra("id")) {
            setTitle("Edit Note");
            edtTitle.setText(getIntent().getStringExtra("title"));
            edtDescription.setText(getIntent().getStringExtra("description"));
            numberPicker.setValue(getIntent().getIntExtra("priority", 1));
        } else {
            setTitle("Add Note");
        }

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.note_add, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.save_note) {
            saveNote();
        }
        return true;
    }

    private void saveNote() {
        String title = edtTitle.getText().toString();
        String description = edtDescription.getText().toString();
        int priority = numberPicker.getValue();

        Intent intent = new Intent();
        intent.putExtra("note_title", title);
        intent.putExtra("note_description", description);
        intent.putExtra("note_priority", priority);
        int id = getIntent().getIntExtra("id", -1);
        if (id != -1) {
            intent.putExtra("id", id);
        }
        setResult(RESULT_OK, intent);
        finish();
    }
}