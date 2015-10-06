package com.excilys.voisinsenor.ui.connection;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import com.excilys.voisinsenor.R;
import com.excilys.voisinsenor.model.User;
import com.excilys.voisinsenor.model.Vehicule;
import com.excilys.voisinsenor.network.event.GetUserEvent;
import com.excilys.voisinsenor.network.service.IRegisterUserService;
import com.excilys.voisinsenor.ui.home.HomeActivity;
import com.excilys.voisinsenor.utils.Internet;

import java.sql.Connection;

import de.greenrobot.event.EventBus;
import retrofit.Callback;
import retrofit.RestAdapter;
import retrofit.RetrofitError;
import retrofit.client.Response;

/**
 * Created by mada on 22/09/15.
 * Send User to API
 * Redirect to Connection
 */
public class InscriptionBisActivity extends Activity {

    private Button valider;
    private Internet internet = null;
    private EditText brand;
    private EditText passangers;
    private EditText matricule;
    private RadioGroup radioDriverGroup = null;
    private RadioButton radioDriverButton = null;
    private User user = null;
    private RestAdapter restAdapter = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_inscription_bis);
        internet = new Internet(this);
        valider = (Button) findViewById(R.id.activity_inscriptionbis_valider);
        brand = (EditText) findViewById(R.id.activity_inscription_vehicule_brand);
        passangers = (EditText) findViewById(R.id.activity_inscription_vehicule_passager);
        matricule = (EditText) findViewById(R.id.activity_inscription_vehicule_matricule);
        radioDriverGroup = (RadioGroup) findViewById(R.id.activity_inscription_radiouGroup_driver);
        restAdapter = new RestAdapter.Builder().setEndpoint(getResources().getString(R.string.server_adress)).build();
        valider.setOnClickListener(validerListener);
        EventBus.getDefault().registerSticky(this);
    }

    private View.OnClickListener validerListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            Vehicule vehicule = new Vehicule();
            // get selected radio button from radioGroup
            int selectedId = radioDriverGroup.getCheckedRadioButtonId();
            // find the radiobutton by returned id
            if (selectedId != -1) {
                radioDriverButton = (RadioButton) findViewById(selectedId);
                String driver = (String) radioDriverButton.getText();

                if (driver.equals("Non")) {
                    user.setVehicule(null);
                } else {
                    vehicule.setBrand(brand.getText().toString());
                    vehicule.setLicencePlate(matricule.getText().toString());
                    vehicule.setPassengerSeats(Integer.parseInt(passangers.getText().toString()));
                    user.setVehicule(vehicule);
                }
            }
            Log.e("MYSELF", user.toString());

            if (internet.internet()) {
                //TODO envoyer les info de l'utilisateur à l'api
                IRegisterUserService service = restAdapter.create(IRegisterUserService.class);
                service.register(user, new Callback<User>() {
                     @Override
                     public void success(User user1, Response response) {
                         Log.e("success INSCRIPTION","succes INSCRIPTION ");
                         Intent i = new Intent(InscriptionBisActivity.this, ConnectionActivity.class);
                         startActivity(i);
                     }

                     @Override
                     public void failure(RetrofitError error) {
                         Log.e("failure","failure");
                     }
                 });
            }
        }
    };

    public void onEventBackgroundThread(GetUserEvent event) {
         user = event.getUser();
    }
}
