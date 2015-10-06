package com.excilys.voisinsenor.ui.home;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;

import com.excilys.voisinsenor.R;
import com.excilys.voisinsenor.model.User;
import com.excilys.voisinsenor.network.event.GetUserEvent;
import com.excilys.voisinsenor.network.event.ReceivedAuthentificationEvent;
import com.excilys.voisinsenor.network.event.ReceivedUserEvent;
import com.excilys.voisinsenor.network.service.IAuthentificationService;
import com.excilys.voisinsenor.network.service.IDownloadPhotoService;
import com.koushikdutta.urlimageviewhelper.UrlImageViewHelper;

import java.io.IOException;
import java.io.InputStream;

import de.greenrobot.event.EventBus;
import retrofit.Callback;
import retrofit.RestAdapter;
import retrofit.RetrofitError;
import retrofit.client.Response;
import timber.log.Timber;

/**
 * Created by mada on 18/09/15.
 */
public class HomeActivity extends Activity {
    private ImageButton menuButton;
    private View menuFragment;
    private DrawerLayout mDrawerLayout;
    private RestAdapter restAdapter;
    private EventBus bus = EventBus.getDefault();
    private User user;

    private ViewPager viewPager;
    private DetailsPagerAdapter adapter;
    private TabLayout tabLayout;

    @Override
    protected void onStop() {
        super.onStop();
    }

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);



        mDrawerLayout = (DrawerLayout) findViewById(R.id.activity_home_drawer_layout);
        menuButton = (ImageButton) findViewById(R.id.activity_home_menu);
        menuFragment = findViewById(R.id.activity_home_menu_fragment);

        viewPager = (ViewPager) findViewById(R.id.pager);
        tabLayout = (TabLayout) findViewById(R.id.tablayout);

        adapter = new DetailsPagerAdapter(getFragmentManager());
        viewPager.setAdapter(adapter);
        tabLayout.setupWithViewPager(viewPager);

        menuButton.setOnClickListener(menuListener);
        restAdapter = new RestAdapter.Builder().setEndpoint(getResources().getString(R.string.server_adress)).build();
        bus.register(this);
        EventBus.getDefault().post(new ReceivedUserEvent());
    }

    private View.OnClickListener menuListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
        mDrawerLayout.openDrawer(menuFragment);
        }
    };


   // get Info User
    public void onEventBackgroundThread(ReceivedUserEvent event) {
        bus.unregister(this);
        SharedPreferences sharedPref = getSharedPreferences(getResources().getString(R.string.myPref), Context.MODE_PRIVATE);
        String myemail = sharedPref.getString(getResources().getString(R.string.myemail), null);
        IAuthentificationService service = restAdapter.create(IAuthentificationService.class);
        Timber.e("myemail" + myemail);
        user = service.authentificate(myemail);
        if (user != null) {
            GetUserEvent us = new GetUserEvent(user);
            EventBus.getDefault().postSticky(us);
        } else {
            Log.e("myemail", myemail);
        }

    }


    @Override
    public void onBackPressed() {
        return;
    }
}
