package com.excilys.voisinsenor.ui.profile;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.excilys.voisinsenor.R;
import com.excilys.voisinsenor.model.User;
import com.excilys.voisinsenor.network.event.GetUserEvent;
import com.excilys.voisinsenor.network.event.ReceivedPhotoEvent;
import com.excilys.voisinsenor.network.service.IDownloadPhotoService;
import com.excilys.voisinsenor.ui.home.HomeActivity;
import com.excilys.voisinsenor.utils.CameraActivity;
import com.excilys.voisinsenor.utils.Internet;
import com.koushikdutta.urlimageviewhelper.UrlImageViewHelper;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;

import de.greenrobot.event.EventBus;
import retrofit.RestAdapter;
import retrofit.client.Response;
import retrofit.http.Body;
import timber.log.Timber;

/**
 * Created by mada on 21/09/15.
 */
public class ProfilActivity extends Activity {

    private ImageButton returnButton;
    private ImageView selfie;
    private TextView lastName;
    private TextView firstName;
    private TextView phone;
    private TextView email;
    private TextView brand;
    private TextView seats;
    private TextView matricule;
    private RestAdapter restAdapter;
    private EventBus bus = EventBus.getDefault();
    private Internet internet;


    @Override
    protected void onDestroy() {
        super.onDestroy();

    }
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profil);
        returnButton = (ImageButton) findViewById(R.id.activity_return);
        lastName = (TextView) findViewById(R.id.activity_profil_lastname);
        firstName = (TextView) findViewById(R.id.activity_profil_firstname);
        phone = (TextView) findViewById(R.id.activity_profil_phone);
        email = (TextView) findViewById(R.id.activity_profil_email);
        brand = (TextView) findViewById(R.id.activity_profil_car_brand);
        seats = (TextView) findViewById(R.id.activity_profil_nbrseats);
        matricule = (TextView) findViewById(R.id.activity_profil_car_matricule);
        selfie = (ImageView) findViewById(R.id.activity_profil_photo);
        returnButton.setOnClickListener(returnListener);
        internet = new Internet(this);
        restAdapter = new RestAdapter.Builder().setEndpoint(getResources().getString(R.string.server_adress)).build();
        selfie.setOnClickListener(photoSelfieListener);
        bus.registerSticky(this);
    }


    public void onEventMainThread(GetUserEvent event) {
        bus.unregister(this);
        User user = event.getUser();
        Timber.e("USer" + user.toString());
        lastName.setText(user.getName());
        firstName.setText(user.getFirstName());
        email.setText(user.getEmail());
        phone.setText(user.getPhoneNumber());
       // selfie.setImageBitmap(BitmapFactory.decodeStream(user.getImage()));

        String path = getResources().getString(R.string.sdcard_selfie);
        File fichierPhoto = new File(path);
        if(fichierPhoto.length()==0) {
            fichierPhoto.delete();
        }
        // s'il existe dans mes fichier je récupere l'image, sinon je vais chercher dans l'API
        if(fichierPhoto.length()!=0) {
            Bitmap bmp = decodeSampledBitmapFromFile(path, 500, 300);
            Bitmap temp = Bitmap.createBitmap(bmp.getWidth(), bmp.getHeight(), Bitmap.Config.RGB_565);
            Canvas c = new Canvas(temp);
            c.drawBitmap(bmp, 0, 0, null);
            selfie.setImageBitmap(temp);
        }else{
            if(internet.internet()){
                // si la photo existe dans l'api
                if(user.getPhoto()!=null){
                    String url = getResources().getString(R.string.server_adress)+"images/download/"+user.getPhoto();
                    UrlImageViewHelper.setUrlDrawable(selfie, url);
                }else{
                    selfie.setImageResource(R.drawable.profile_default);
                }
            }else{
                selfie.setImageResource(R.drawable.profile_default);
            }
        }
        //selfie.setImageResource(R.drawable.);
        if(user.getVehicule() != null){
            brand.setText(user.getVehicule().getBrand());
            seats.setText(Integer.toString(user.getVehicule().getPassengerSeats()));
        }
    }

    private View.OnClickListener returnListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            Intent i = new Intent(v.getContext(), HomeActivity.class);
            startActivity(i);
        }
    };

    View.OnClickListener photoSelfieListener = new View.OnClickListener(){

        @Override
        public void onClick(View v) {
            Intent i  = new Intent(ProfilActivity.this,CameraActivity.class);
            startActivity(i);
            finish();
        }
    };


    private Bitmap decodeSampledBitmapFromFile(String path, int reqWidth, int reqHeight) {
        Log.d("Camera ", "decodeSampledBitmapFromFile class Camera");
        final BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeFile(path, options);

        final int height = options.outHeight;
        final int width = options.outWidth;
        options.inPreferredConfig = Bitmap.Config.RGB_565;
        int inSampleSize = 1;

        if (height > reqHeight) {
            inSampleSize = Math.round((float)height / (float)reqHeight);
        }

        int expectedWidth = width / inSampleSize;

        if (expectedWidth > reqWidth) {
            inSampleSize = Math.round((float)width / (float)reqWidth);
        }

        options.inSampleSize = inSampleSize;
        options.inJustDecodeBounds = false;

        return BitmapFactory.decodeFile(path, options);
    }

}
