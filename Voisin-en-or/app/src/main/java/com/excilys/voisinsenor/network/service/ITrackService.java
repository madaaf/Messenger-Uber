package com.excilys.voisinsenor.network.service;

import com.excilys.voisinsenor.model.Track;


import java.util.List;

import retrofit.Callback;
import retrofit.http.Body;
import retrofit.http.GET;
import retrofit.http.POST;

/**
 * Created by mada on 28/09/15.
 */
public interface ITrackService {

    @POST("/track")
    void postTrack(@Body Track track, Callback<String> callback);

    @GET("/track")
    void getTrack(Callback<List<Track>> callback);
}
