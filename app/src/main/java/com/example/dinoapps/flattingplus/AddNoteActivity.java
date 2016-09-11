package com.example.dinoapps.flattingplus;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.EditText;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class AddNoteActivity extends AppCompatActivity {
    EditText titleText;
    EditText notesText;
    DBHelper dbHelper= new DBHelper(this);
    String TAG = "Add Note Activity";
    RequestQueue queue;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_note);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        queue = Volley.newRequestQueue(this);

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
        if(title.equals(""))
        {
            title = "Title";
        }

        if(note.equals(""))
        {
            note = "Note";
        }

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
        String userEmail = MainActivity.dbHelper.getEmail();
        String flatgroup = MainActivity.dbHelper.getGroup();
        Long tsLong = System.currentTimeMillis()/1000;
        String ts = tsLong.toString();

        Log.v("Add note activity", "Email: " + userEmail + " Group: " + flatgroup + " Time created: " + ts);

        //Figure out what type of note we have
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
        String type = prefs.getString("NoteType", "Empty");

        Log.v(TAG, "Type: " + type);

        if(type == "Notes") {
            MainActivity.dbHelper.insertNote(userEmail, title, note, flatgroup, ts);
        }
        else if(type.equals("Money"))
        {
            MainActivity.dbHelper.insertMoney(userEmail, title, note, flatgroup, ts);
        }
        else if(type.equals("Shopping"))
        {
            MainActivity.dbHelper.insertShopping(userEmail, title, note, flatgroup, ts);
        }

        //Upload the note to the internet db
        uploadNote(userEmail, flatgroup, ts, title, note, type);

    }

    public void uploadNote(String userEmail, String flatgroup, String ts, String title, String note, String type)
    {
        Log.v("AddNote", userEmail + " " + flatgroup + " " + ts + " " + title + " " + note);
        String baseURL = "https://flattingplus.herokuapp.com";
        String url;

        if(type.equals("Notes")) {
            url = baseURL + "/add/note";
        }
        else if(type.equals("Money"))
        {
            url = baseURL + "/add/money";
        }
        else //Shopping
        {
            url = baseURL + "/add/shopping";
        }

        /*Post data*/
        JSONObject notes = new JSONObject();
        try {
            notes.put("group", flatgroup);
            notes.put("notetitle", title);
            notes.put("notecontent", note);
            notes.put("notecreator", userEmail);
            notes.put("notetimestamp", ts);

        } catch (JSONException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        JsonObjectRequest postRequest = new JsonObjectRequest( Request.Method.PUT, url, notes,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try
                        {
                            Log.v(TAG, "note added: " + response.toString(4));
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        //   Handle Error
                        Log.v(TAG, "Error: " + error);
                    }
                }) {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                HashMap<String, String> headers = new HashMap<String, String>();
                headers.put("Content-Type", "application/json; charset=utf-8");
                headers.put("User-agent", System.getProperty("http.agent"));
                return headers;
            }
        };
        queue.add(postRequest);
    }
}
