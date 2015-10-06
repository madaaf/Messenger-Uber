package com.excilys.voisinsenor.network.service;

import com.excilys.voisinsenor.model.gouvdata.GeoCode;

import retrofit.http.GET;
import retrofit.http.Query;

/**
 * Created by mada on 10/09/15.
 */
public interface IAddressService {

    @GET("/search")
    GeoCode search(@Query("q") String address, @Query("limit") int limit);

}
