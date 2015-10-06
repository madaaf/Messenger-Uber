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
import com.excilys.voisinsenor.R;
import com.excilys.voisinsenor.model.Registration;
import com.excilys.voisinsenor.model.User;
import com.excilys.voisinsenor.network.event.ReceivedAuthentificationEvent;
import com.excilys.voisinsenor.network.service.IAuthentificationService;
import com.excilys.voisinsenor.network.service.IRegistrationService;
import com.excilys.voisinsenor.ui.home.HomeActivity;
import com.excilys.voisinsenor.utils.PopupActivity;

import de.greenrobot.event.EventBus;
import retrofit.Callback;
import retrofit.RestAdapter;
import retrofit.RetrofitError;
import retrofit.client.Response;
import timber.log.Timber;

/**
 * Created by mada on 15/09/15.
 * Send RegId to the API
 * Save email in Pref
 */
public class ConnectionActivity extends Activity {

    private EditText email;
    private Button validate;
    private String emailinput;
    private RestAdapter restAdapter;
    private EventBus bus = EventBus.getDefault();
    private PopupActivity popup;

    @Override
    protected void onResume() {
        super.onResume();
        bus.register(this);
    }

    @Override
    protected void onStop() {
        super.onStop();
        bus.unregister(this);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_connection);
        email = (EditText) findViewById(R.id.activity_connection_email);
        validate = (Button) findViewById(R.id.activity_connection_validate);
        validate.setOnClickListener(ButtonListener);
        popup = new PopupActivity(this);
        restAdapter = new RestAdapter.Builder().setEndpoint(getResources().getString(R.string.server_adress)).build();

    }

    View.OnClickListener ButtonListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            EventBus.getDefault().post(new ReceivedAuthentificationEvent());
        }
    };

    public void onEventBackgroundThread(ReceivedAuthentificationEvent event) {
        emailinput = email.getText().toString();
        Timber.e("email input", emailinput);
        // get user from email
        IAuthentificationService service = restAdapter.create(IAuthentificationService.class);
        User user = service.authentificate(emailinput);
        if(user!=null){
            // save the email in preference
            SharedPreferences sharedPref = getSharedPreferences(getResources().getString(R.string.myPref), Context.MODE_PRIVATE);
            SharedPreferences.Editor editor = sharedPref.edit();
            editor.putString(getResources().getString(R.string.myemail), user.getEmail());
            editor.putString(getResources().getString(R.string.username), user.getFirstName()+" "+user.getName());
            editor.commit();

            final String regid = sharedPref.getString(getResources().getString(R.string.regId), null);

            //send registerId to API
            IRegistrationService registrationConnction = restAdapter.create(IRegistrationService.class);
            Registration r = new Registration(user.getEmail(),regid);
            registrationConnction.register(r, new Callback<Registration>() {
                @Override
                public void success(Registration registration, Response response) {
                    Log.e("succes ", "success connection");
                    Intent i = new Intent(ConnectionActivity.this, HomeActivity.class);
                    startActivity(i);
                }

                @Override
                public void failure(RetrofitError error) {
                    popup.afficherPopup("Une erreur est survenue lors de la connexion", null);
                }
            });

        }else{
            popup.afficherPopup("Votre adresse mail est incorrect", null);
        }

    }

}
