package com.example.dinoapps.flattingplus;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.codetroopers.betterpickers.calendardatepicker.CalendarDatePickerDialogFragment;
import com.codetroopers.betterpickers.radialtimepicker.RadialTimePickerDialogFragment;

public class EventPickerActivity extends AppCompatActivity implements CalendarDatePickerDialogFragment.OnDateSetListener, RadialTimePickerDialogFragment.OnTimeSetListener{
    TextView startDate;
    TextView timeDate;
    String time;
    String date;
    String description;
    Button doneB;
    static boolean isRunning = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_event_picker);
//        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
//        setSupportActionBar(toolbar);


      //  getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        this.startDate = (TextView) findViewById(R.id.startDateTxt);
        this.timeDate = (TextView) findViewById(R.id.timeTxt);
        this.doneB = (Button) findViewById(R.id.doneButtonPicker);

        this.startDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                CalendarDatePickerDialogFragment cdp = new CalendarDatePickerDialogFragment()
                        .setOnDateSetListener(EventPickerActivity.this);
                cdp.show(getSupportFragmentManager(), "date picker");

            }
        });

        this.timeDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                RadialTimePickerDialogFragment rtpd = new RadialTimePickerDialogFragment()
                        .setOnTimeSetListener(EventPickerActivity.this);
                rtpd.show(getSupportFragmentManager(), "time picker");
            }

        });


        this.doneB.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v) {
                Log.v("Done","Done clicked");
                TextView dec = (TextView)findViewById(R.id.eventDiscriptionTxt);
                setDesc(dec.getText().toString());
                //Go back to calendar fragment
                //Intent n = new Intent(v.getContext(), CalendarFragment.class);
                //startActivity(new Intent(v.getContext(), CalendarFragment.class));

                // We need an Editor object to make preference changes.
                // All objects are from android.context.Context
                SharedPreferences settings = getSharedPreferences("DateData", 0);
                SharedPreferences.Editor editor = settings.edit();
                editor.putString("Date", date);

                // Commit the edits!
                editor.commit();
                if(time.equals(null) || date.equals(null) || description.equals(null))
                {
                    Log.v("Event picker", "Missing data");
                }
                else
                {
                    finish();
                }
            }
        });
    }

    public void setDesc(String description)
    {
        this.description = description;
    }

    @Override
    public void onDateSet(CalendarDatePickerDialogFragment dialog, int year, int monthOfYear, int dayOfMonth) {
        startDate.setText(getString(R.string.calendar_date_picker_result_values, year, monthOfYear, dayOfMonth));
        this.date = dayOfMonth + "," + monthOfYear + "," + year;
    }

    @Override
    public void onTimeSet(RadialTimePickerDialogFragment dialog, int hourOfDay, int minute) {
        timeDate.setText(getString(R.string.radial_time_picker_result_value, hourOfDay, minute));
        this.time = hourOfDay + ":" + minute;
    }

    public String getDate()
    {
        return this.date;
    }

    public String getDateTime()
    {
        return this.time;
    }

    public String getDescription()
    {
        return this.description;
    }

    @Override
    public void onPause()
    {
        super.onPause();
        Log.v("Paused", "On pause");
        isRunning = false;

        if(date != null) {
            Log.v("EventPicker", "success");
            SharedPreferences paus = getSharedPreferences("hasSuccess", 0);
            SharedPreferences.Editor ed = paus.edit();
            ed.putString("Success", "true");

            // Commit the edits!
            ed.commit();
        }
        Log.v("Event Picker", "Date: " + date);

        //Share the date result
        SharedPreferences settings = getSharedPreferences("DateData", 0);
        SharedPreferences.Editor editor = settings.edit();
        editor.putString("Date", date);
        editor.commit();

        //Share the time result
        SharedPreferences timeS = getSharedPreferences("TimeData", 0);
        SharedPreferences.Editor timeEd = timeS.edit();
        timeEd.putString("Time", time);
        timeEd.commit();

        //share the description result
        SharedPreferences descS = getSharedPreferences("DescriptionData", 0);
        SharedPreferences.Editor descEd = descS.edit();
        descEd.putString("Description", description);
        descEd.commit();
    }

    @Override
    public void onResume()
    {
        super.onResume();
        Log.v("Resumed", "On resume");
        isRunning = true;

        SharedPreferences paus = getSharedPreferences("isPaused", 0);
        SharedPreferences.Editor ed = paus.edit();
        ed.putBoolean("Paused", isRunning);

        // Commit the edits!
        ed.commit();
    }


}
