package com.example.dinoapps.flattingplus;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import static android.R.attr.button;

public class GroupLoginReg extends AppCompatActivity {
    private String TAG = "GroupLogin";
    EditText groupname;
    EditText grouppass;
    TextView errorText;
//    static DBHelper dbHelper;
    RequestQueue queue;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_group_login_reg);
        queue = Volley.newRequestQueue(this);
        groupname = (EditText) findViewById(R.id.groupNameText);
        grouppass = (EditText) findViewById(R.id.groupPassword);
        errorText = (TextView) findViewById(R.id.textView9);
        errorText.setVisibility(View.INVISIBLE);
        Button loginButton= (Button) findViewById(R.id.buttonLoginG);
        loginButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                if(groupname.getText().toString().equals("") || grouppass.getText().toString().equals(""))
                {
                    Log.v(TAG, "Password or group name is empty");
                    errorText.setText("Please make sure that both name and password are not empty and try again");
                    errorText.setVisibility(View.VISIBLE);
                }
                else
                {
                    String groupName = groupname.getText().toString();
                    String groupPassword = grouppass.getText().toString();
                    //Find the group in the database using the password and email
                    JSONRequestLogin("/get/flatgroup", groupName, groupPassword);
                }
            }
        });


        Button registerButton= (Button) findViewById(R.id.buttonRegG);
        registerButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Log.v(TAG, "reg button clicked");
                if(groupname.equals("") || grouppass.equals(""))
                {
                    Log.v(TAG, "Password or group name is empty");
                    errorText.setText("Please make sure that both name and password are not empty and try again");
                    errorText.setVisibility(View.VISIBLE);
                }
                else
                {
                    String groupName = groupname.getText().toString();
                    String groupPassword = grouppass.getText().toString();
                    addGroupTONetDB("/add/group", groupName, groupPassword);
                }
            }
        });
    }

    public void JSONRequestLogin(String route, String gname, String password)
    {
        String baseURL = "https://flattingplus.herokuapp.com";
        String url = baseURL + route + "?gname=" + gname + "&pass=" + password;

        JsonArrayRequest getRequest = new JsonArrayRequest( Request.Method.GET, url, null,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        try {
                            Log.v("Response:group log reg ", response.toString(4));

                            if(response.length() < 1)//No group found
                            {
                                errorText.setText("Groupname and/or password incorrect");
                                Toast.makeText(getApplicationContext(), "Groupname and/or password incorrect",
                                        Toast.LENGTH_LONG).show();
                            }
                            else
                            {
                               String groupName = groupname.getText().toString();
                                MainActivity.dbHelper.addGroupToUser(groupName);

                                //Add group info to local db
                                JSONObject jObj = response.getJSONObject(0);
                                String id = jObj.getString("id");
                                groupName = jObj.getString("groupname");
                                String notes = jObj.getString("notes");
                                String shoppinglist = jObj.getString("shoppinglist");
                                String calendar = jObj.getString("calendar");
                                String money = jObj.getString("money");

                                Toast.makeText(getApplicationContext(), "Signin successful!",
                                        Toast.LENGTH_LONG).show();
                                MainActivity.dbHelper.updateGroup(groupName, shoppinglist, calendar, money, notes);

                                Intent home = new Intent(getApplicationContext(), MainActivity.class);
                                startActivity(home);
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

//    public void JSONRequestReg(String route, String gname, String password)
//    {
//        String baseURL = "https://flattingplus.herokuapp.com";
//
//        String url = baseURL + route;
//
//        // Post params to be sent to the server
//        JSONObject group = new JSONObject();
//        try {
//            group.put("group", gname);
//            group.put("gpass", password);
//
//        } catch (JSONException e) {
//            // TODO Auto-generated catch block
//            e.printStackTrace();
//        }
//
//        JsonObjectRequest req = new JsonObjectRequest(Request.Method.PUT, url, group,
//                new Response.Listener<JSONObject>() {
//                    @Override
//                    public void onResponse(JSONObject response) {
//                        //try {
//                            //Add group to user
//                            String groupName = groupname.getText().toString();
//                            dbHelper.addGroupToUser(groupName);
//
//                            //Add group to local db
//                            dbHelper.insertGroup(groupName, "Empty", "Empty", "Empty", "Empty");
//
//                            Intent home = new Intent(getApplicationContext(), MainActivity.class);
//                            startActivity(home);
//                        //} catch (JSONException e) {
//                        //    e.printStackTrace();
//                        //}
//                    }
//                }, new Response.ErrorListener() {
//            @Override
//            public void onErrorResponse(VolleyError error) {
//                VolleyLog.e("Error: ", error.getMessage());
//            }
//        });
//
//        // add the request object to the queue to be executed
//        queue.add(req);
//    }

    public void addGroupTONetDB(String route, String gname, String password)
    {
        Log.v(TAG, gname + " " + password);
        String baseURL = "https://flattingplus.herokuapp.com";
        String url = baseURL + route;

         /*Post data*/
        Map<String, String> jsonParams = new HashMap<String, String>();

        jsonParams.put("group", gname);
        jsonParams.put("gpass", password);

        JsonObjectRequest postRequest = new JsonObjectRequest( Request.Method.PUT, url, new JSONObject(jsonParams),
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
//                        try
//                        {
                            String groupName = groupname.getText().toString();
                            MainActivity.dbHelper.addGroupToUser(groupName);

                            //Add group to local db
                            MainActivity.dbHelper.insertGroup(groupName, "Empty", "Empty", "Empty", "Empty");

                            Intent home = new Intent(getApplicationContext(), MainActivity.class);
                            startActivity(home);
//                        } catch (JSONException e) {
//                            e.printStackTrace();
//                        }

                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        //   Handle Error
                        Log.v(TAG, "Error: " + error);
                    }
                }) {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                HashMap<String, String> headers = new HashMap<String, String>();
                headers.put("Content-Type", "application/json; charset=utf-8");
                headers.put("User-agent", System.getProperty("http.agent"));
                return headers;
            }
        };
        queue.add(postRequest);
    }


}
