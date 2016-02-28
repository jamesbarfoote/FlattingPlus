package com.example.dinoapps.flattingplus;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.EditText;

public class AddNoteActivity extends AppCompatActivity {
    EditText titleText;
    EditText notesText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_note);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        titleText = (EditText) findViewById(R.id.newNoteTitleTxt);
        notesText = (EditText) findViewById(R.id.newNoteTxt);


        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    @Override
    public void onPause()
    {
        super.onPause();
        String title = titleText.getText().toString();
        String note = notesText.getText().toString();

        Log.v("Add note", "Title: " + title);
        SharedPreferences notesT = getSharedPreferences("NotesTitle", 0);
        SharedPreferences.Editor ed = notesT.edit();
        ed.putString("NotesT", title);
        ed.commit();

        Log.v("Add note", " Text: " + note);
        SharedPreferences notesN = getSharedPreferences("NotesText", 0);
        SharedPreferences.Editor edit = notesN.edit();
        edit.putString("NotesTxt", note);
        edit.commit();
    }
}
