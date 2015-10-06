package com.excilys.voisinsenor.network.service;

import com.excilys.voisinsenor.model.Registration;


import retrofit.Callback;
import retrofit.http.Body;
import retrofit.http.POST;

/**
 * Created by mada on 10/09/15.
 */
public interface IRegistrationService {

    @POST("/registrationId")
    void register(@Body Registration register, Callback<Registration> callback);
}
