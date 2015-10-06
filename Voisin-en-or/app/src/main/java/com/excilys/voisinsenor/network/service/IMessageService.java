package com.excilys.voisinsenor.network.service;

import com.excilys.voisinsenor.model.Message;
import com.excilys.voisinsenor.model.Registration;

import retrofit.Callback;
import retrofit.http.Body;
import retrofit.http.POST;

/**
 * Created by mada on 14/09/15.
 */

public interface IMessageService {

    @POST("/message/send")
    void sendMessage(@Body Message message, Callback<Message> callback);
}
