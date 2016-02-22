package com.example.dinoapps.flattingplus;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;

import it.neokree.materialnavigationdrawer.MaterialNavigationDrawer;

public class MainActivity extends MaterialNavigationDrawer {

    @Override
    public void init(Bundle savedInstanceState) {
        addSection(newSection("Notes", new NotesFragment()));
        addSection(newSection("Money", new MoneyFragment()));
        addSection(newSection("Shopping", new ShoppingFragment()));
        addSection(newSection("Calendar", new CalendarFragment()));
        addBottomSection(newSection("Settings", new SettingsFragment()));


    }

}
