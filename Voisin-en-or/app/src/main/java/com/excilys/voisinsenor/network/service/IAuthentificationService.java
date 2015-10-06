package com.excilys.voisinsenor.network.service;

import com.excilys.voisinsenor.model.User;

import retrofit.http.GET;
import retrofit.http.Query;

/**
 * Created by mada on 15/09/15.
 */
public interface IAuthentificationService {

    @GET("/users/find")
    User authentificate(@Query("email") String email);
}
