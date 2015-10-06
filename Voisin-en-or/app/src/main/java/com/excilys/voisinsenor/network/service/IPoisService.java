package com.excilys.voisinsenor.network.service;

import com.excilys.voisinsenor.model.POI;
import java.util.List;
import retrofit.http.GET;

/**
 * Created by mada on 14/09/15.
 */
public interface IPoisService {

    @GET("/poi")
    List<POI> getPois();
}
