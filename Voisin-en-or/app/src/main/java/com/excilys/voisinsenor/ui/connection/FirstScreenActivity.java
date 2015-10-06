package com.excilys.voisinsenor.ui.connection;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.excilys.voisinsenor.R;
import com.squareup.okhttp.MediaType;
import com.squareup.okhttp.RequestBody;
import com.squareup.okhttp.Response;

import java.io.IOException;

import okio.BufferedSink;
import retrofit.http.Body;

/**
 * Created by mada on 21/09/15.
 */
public class FirstScreenActivity extends Activity {

    private Button connection;
    private Button inscription;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_firstscreen);
        connection = (Button) findViewById(R.id.activity_firstscreen_my_button_connexion);
        inscription = (Button) findViewById(R.id.activity_firstscreen_my_button_inscription);

        connection.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(FirstScreenActivity.this, ConnectionActivity.class);
                startActivity(i);
            }
        });

        inscription.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(FirstScreenActivity.this, InscriptionActivity.class);
                startActivity(i);
            }
        });

    }


    @Override
    public void onBackPressed() {
        return;
    }
}
