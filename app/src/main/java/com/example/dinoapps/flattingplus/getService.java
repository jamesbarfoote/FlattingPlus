package com.example.dinoapps.flattingplus;

import android.app.Service;
import android.content.Intent;
import android.os.Bundle;
import android.os.IBinder;
import android.support.v4.app.Fragment;
import android.support.v7.widget.RecyclerView;
import android.util.Log;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;

import org.greenrobot.eventbus.EventBus;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class getService extends Service {
    private String TAG = "getService";
    private RequestQueue queue;

    public getService() {
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        queue = Volley.newRequestQueue(this);

        String groupName = intent.getStringExtra("groupname");//Get the data from the intent
        String date = intent.getStringExtra("date");
        String type = intent.getStringExtra("Type");

        getNewNotes(groupName, date, type);//update the category


        return Service.START_NOT_STICKY;
    }

    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        throw new UnsupportedOperationException("Not yet implemented");
    }

    public void getNewNotes(String groupName, String date, String type)
    {
        String baseURL = "https://flattingplus.herokuapp.com";
        String url;

        if(type.equals("Notes")) {
            url = baseURL + "/get/notes" + "?gname=" + groupName + "&date=" + date;

            JsonArrayRequest getRequest = new JsonArrayRequest(Request.Method.GET, url, null,
                    new Response.Listener<JSONArray>() {
                        @Override
                        public void onResponse(JSONArray response) {
                            try {
                                Log.v("Response:group log reg ", response.toString(4));

                                if (response.length() < 1)//No new notes
                                {
                                    Log.v(TAG, "No new notes");
                                }
                                else {
                                    Log.v(TAG, "Number of new notes: " + response.length());
                                    ArrayList<DataObject> data = new ArrayList<>();

                                    //Go through all the new notes adding them to the local db
                                    for(int i = 0; i < response.length(); i++) {
                                        //The object containing all the new notes
                                        JSONObject jObj = response.getJSONObject(i);
                                        String useremail = jObj.getString("creator");
                                        String title = jObj.getString("title");
                                        String content = jObj.getString("content");
                                        String flatgroup = jObj.getString("groupname");
                                        String creationTime = jObj.getString("currtime");
                                        MainActivity.dbHelper.insertNote(useremail, title, content, flatgroup, creationTime);
                                        Log.v(TAG, "Title: " + title + " Content: " + content + " Group name: " + flatgroup + " Creation time: " + creationTime);

                                        //Update the notes view so we can see the recently received note
                                        DataObject obj = new DataObject(title, content);
                                        data.add(obj);
                                    }
//                                NotesFragment nf = new NotesFragment();
//                                nf.update(data);

                                    //If true then the notes activity is visible
                                    if(NotesFragment.m_iAmVisible)
                                    {
                                        Log.v(TAG, "notes are visible");
                                        EventBus.getDefault().post(new MessageEvent("Need to restart notes!!"));
//                                    Intent restartNotes = new Intent(getApplicationContext(), NotesFragment.class);
//                                    startActivity(restartNotes);
                                    }

                                }
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                    }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    VolleyLog.e("Error: ", error.getMessage());
                }
            });

            // add the request object to the queue to be executed
            queue.add(getRequest);
        }
        else if(type.equals("Money"))
        {
            url = baseURL + "/get/money" + "?gname=" + groupName + "&date=" + date;

            JsonArrayRequest getRequest = new JsonArrayRequest(Request.Method.GET, url, null,
                    new Response.Listener<JSONArray>() {
                        @Override
                        public void onResponse(JSONArray response) {
                            try {
                                Log.v("Response:group log reg ", response.toString(4));

                                if (response.length() < 1)//No new notes
                                {
                                    Log.v(TAG, "No new money notes");
                                }
                                else {
                                    Log.v(TAG, "Number of new money notes: " + response.length());
                                    ArrayList<DataObject> data = new ArrayList<>();

                                    //Go through all the new notes adding them to the local db
                                    for(int i = 0; i < response.length(); i++) {
                                        //The object containing all the new money items
                                        JSONObject jObj = response.getJSONObject(i);
                                        String useremail = jObj.getString("creator");
                                        String title = jObj.getString("title");
                                        String content = jObj.getString("content");
                                        String flatgroup = jObj.getString("groupname");
                                        String creationTime = jObj.getString("currtime");
                                        MainActivity.dbHelper.insertMoney(useremail, title, content, flatgroup, creationTime);
                                        Log.v(TAG, "Title: " + title + " Content: " + content + " Group name: " + flatgroup + " Creation time: " + creationTime);

                                        //Update the money view so we can see the recently received money note
                                        DataObject obj = new DataObject(title, content);
                                        data.add(obj);
                                    }
//                                NotesFragment nf = new NotesFragment();
//                                nf.update(data);

                                    //If true then the money activity is visible
                                    if(MoneyFragment.m_iAmVisible)
                                    {
                                        Log.v(TAG, "money notes are visible");
                                        EventBus.getDefault().post(new MessageEvent("Need to restart money!!"));
//                                    Intent restartNotes = new Intent(getApplicationContext(), NotesFragment.class);
//                                    startActivity(restartNotes);
                                    }

                                }
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                    }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    VolleyLog.e("Error: ", error.getMessage());
                }
            });

            // add the request object to the queue to be executed
            queue.add(getRequest);
        }
        else //Shopping
        {
            url = baseURL + "/get/shopping" + "?gname=" + groupName + "&date=" + date;

            JsonArrayRequest getRequest = new JsonArrayRequest(Request.Method.GET, url, null,
                    new Response.Listener<JSONArray>() {
                        @Override
                        public void onResponse(JSONArray response) {
                            try {
                                Log.v("Response:group log reg ", response.toString(4));

                                if (response.length() < 1)//No new notes
                                {
                                    Log.v(TAG, "No new shopping items");
                                }
                                else {
                                    Log.v(TAG, "Number of new shopping items: " + response.length());
                                    ArrayList<DataObject> data = new ArrayList<>();

                                    //Go through all the new shopping items adding them to the local db
                                    for(int i = 0; i < response.length(); i++) {
                                        //The object containing all the new shopping items
                                        JSONObject jObj = response.getJSONObject(i);
                                        String useremail = jObj.getString("creator");
                                        String title = jObj.getString("title");
                                        String content = jObj.getString("content");
                                        String flatgroup = jObj.getString("groupname");
                                        String creationTime = jObj.getString("currtime");
                                        MainActivity.dbHelper.insertShopping(useremail, title, content, flatgroup, creationTime);
                                        Log.v(TAG, "Title: " + title + " Content: " + content + " Group name: " + flatgroup + " Creation time: " + creationTime);

                                        //Update the shopping view so we can see the recently received item
                                        DataObject obj = new DataObject(title, content);
                                        data.add(obj);
                                    }
//                                NotesFragment nf = new NotesFragment();
//                                nf.update(data);

                                    //If true then the shopping activity is visible
                                    if(ShoppingFragment.m_iAmVisible)
                                    {
                                        Log.v(TAG, "shopping itmes are visible");
                                        EventBus.getDefault().post(new MessageEvent("Need to restart shopping!!"));
//                                    Intent restartNotes = new Intent(getApplicationContext(), NotesFragment.class);
//                                    startActivity(restartNotes);
                                    }

                                }
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                    }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    VolleyLog.e("Error: ", error.getMessage());
                }
            });

            // add the request object to the queue to be executed
            queue.add(getRequest);
        }


    }
}
