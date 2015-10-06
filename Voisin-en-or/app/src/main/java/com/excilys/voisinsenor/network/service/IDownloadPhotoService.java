package com.excilys.voisinsenor.network.service;

import retrofit.Callback;
import retrofit.http.GET;
import retrofit.http.Path;

/**
 * Created by mada on 24/09/15.
 * Cette classe ne sert à rien pour l'instant, je la laisse au cas ou
 * On utilise la libraire URLDrawable à la place
 */
public interface IDownloadPhotoService {

    @GET("/images/download/{file}")
    void download(@Path("file") String fileName, Callback<String> callback);
}
