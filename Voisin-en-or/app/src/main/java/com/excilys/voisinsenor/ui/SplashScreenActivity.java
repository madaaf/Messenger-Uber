package com.excilys.voisinsenor.ui;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;

import com.excilys.voisinsenor.R;
import com.excilys.voisinsenor.model.User;
import com.excilys.voisinsenor.network.event.GetPoisEvent;
import com.excilys.voisinsenor.network.event.GetUserEvent;
import com.excilys.voisinsenor.network.event.ReceivedAuthentificationEvent;
import com.excilys.voisinsenor.network.event.ReceivedMainApplication;
import com.excilys.voisinsenor.network.service.IAuthentificationService;
import com.excilys.voisinsenor.ui.home.HomeActivity;
import com.excilys.voisinsenor.ui.messenger.ConversationsActivity;

import de.greenrobot.event.EventBus;
import retrofit.RestAdapter;

/**
 * Created by mada on 09/09/15.
 */
public class SplashScreenActivity extends Activity  {
    private RestAdapter restAdapter;
    private User user;
    private String myemail;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EventBus.getDefault().registerSticky(this);
        restAdapter = new RestAdapter.Builder().setEndpoint(getResources().getString(R.string.server_adress)).build();
        SharedPreferences sharedPref = getSharedPreferences(getResources().getString(R.string.myPref), Context.MODE_PRIVATE);
        myemail = sharedPref.getString(getResources().getString(R.string.myemail),null);
        // lane le bacjrgound dans MainApplication
        EventBus.getDefault().post(new ReceivedMainApplication());
        EventBus.getDefault().post(new ReceivedAuthentificationEvent());
    }

    public void onEventBackgroundThread(ReceivedAuthentificationEvent event) {
        IAuthentificationService service = restAdapter.create(IAuthentificationService.class);
        user = service.authentificate(myemail);
        GetUserEvent us = new GetUserEvent(user);
        EventBus.getDefault().postSticky(us);
    }

    public void onEventMainThread(GetPoisEvent event){
        LocationManager lm = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        Criteria c = new Criteria();
        String provider = lm.getBestProvider(c, false);
        //now you have best provider
        //get location
        Location l = lm.getLastKnownLocation(provider);

        double longitude = l.getLongitude();
        double latitude = l.getLatitude();
        // save location in preference
        SharedPreferences sharedPref = getSharedPreferences(getResources().getString(R.string.myemail), Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putFloat(getResources().getString(R.string.longitude), (float) longitude);
        editor.putFloat(getResources().getString(R.string.myemail), (float) latitude);
        editor.commit();


        Intent i = new Intent(SplashScreenActivity.this, HomeActivity.class);
        startActivity(i);

       /* if (l != null) {
            //get latitude and longitude of the location
            double longitude = l.getLongitude();
            double latitude = l.getLatitude();
            Intent i = new Intent(SplashScreenActivity.this, MapActivity.class);
            Bundle mBundle = new Bundle();
            mBundle.putDouble("longitude", longitude);
            mBundle.putDouble("latitude", latitude);
            i.putExtras(mBundle);
            startActivity(i);
        } else {
            Intent i = new Intent(SplashScreenActivity.this, MapActivity.class);
            Bundle mBundle = new Bundle();
            mBundle.putDouble("longitude", 2.3488000d);
            mBundle.putDouble("latitude", 48.8534100d);
            i.putExtras(mBundle);
            startActivity(i);
            System.out.println("probleme");
        }*/
    }

}
