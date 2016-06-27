package com.example.dinoapps.flattingplus;

import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.OptionalPendingResult;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class SignInActivity extends AppCompatActivity implements
        GoogleApiClient.OnConnectionFailedListener,
        View.OnClickListener {

    private GoogleApiClient mGoogleApiClient;
    private static final int RC_SIGN_IN = 9001;
    private ProgressDialog mProgressDialog;
    private TextView mStatusTextView;
    private TextView titleThing;
    static DBHelper dbHelper;
    String TAG = "Signin";
    RequestQueue queue;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_in);
        queue = Volley.newRequestQueue(this);

        titleThing = (TextView)findViewById(R.id.textView3);
        findViewById(R.id.sign_in_button).setOnClickListener(this);

        // Configure sign-in to request the user's ID, email address, and basic
// profile. ID and basic profile are included in DEFAULT_SIGN_IN.
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .build();

        // Build a GoogleApiClient with access to the Google Sign-In API and the
// options specified by gso.
        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .enableAutoManage(this /* FragmentActivity */, this /* OnConnectionFailedListener */)
                .addApi(Auth.GOOGLE_SIGN_IN_API, gso)
                .build();

        SignInButton signInButton = (SignInButton) findViewById(R.id.sign_in_button);
        signInButton.setSize(SignInButton.SIZE_STANDARD);
        signInButton.setScopes(gso.getScopeArray());
    }

    @Override
    public void onStart() {
        super.onStart();

        OptionalPendingResult<GoogleSignInResult> opr = Auth.GoogleSignInApi.silentSignIn(mGoogleApiClient);
        if (opr.isDone()) {
            // If the user's cached credentials are valid, the OptionalPendingResult will be "done"
            // and the GoogleSignInResult will be available instantly.
            Log.d("", "Got cached sign-in");
            GoogleSignInResult result = opr.get();
            handleSignInResult(result);
        } else {
            // If the user has not previously signed in on this device or the sign-in has expired,
            // this asynchronous branch will attempt to sign in the user silently.  Cross-device
            // single sign-on will occur in this branch.
            showProgressDialog();
            opr.setResultCallback(new ResultCallback<GoogleSignInResult>() {
                @Override
                public void onResult(GoogleSignInResult googleSignInResult) {
                    Log.v("Results returned", "Res returned");
                    hideProgressDialog();
                    handleSignInResult(googleSignInResult);
                }
            });
        }
    }


    private void showProgressDialog() {
        if (mProgressDialog == null) {
            mProgressDialog = new ProgressDialog(this);
            mProgressDialog.setMessage("Loading");
            mProgressDialog.setIndeterminate(true);
        }

        mProgressDialog.show();
    }

    // [START revokeAccess]
    private void revokeAccess() {
        Auth.GoogleSignInApi.revokeAccess(mGoogleApiClient).setResultCallback(
                new ResultCallback<Status>() {
                    @Override
                    public void onResult(Status status) {
                        // [START_EXCLUDE]
                        updateUI(false);
                        // [END_EXCLUDE]
                    }
                });
    }
    // [END revokeAccess]

    private void hideProgressDialog() {
        if (mProgressDialog != null && mProgressDialog.isShowing()) {
            mProgressDialog.hide();
        }
    }

    // [START onActivityResult]
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        // Result returned from launching the Intent from GoogleSignInApi.getSignInIntent(...);
        Log.v("Res code", "" + resultCode);
        if (requestCode == RC_SIGN_IN) {
            GoogleSignInResult result = Auth.GoogleSignInApi.getSignInResultFromIntent(data);
            handleSignInResult(result);
        }
    }
    // [END onActivityResult]

    // [START handleSignInResult]
    private void handleSignInResult(GoogleSignInResult result) {
        Log.d("", "handleSignInResult:" + result.isSuccess());
        if (result.isSuccess()) {
            // Signed in successfully, show authenticated UI.
            GoogleSignInAccount acct = result.getSignInAccount();
            String personName = acct.getDisplayName();
            String personEmail = acct.getEmail();
            String personId = acct.getId();
            Uri personPhoto = acct.getPhotoUrl();
            Log.v("Account info", "Name: " + personName + " Email: " + personEmail);
            titleThing.setText(personName + " " + personEmail);
            addUserToDB(personName, personEmail, personId, personPhoto);
//            updateUI(true);
        } else {
            Log.v("Signin", "signin failed");
            // Signed out, show unauthenticated UI.
            updateUI(false);
        }
    }
    // [END handleSignInResult]

    private void updateUI(boolean signedIn) {
        if (signedIn) {
            findViewById(R.id.sign_in_button).setVisibility(View.GONE);
//            findViewById(R.id.sign_out_and_disconnect).setVisibility(View.VISIBLE);
        } else {
            titleThing.setText("Signed out");

            findViewById(R.id.sign_in_button).setVisibility(View.VISIBLE);
//            findViewById(R.id.sign_out_and_disconnect).setVisibility(View.GONE);
        }
    }

    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {
        // An unresolvable error has occurred and Google APIs (including Sign-In) will not
        // be available.
        Log.d("", "onConnectionFailed:" + connectionResult);
    }

    public void signIn()
    {
        Log.v("Signin method", "In signin method");
        Intent signInIntent = Auth.GoogleSignInApi.getSignInIntent(mGoogleApiClient);
        startActivityForResult(signInIntent, RC_SIGN_IN);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.sign_in_button:
                signIn();
                break;
        }
    }

    private void addUserToDB(String personName, String personEmail, String personId, Uri personPhoto)
    {
        dbHelper = new DBHelper(this);
        dbHelper.insertUser(personName, personEmail, "" + personPhoto, "null");
        int size = dbHelper.getAllUsers().getCount();

        volleyGetUser("/get/user", personEmail, personName, "" + personPhoto);
        //Check internet db to see if user exists
            //pull down info if it does
            //Check if they are part of a group
                //if not then show the group login page
                //else download group info

        //Return user to main activity
    }


    public void volleyGetUser(String route, String personEmail, final String personName, final String personPhoto)
    {
//        JSONObject user = new JSONObject();
//        try {
//            user.put("email", "test@test.com");
//
//        } catch (JSONException e) {
//            // TODO Auto-generated catch block
//            e.printStackTrace();
//        }

//        if(user != null) {// user exists
//            Log.v("sigin", " " + user.toString());
            String baseURL = "https://flattingplus.herokuapp.com";
//            RequestQueue queue = Volley.newRequestQueue(this);
//            email = "test@test.com";
            String url = baseURL + route + "?email=" + personEmail;
            final String pEmail = personEmail;
            // Request a string response from the provided URL.

        JsonArrayRequest req = new JsonArrayRequest(url, new Response.Listener<JSONArray> () {
            @Override
            public void onResponse(JSONArray response) {
                try {
//                    VolleyLog.v("Response:%n %s", response.toString(4));
                    Log.v("Response:%n %s", response.toString(4));
                    if(response.length() < 1) //User doesnt exist
                    {
                        Log.v("Signin", "user doesn't exits");
                        //TODO add user to internet db
                        addUserTONetDB(pEmail, personName, personPhoto);


                    }
                    else //User exits so update the local version
                    {
                        JSONArray arr = new JSONArray(response.toString(4));
                        JSONObject jObj = arr.getJSONObject(0);
                        String id = jObj.getString("id");
                        String email = jObj.getString("email");
                        String name = jObj.getString("name");
                        String pic = jObj.getString("pic");
                        String flatgroup = jObj.getString("flatgroup");
                        Log.v(TAG, "id " + id + " email: " + email + " name: " + name + " pic: " + pic + " flatgroup: " + flatgroup);

                        dbHelper.updateUser(name, email, pic, flatgroup);//Update the internal db

                        //Check if they are part of a group
                        if(flatgroup.equals("null")) {
                            Log.v(TAG, "flatgroup is empty");
                            //if not then show the group login page
                            gotg();

                        }
                        else//else download group info
                        {

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
            // Add the request to the RequestQueue.
            queue.add(req);
//        }
//        else //user doesn't exist
//        {
//
//        }
    }

    public void gotg()
    {
        Log.v(TAG, "Starting group signin");
        Intent grouplogin = new Intent(this, GroupLoginReg.class);
        startActivity(grouplogin);
    }

//

    public void addUserTONetDB(String pEmail, String personName, String personPhoto)
    {
        Log.v(TAG, pEmail + " " + personName + " " + personPhoto);
        String baseURL = "https://flattingplus.herokuapp.com";
        String url = baseURL + "/add/user";

         /*Post data*/
        Map<String, String> jsonParams = new HashMap<String, String>();

        jsonParams.put("email", pEmail);
        jsonParams.put("name", personName);
        jsonParams.put("group", "");
        jsonParams.put("pic", personPhoto);

        JSONArray jsonArray = new JSONArray();
        jsonArray.put(jsonParams);

        JsonArrayRequest postRequest = new JsonArrayRequest( Request.Method.PUT, url, jsonArray,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                       try
                       {
                           Log.v(TAG, "add user: " + response.toString(4));
                           gotg();
                       } catch (JSONException e) {
                        e.printStackTrace();
                    }

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
