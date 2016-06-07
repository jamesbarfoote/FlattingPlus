package com.example.dinoapps.flattingplus;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link NotesFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link NotesFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class NotesFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private boolean addButtonClicked;

    DBHelper dbHelper= new DBHelper(getContext());


    private long numNotes = 0;

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private RecyclerView mRecyclerView;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;
    private static String LOG_TAG = "CardViewActivity";
    // ArrayList res;
    private int index = 0;

    private OnFragmentInteractionListener mListener;

    public NotesFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment NotesFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static NotesFragment newInstance(String param1, String param2) {
        NotesFragment fragment = new NotesFragment();
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

        View v = inflater.inflate(R.layout.fragment_notes, container, false);

        mRecyclerView = (RecyclerView) v.findViewById(R.id.my_recycler_view);
        mRecyclerView.setHasFixedSize(true);
        mLayoutManager = new LinearLayoutManager(getContext());
        mRecyclerView.setLayoutManager(mLayoutManager);
        mAdapter = new MyRecycleViewAdapter(getDataSet());
        mRecyclerView.setAdapter(mAdapter);

        // Code to Add an item with default animation
        //((MyRecyclerViewAdapter) mAdapter).addItem(obj, index);

        // Code to remove an item with default animation
        //((MyRecyclerViewAdapter) mAdapter).deleteItem(index);


        FloatingActionButton fab = (FloatingActionButton) v.findViewById(R.id.floatAddNote);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                final FragmentTransaction ft = getFragmentManager().beginTransaction();
//                ft.replace(R.id.calendar_view, new EventPicker(), "NewFragmentTag");
//                ft.commit();
                // AddNoteActivity nn = new AddNoteActivity();
                addButtonClicked = true;
                Log.v("add button", "Set to: " + addButtonClicked);
                Intent ni = new Intent(getContext(), AddNoteActivity.class);
                startActivity(ni);


            }
        });

        return v;
    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
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

    @Override
    public void onResume() {
        super.onResume();
        Log.v("Index", "Size " + index);
        Log.v("Resumes", "In resume for notes fragment");
        Log.v("add button", "Set to: " + addButtonClicked);

        //get the data from shared prefs
//        SharedPreferences notesT = getActivity().getSharedPreferences("NotesTitle", 0);
//        title = notesT.getString("NotesT", null);
//
//        SharedPreferences notesN = getActivity().getSharedPreferences("NotesText", 0);
//        note = notesN.getString("NotesTxt", null);
//
//        Log.v("Notes Frag", title + " Note: " + note);
        Cursor cursor = MainActivity.dbHelper.getNotesCount();
        int cnt = 0;
        if(cursor != null) {
            Log.v("Non null", "cursor wasnt null");
            cnt = cursor.getCount();
        }
                Log.v("Notes count"," count: " + cnt);

        if(cnt > this.numNotes)
        {
            Log.v("adding note", "need to add");
            //add the new notes
            long numNew = cnt - this.numNotes;
            //Go through all the new notes and add them
            Cursor c = MainActivity.dbHelper.getNotes();
            ArrayList<String> title= new ArrayList<String>();
            if (cursor.moveToFirst()) {

                while (cursor.isAfterLast() == false) {
                    String name = cursor.getString(cursor
                            .getColumnIndex("title"));

                    title.add(name);
                    cursor.moveToNext();
                }
            }


            ArrayList<String> content= new ArrayList<String>();
            if (cursor.moveToFirst()) {

                while (cursor.isAfterLast() == false) {
                    String name = cursor.getString(cursor
                            .getColumnIndex("content"));

                    content.add(name);
                    cursor.moveToNext();
                }
            }



            for (int i = content.size() - (int)numNew; i < content.size(); i++)
            {
                String t = title.get(i);
                    String cont = content.get(i);
Log.v("title and content:", "title: " + t + " content: " + cont);
                    addCurrentNote(t, cont);
                    ArrayList<DataObject> d = getDataSetTest();


                    if(d != null) {
                        mAdapter = new MyRecycleViewAdapter(d);
                        mRecyclerView.setAdapter(mAdapter);
                    }

                    addButtonClicked = false; //reset the clicked status

            }
            this.numNotes += numNew;

        }
//
        ((MyRecycleViewAdapter) mAdapter).setOnItemClickListener(new MyRecycleViewAdapter
                .MyClickListener() {
            @Override
            public void onItemClick(int position, View v) {
                Log.i(LOG_TAG, " Clicked on Item " + position);
            }
        });
    }

    private ArrayList<DataObject> getDataSet() {
        ArrayList results = new ArrayList<DataObject>();
        for (int index = 0; index < 20; index++) {
            DataObject obj = new DataObject("Title " + index,
                    "Note stuff here " + index);
            results.add(index, obj);
        }
        return results;
    }

    private ArrayList<DataObject> getDataSetTest() {
        Set<String> currentNotes = getCurrentNotes();
        if(currentNotes != null) {
            ArrayList<String> data = new ArrayList<>();
            for (String s : currentNotes) {
                data.add(s);
            }

            ArrayList results = new ArrayList<DataObject>();
            for (int index = 0; index < data.size() - 1; index++) {
                DataObject obj = new DataObject(data.get(index),
                        data.get(index + 1));
                results.add(index, obj);
            }
            return results;
        }
        return null;
    }

    public void addCurrentNote(String title, String notes)
    {
        Log.v("Notes Frag add current", title + " Note: " + notes);

        Set<String> s = getCurrentNotes();
        if(s == null) {
            s = new HashSet<>();
        }
        s.add(title);
        s.add(notes);

        for(String n: s)
        {
            Log.v("HashSet", "" + n);
        }

        SharedPreferences notesD = getContext().getSharedPreferences("NotesData", 0);
        SharedPreferences.Editor edit = notesD.edit();
        edit.putStringSet("NotesData", s);
        edit.commit();
    }

    public Set<String> getCurrentNotes()
    {
        Set<String> notes;
        SharedPreferences notesC = getActivity().getSharedPreferences("NotesData", 0);
        notes = notesC.getStringSet("NotesData", null);
        return notes;

    }

}
