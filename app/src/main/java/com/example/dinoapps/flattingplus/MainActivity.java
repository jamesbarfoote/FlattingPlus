package com.example.dinoapps.flattingplus;

import android.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;
import com.google.firebase.iid.FirebaseInstanceId;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

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

    // This method will be called when a MessageEvent is posted (in the UI thread for Toast)
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onMessageEvent(MessageEvent event) {
        Log.v(TAG, "got message from service: " + event.message);
        Toast.makeText(this, event.message, Toast.LENGTH_SHORT).show();

        NotesFragment fr = new NotesFragment();
        final FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        ft.replace(R.id.fragment_notes, fr);
        ft.commit();



    }

    // This method will be called when a SomeOtherEvent is posted
    @Subscribe
    public void handleSomethingElse(String event) {
//        doSomethingWith(event);
    }

    @Override
    public void onStart() {
        super.onStart();
        EventBus.getDefault().register(this);
    }

    @Override
    public void onStop() {
        EventBus.getDefault().unregister(this);
        super.onStop();
    }
}
