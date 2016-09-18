package com.example.dinoapps.flattingplus;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.github.brnunes.swipeablerecyclerview.SwipeableRecyclerViewTouchListener;

import java.util.ArrayList;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link ShoppingFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link ShoppingFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ShoppingFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    private boolean addButtonClicked;
    public static boolean m_iAmVisible;
    private String TAG = "ShoppingFragment";
    private long numNotes = 0;

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;

    private RecyclerView mRecyclerView;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;

    public ShoppingFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment ShoppingFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static ShoppingFragment newInstance(String param1, String param2) {
        ShoppingFragment fragment = new ShoppingFragment();
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
        m_iAmVisible = true;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_shopping, container, false);

        mRecyclerView = (RecyclerView) v.findViewById(R.id.shopping_recycler_view);
        mRecyclerView.setHasFixedSize(true);
        mLayoutManager = new LinearLayoutManager(getContext());
        mRecyclerView.setLayoutManager(mLayoutManager);
        mAdapter = new MyRecycleViewAdapter(getAndDisplay());
        mRecyclerView.setAdapter(mAdapter);

        m_iAmVisible = true;
        FloatingActionButton fab = (FloatingActionButton) v.findViewById(R.id.floatAddShopping);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Set a shared prefs so that our add notes activity knows the call is coming from the shopping fragment
                SharedPreferences sharedPreferences = PreferenceManager
                        .getDefaultSharedPreferences(getContext());
                SharedPreferences.Editor noteType = sharedPreferences.edit();
                noteType.putString("NoteType", "Shopping");
                noteType.commit();

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
        m_iAmVisible = true;


        //get all the info from the shopping table
        Cursor cursor = MainActivity.dbHelper.getShoppingCount();
        int cnt = 0;
        if(cursor != null) {
            cnt = cursor.getCount();
        }
        Log.v("Shopping count","Shopping count: " + cnt);

        if(cnt > this.numNotes)
        {
            Log.v("adding shopping note", "need to add shopping note");
            //add the new notes
            long numNew = cnt - this.numNotes;
            ArrayList<DataObject> d =getAndDisplay();
            update(d);

            this.numNotes += numNew;

        }
//
        ((MyRecycleViewAdapter) mAdapter).setOnItemClickListener(new MyRecycleViewAdapter
                .MyClickListener() {
            @Override
            public void onItemClick(int position, View v) {
                Log.i(TAG, " Clicked on Item " + position);
            }
        });

        SwipeableRecyclerViewTouchListener swipeTouchListener =
                new SwipeableRecyclerViewTouchListener(mRecyclerView,
                        new SwipeableRecyclerViewTouchListener.SwipeListener() {
                            @Override
                            public boolean canSwipeLeft(int position) {
                                return true;
                            }

                            @Override
                            public boolean canSwipeRight(int position) {
                                return true;
                            }

                            @Override
                            public void onDismissedBySwipeLeft(RecyclerView recyclerView, int[] reverseSortedPositions) {
                                for (int position : reverseSortedPositions) {
//                                    Toast.makeText(MainActivity.this, mItems.get(position) + " swiped left", Toast.LENGTH_SHORT).show();
//                                    mItems.remove(position);
                                    ((MyRecycleViewAdapter) mAdapter).deleteItem(position, "Shopping");
                                    Toast.makeText(getContext(), "Swiped Left " + position, Toast.LENGTH_LONG).show();

                                    mAdapter.notifyItemRemoved(position);
                                }
                                mAdapter.notifyDataSetChanged();
                            }

                            @Override
                            public void onDismissedBySwipeRight(RecyclerView recyclerView, int[] reverseSortedPositions) {
                                for (int position : reverseSortedPositions) {
//                                    Toast.makeText(MainActivity.this, mItems.get(position) + " swiped right", Toast.LENGTH_SHORT).show();
//                                    mItems.remove(position);
                                    ((MyRecycleViewAdapter) mAdapter).deleteItem(position, "Shopping");

                                    Toast.makeText(getContext(), "Swiped Right on Item " + position, Toast.LENGTH_LONG).show();

                                    mAdapter.notifyItemRemoved(position);
                                }
                                mAdapter.notifyDataSetChanged();
                            }
                        });

        mRecyclerView.addOnItemTouchListener(swipeTouchListener);
    }

    private ArrayList<DataObject> createDataObjs(ArrayList<String> title, ArrayList<String> content, ArrayList<String> times)
    {
        ArrayList<DataObject> data = new ArrayList<>();
        for(int i =0; i < title.size(); i++)
        {
            DataObject obj = new DataObject(title.get(i), content.get(i), times.get(i));
            data.add(obj);
        }
        return data;
    }

    private ArrayList<DataObject> getAndDisplay()
    {
        Cursor cursor = MainActivity.dbHelper.getShoppingCount();
        int cnt = 0;
        if(cursor != null) {
            cnt = cursor.getCount();
        }

        //add all the titles to an array
        ArrayList<String> title= new ArrayList<String>();
        if (cursor.moveToFirst()) {

            while (cursor.isAfterLast() == false) {
                String name = cursor.getString(cursor
                        .getColumnIndex("title"));

                title.add(name);
                cursor.moveToNext();
            }
        }

        //add all the content to another array
        ArrayList<String> content= new ArrayList<String>();
        if (cursor.moveToFirst()) {

            while (cursor.isAfterLast() == false) {
                String name = cursor.getString(cursor
                        .getColumnIndex("content"));

                content.add(name);
                cursor.moveToNext();
            }
        }

        //add all the times to another array
        ArrayList<String> times= new ArrayList<String>();
        if (cursor.moveToFirst()) {

            while (cursor.isAfterLast() == false) {
                String name = cursor.getString(cursor
                        .getColumnIndex("created"));

                times.add(name);
                cursor.moveToNext();
            }
        }

        ArrayList<DataObject> d = createDataObjs(title, content, times);
        return d;
    }
    public void update(ArrayList<DataObject> d)
    {
//        updateView(d);
        if(d != null) {
            mAdapter = new MyRecycleViewAdapter(d);
            mRecyclerView.setAdapter(mAdapter);
        }
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        m_iAmVisible = isVisibleToUser;

        if (m_iAmVisible) {
            Log.d(TAG, "this fragment is now visible");
        } else {
            Log.d(TAG, "this fragment is now invisible");
        }
    }


    @Override
    public void onPause() {
        super.onPause();
        m_iAmVisible = false;
        Log.v(TAG, "Pausing");
    }
}
