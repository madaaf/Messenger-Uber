package com.excilys.voisinsenor.network.service;


import retrofit.Callback;
import retrofit.http.Multipart;
import retrofit.http.POST;
import retrofit.http.Part;
import retrofit.http.Query;
import retrofit.mime.TypedFile;

/**
 * Created by mada on 24/09/15.
 */
public interface IUploadPhotoService {

    @POST("/images/upload")
    @Multipart
    void upload(@Part("file") TypedFile image,@Query("email") String email, Callback<String> callback);
}
