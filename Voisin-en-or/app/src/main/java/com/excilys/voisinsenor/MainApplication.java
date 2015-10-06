package com.excilys.voisinsenor;

import android.app.Application;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationManager;
import android.os.AsyncTask;
import android.util.Log;
import com.excilys.voisinsenor.gcm.RegistrationIntentService;
import com.excilys.voisinsenor.model.POI;
import com.excilys.voisinsenor.network.event.GetPoisEvent;
import com.excilys.voisinsenor.network.service.IPoisService;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GoogleApiAvailability;
import com.google.android.gms.gcm.GoogleCloudMessaging;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import de.greenrobot.event.EventBus;
import retrofit.RestAdapter;
import timber.log.Timber;

/**
 * Created by mada on 09/09/15.
 */

/**
 * GOALS :
 *  1° Put RegId in Ref
 *  2° Put location in Ref
 *  3° Get POIS
 *  4° Check first connection
 */

public class MainApplication extends Application {

    private static Context context;
    private static String SENDER_ID;
    private RestAdapter restAdapter;
    private String regid;
    private List<POI> POIs;
    private GoogleCloudMessaging gcm;

    @Override
    public void onCreate() {
        super.onCreate();
        context = this;
        SENDER_ID = getResources().getString(R.string.sender_id);
        restAdapter = new RestAdapter.Builder().setEndpoint(getResources().getString(R.string.server_adress)).build();
        POIs = new ArrayList<>();
        // Start IntentService to register this application with GCM.
        if (checkPlayServices()) {
            Intent intent = new Intent(this, RegistrationIntentService.class);
            startService(intent);
            new Register().execute();
        }

        Timber.plant(new Timber.DebugTree());
    }


    private class Register extends AsyncTask<String, String, String> {

        @Override
        protected String doInBackground(String... args) {
            try {
                // Initialise preference
                SharedPreferences sharedPref = getSharedPreferences(getResources().getString(R.string.myPref), Context.MODE_PRIVATE);
                SharedPreferences.Editor editor = sharedPref.edit();

                if (gcm == null) {
                    gcm = GoogleCloudMessaging.getInstance(context);
                    regid = gcm.register(SENDER_ID);
                    Timber.d("RegIdN ", regid);
                    // save the RegId in preference
                    editor.putString(getResources().getString(R.string.regId), regid);
                    editor.commit();
                }
                // save location in preference
                LocationManager lm = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
                Criteria c = new Criteria();
                String provider = lm.getBestProvider(c, false);
                Location l = lm.getLastKnownLocation(provider);
                if(l!=null){
                    double latitude = l.getLatitude();
                    double longitude = l.getLongitude();
                    editor.putFloat(getResources().getString(R.string.longitude), (float) longitude);
                    editor.putFloat(getResources().getString(R.string.latitude), (float) latitude);
                    editor.commit();
                }

                // get POIS
                IPoisService poisService = restAdapter.create(IPoisService.class);
                POIs = poisService.getPois();
                EventBus.getDefault().postSticky(new GetPoisEvent(POIs));
                return null;

            } catch (IOException ex) {
                Log.e("Error", ex.getMessage());
                return "Fails";
            }
        }
    }
    /**
     * Check the device to make sure it has the Google Play Services APK. If
     * it doesn't, display a dialog that allows users to download the APK from
     * the Google Play Store or enable it in the device's system settings.
     */
    private boolean checkPlayServices() {
        GoogleApiAvailability apiAvailability = GoogleApiAvailability.getInstance();
        int resultCode = apiAvailability.isGooglePlayServicesAvailable(this);
        if (resultCode != ConnectionResult.SUCCESS) {
            if (apiAvailability.isUserResolvableError(resultCode)) {
                //apiAvailability.getErrorDialog(this, resultCode, PLAY_SERVICES_RESOLUTION_REQUEST).show();
            } else {
                // Log.i(TAG, "This device is not supported.");
                //context.finish();
            }
            return false;
        }
        return true;
    }
}
