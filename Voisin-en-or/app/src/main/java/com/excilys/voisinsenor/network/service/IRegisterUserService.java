package com.excilys.voisinsenor.network.service;

import com.excilys.voisinsenor.model.Registration;
import com.excilys.voisinsenor.model.User;

import retrofit.Callback;
import retrofit.http.Body;
import retrofit.http.POST;

/**
 * Created by mada on 23/09/15.
 */
public interface IRegisterUserService {

    @POST("/users/save")
    void register(@Body User user, Callback<User> callback);
}
