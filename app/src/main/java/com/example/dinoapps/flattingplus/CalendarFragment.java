package com.example.dinoapps.flattingplus;

import android.content.ContentValues;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.text.format.Time;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import com.tyczj.extendedcalendarview.CalendarProvider;
import com.tyczj.extendedcalendarview.Day;
import com.tyczj.extendedcalendarview.Event;
import com.tyczj.extendedcalendarview.ExtendedCalendarView;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.List;
import java.util.TimeZone;
import java.util.concurrent.TimeUnit;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link CalendarFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link CalendarFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class CalendarFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    //CalendarView calendar;
    ExtendedCalendarView calendar;
    ListView listViewCalendarItems;
    ArrayList<Event> itemsList;
    ArrayAdapter<String> adapter;
    private String dateData;
    private String timeData;
    private String descriptionData;
    private int dayOfWeek;
    private int monthOfYear;
    private int year;
    private int hours;
    private int minutes;


    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;

    public CalendarFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment CalendarFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static CalendarFragment newInstance(String param1, String param2) {
        CalendarFragment fragment = new CalendarFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }



    }



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        View v = inflater.inflate(R.layout.fragment_calendar, container, false);
        // this.calendar = (CalendarView) v.findViewById(R.id.calendarViewMain);

        this.listViewCalendarItems = (ListView) v.findViewById(R.id.listViewCalendarItems);
        //Disable Scrolling
//        this.listViewCalendarItems.setOnTouchListener(new View.OnTouchListener() {
//            @Override
//            public boolean onTouch(View v, MotionEvent event) {
//                return true;
//            }
//        });


        this.calendar = (ExtendedCalendarView)v.findViewById(R.id.calendar);
//        this.calendar.setGesture(ExtendedCalendarView.LEFT_RIGHT_GESTURE);

        calendar.setOnDayClickListener(new ExtendedCalendarView.OnDayClickListener() {
            @Override
            public void onDayClicked(AdapterView<?> adapter, View view,
                                     int position, long id, Day day) {

                boolean dayIsClicked = true;
                Day clickedDay = day;
                getScheduleDetails(day);
                                Toast.makeText(getContext(), day.getDay() + "/ " + day.getMonth() + "/ " + day.getYear() + " Events: " + day.getNumOfEvenets(), Toast.LENGTH_LONG).show();
                ArrayList<String> myStringArray1 = new ArrayList<String>();
                //myStringArray1.add(day.getEvents().get(0).getDescription());
                //adapter = new AdapterView<>(getActivity(), R.layout.listitem, myStringArray1);
                if(itemsList.size() > 0)
                {
                    ArrayList<String> dayEvents = new ArrayList<String>();
                    for(Event e: itemsList)
                    {
                        dayEvents.add(e.getDescription());
                        listViewCalendarItems.setAdapter(new ArrayAdapter(getContext(), R.layout.listitem, R.id.txtview, dayEvents));

                    }
                }
                else
                {
                    listViewCalendarItems.setAdapter(new ArrayAdapter(getContext(), R.layout.listitem,R.id.txtview, itemsList));

                }
                //listViewCalendarItems.setAdapter(adapter);
//                listViewCalendarItems.setAdapter(new ListAdapter(getContext(), R.layout.itemlist, this.itemsList));

            }

        });



        //initializes the calendarview
       // initializeCalendar();

        FloatingActionButton fab = (FloatingActionButton) v.findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                final FragmentTransaction ft = getFragmentManager().beginTransaction();
//                ft.replace(R.id.calendar_view, new EventPicker(), "NewFragmentTag");
//                ft.commit();
                EventPickerActivity epa = new EventPickerActivity();
                Intent ep = new Intent(getContext(), EventPickerActivity.class);
                startActivity(ep);


            }
        });

        return v;
    }

    private void getScheduleDetails(Day day) {

        this.itemsList = new ArrayList();

        for (Event e : day.getEvents()) {
            this.itemsList.add(e);
        }
    }



    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

//public void OnDateSelectedListener()
//{
//    MaterialCalendarView.getSelectedDates();
//}
    @Override
    public void onResume()
    {
        super.onResume();
        String hasSuccess;
        String res;
        SharedPreferences suc = getActivity().getSharedPreferences("hasSuccess", 0);
        hasSuccess = suc.getString("Success", null);
        Log.v("Cal Frag", "success: " + hasSuccess);

        if(hasSuccess !=null && hasSuccess.equals("true")) {
            suc.edit().clear().commit();

            SharedPreferences settings = getActivity().getSharedPreferences("DateData", 0);
            dateData = settings.getString("Date", null);

            SharedPreferences timeS = getActivity().getSharedPreferences("TimeData", 0);
            timeData = timeS.getString("Time", null);

            SharedPreferences descS = getActivity().getSharedPreferences("DescriptionData", 0);
            descriptionData = descS.getString("Description", null);

            settings.edit().clear().commit();
            timeS.edit().clear().commit();
            descS.edit().clear().commit();


            Log.v("CalFrag Res", "Date: " + dateData + " Time: " + timeData + " Description: " + descriptionData);

            if(dateData != null && timeData != null && descriptionData != null) {
                addEvent(dateData, timeData, descriptionData);
            }
        }
    }

    public void addEvent(String date, String time, String description)
    {
        ContentValues values = new ContentValues();
        values.put(CalendarProvider.COLOR, Event.COLOR_RED);
        values.put(CalendarProvider.DESCRIPTION, description);
        values.put(CalendarProvider.LOCATION, "Some location");
        values.put(CalendarProvider.EVENT, "Event name");

        splitDate(date);
        splitTime(time);

        Calendar cal = Calendar.getInstance();
        //dateData = day,month,year
        //timeData = hours:minutes
        cal.set(year, monthOfYear, dayOfWeek, hours, minutes);
        values.put(CalendarProvider.START, cal.getTimeInMillis());
        TimeZone tz = TimeZone.getDefault();
        int StartDayJulian = Time.getJulianDay(cal.getTimeInMillis(), TimeUnit.MILLISECONDS.toSeconds(tz.getOffset(cal.getTimeInMillis())));
        values.put(CalendarProvider.START_DAY, StartDayJulian);

        cal.set(year, monthOfYear, dayOfWeek, hours, minutes);
        int endDayJulian = Time.getJulianDay(cal.getTimeInMillis(), TimeUnit.MILLISECONDS.toSeconds(tz.getOffset(cal.getTimeInMillis())));

        values.put(CalendarProvider.END, cal.getTimeInMillis());
        values.put(CalendarProvider.END_DAY, endDayJulian);

        Uri uri = getContext().getContentResolver().insert(CalendarProvider.CONTENT_URI, values);
    }




    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p/>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }

    public void splitDate(String date)
    {
        List<String> split = Arrays.asList(date.split(","));
        dayOfWeek = Integer.parseInt(split.get(0));
        monthOfYear = Integer.parseInt(split.get(1));
        year = Integer.parseInt(split.get(2));
        Log.v("Split date", "Day: " + dayOfWeek + " Month: " + monthOfYear + " Year: " + year);
    }

    public void splitTime(String time)
    {
        List<String> split = Arrays.asList(time.split(":"));
        hours = Integer.parseInt(split.get(0));
        minutes = Integer.parseInt(split.get(1));
        Log.v("Split time", "Hours: " + hours + " Minutes: " + minutes);

    }
}
