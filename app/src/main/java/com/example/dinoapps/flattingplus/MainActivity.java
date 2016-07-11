package com.example.dinoapps.flattingplus;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;
import com.google.firebase.iid.FirebaseInstanceId;

import java.util.ArrayList;

import it.neokree.materialnavigationdrawer.MaterialNavigationDrawer;

public class MainActivity extends MaterialNavigationDrawer {
    ArrayList<DataObject> res = new ArrayList<DataObject>();
static DBHelper dbHelper;
    String TAG = "MainActivity";
//    public static RequestQueue queue;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        RequestQueue queue = Volley.newRequestQueue(this);
        Log.d(TAG, "InstanceID token: " + FirebaseInstanceId.getInstance().getToken());
        dbHelper = new DBHelper(this);
        Log.v("created db", "db started");

        if(dbHelper.getAllUsers().getCount() < 1)
        {
            Log.v("Not signed it", "Going to login screen");
            Intent signin = new Intent(this, SignInActivity.class);
            startActivity(signin);
        }

//        if(dbHelper.getAllGroup().getCount() < 1)
//        {
//            Log.v(TAG, "User no group info");
//            Intent groupLogin = new Intent (this, GroupLoginReg.class);
//            startActivity(groupLogin);
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
