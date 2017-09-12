package com.example.renatogallis.fixcard.API;

import com.example.renatogallis.fixcard.Locals.Location;
import com.example.renatogallis.fixcard.Locals.Locals;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

/**
 * Created by Renato Gallis on 28/08/2017.
 */

public interface LocalAPI {
    //@GET("/maps/api/place/nearbysearch/json?location=-23.5062594,-46.5049528&radius=50000&type=library&keyword=cruise&key=AIzaSyDEQS0L1OpK6vsCrAmFA38kooLg5bXxpL8")
    @GET("api/place/nearbysearch/json?sensor=true&key=AIzaSyDEQS0L1OpK6vsCrAmFA38kooLg5bXxpL8")
    Call<Locals> getDadosLocal(@Query("type") String type, @Query("location") String location, @Query("radius") int radius);

}
