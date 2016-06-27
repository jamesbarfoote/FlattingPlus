package com.example.dinoapps.flattingplus;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import static android.R.attr.button;

public class GroupLoginReg extends AppCompatActivity {
    String TAG = "GroupLogin";
    EditText groupname = (EditText) findViewById(R.id.groupNameText);
    EditText grouppass = (EditText) findViewById(R.id.groupPassword);
    TextView errorText = (TextView) findViewById(R.id.textView9);



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_group_login_reg);
        errorText.setVisibility(View.INVISIBLE);


        Button loginButton= (Button) findViewById(R.id.buttonLoginG);
        loginButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
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
                    //Find the group in the database using the password and email
                    JSONRequestLogin("/get/group", groupName, groupPassword);
                }
            }
        });


        Button registerButton= (Button) findViewById(R.id.buttonRegG);
        registerButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
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
                    JSONRequestReg("/add/group", groupName, groupPassword);
                }
            }
        });
    }

    public void JSONRequestLogin(String route, String gname, String password)
    {
        String baseURL = "https://flattingplus.herokuapp.com";
        RequestQueue queue = Volley.newRequestQueue(this);
        String url = baseURL + route + "?group=" + gname + "&gpass=" + password;

        JsonObjectRequest req = new JsonObjectRequest(url, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            Log.v("Response:%n %s", response.toString(4));

                            if(response.length() < 1)//No group found
                            {
                                errorText.setText("Groupname and/or password incorrect");
                            }
                            else
                            {
                                //TODO
                                // Returned something so we want to store it in our internal database and return user to main screen
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
        queue.add(req);
    }

    public void JSONRequestReg(String route, String gname, String password)
    {
        String baseURL = "https://flattingplus.herokuapp.com";
        RequestQueue queue = Volley.newRequestQueue(this);
        String url = baseURL + route;

        // Post params to be sent to the server
        JSONObject group = new JSONObject();
        try {
            group.put("group", gname);
            group.put("gpass", password);

        } catch (JSONException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        JsonObjectRequest req = new JsonObjectRequest(url, group,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            Log.v("Response:%n %s", response.toString(4));
                            //TODO add group to user and store the group in to local db
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
        queue.add(req);
    }


}
