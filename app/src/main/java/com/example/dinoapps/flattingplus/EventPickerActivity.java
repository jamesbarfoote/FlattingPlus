package com.example.dinoapps.flattingplus;

import android.content.Intent;
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
                setDesc(dec.toString());
                //Go back to calendar fragment
                //Intent n = new Intent(v.getContext(), CalendarFragment.class);
                //startActivity(new Intent(v.getContext(), CalendarFragment.class));
                finish();
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


}
