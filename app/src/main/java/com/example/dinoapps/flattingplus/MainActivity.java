package com.example.dinoapps.flattingplus;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import java.io.Console;
import java.util.ArrayList;

import it.neokree.materialnavigationdrawer.MaterialNavigationDrawer;

public class MainActivity extends MaterialNavigationDrawer {
    ArrayList<DataObject> res = new ArrayList<DataObject>();
static DBHelper dbHelper;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        dbHelper = new DBHelper(this);
        Log.v("created db", "db started");

//        if(dbHelper.getAllUsers().getCount() < 1)
//        {
            Log.v("Not signed it", "Going to login screen");
            Intent signin = new Intent(this, SignInActivity.class);
            startActivity(signin);
//        }

    }

    @Override
    public void init(Bundle savedInstanceState) {
        addSection(newSection("Notes", new NotesFragment()));
        addSection(newSection("Money", new MoneyFragment()));
        addSection(newSection("Shopping", new ShoppingFragment()));
        addSection(newSection("Calendar", new CalendarFragment()));
        addBottomSection(newSection("Settings", new SettingsFragment()));


    }



}
