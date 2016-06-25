package com.example.dinoapps.flattingplus.API;

import com.example.dinoapps.flattingplus.Model.FlattingPlusModel;

import retrofit2.Callback;
import retrofit2.http.GET;
import retrofit2.http.Path;

/**
 * Created by james on 25-Jun-16.
 */

public interface FlattingPlusAPI {
    @GET("/users/{user}")      //here is the other url part.best way is to start using /
    public void getFeed(@Path("user") String user, Callback<FlattingPlusModel> response);
    //string user is for passing values from edittext for eg: user=basil2style,google
    //response is the response from the server which is now in the POJO
}
