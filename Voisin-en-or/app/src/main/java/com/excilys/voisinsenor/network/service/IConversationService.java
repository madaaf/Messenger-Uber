package com.excilys.voisinsenor.network.service;

import com.excilys.voisinsenor.model.Message;

import java.util.List;

import retrofit.Callback;
import retrofit.http.GET;
import retrofit.http.Query;

/**
 * Created by mada on 25/09/15.
 */
public interface IConversationService {

    @GET("/message/conversation")
    void getConversation(@Query("email1") String email1,@Query("email2") String email2, Callback<List<Message>> callback);
}
