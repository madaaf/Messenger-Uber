package com.excilys.voisinsenor.ui.connection;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import com.excilys.voisinsenor.R;
import com.excilys.voisinsenor.ui.home.HomeActivity;

;

/**
 * Created by mada on 24/09/15.
 * Premiere screen a se lancer dans l'application
 * 1° check first connection
 * 2° get user from email if is an old user and redirect to the home
 */
public class LoadingActivity extends Activity {

    private String myemail;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_loading);
        SharedPreferences sharedPref = getSharedPreferences(getResources().getString(R.string.myPref), Context.MODE_PRIVATE);
        myemail = sharedPref.getString(getResources().getString(R.string.myemail), null);
        // ancien utilisateur
        if (myemail != null) {
            Intent i = new Intent(LoadingActivity.this, HomeActivity.class);
            startActivity(i);
            // Nouvel  utilisateur
        } else {
            Intent i = new Intent(LoadingActivity.this, FirstScreenActivity.class);
            startActivity(i);
        }
    }


    @Override
    public void onBackPressed() {
        return;
    }
}
