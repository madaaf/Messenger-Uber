package com.excilys.voisinsenor.ui.messenger;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ListView;

import com.excilys.voisinsenor.R;
import com.excilys.voisinsenor.model.User;
import com.excilys.voisinsenor.network.event.ReceivedUsersEvent;
import com.excilys.voisinsenor.network.service.IUsersService;
import com.excilys.voisinsenor.ui.home.HomeActivity;

import java.util.ArrayList;
import java.util.List;

import de.greenrobot.event.EventBus;
import retrofit.RestAdapter;

/**
 * Created by mada on 14/09/15.
 */
public class ConversationsActivity extends Activity {
    private ListView conversationslv;
    private ConversationAdapter conversationAdapter;
    private RestAdapter restAdapter;
    private List<User> users;
    private ImageButton returnButton;
    private EventBus bus = EventBus.getDefault();
    private String myemail;

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onStop() {
        super.onStop();
        bus.unregister(this);
    }

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_conversations);

        SharedPreferences sharedPref = getSharedPreferences(getResources().getString(R.string.myPref), Context.MODE_PRIVATE);
        myemail = sharedPref.getString(getResources().getString(R.string.myemail), null);
        users = new ArrayList<>();
        conversationslv = (ListView) findViewById(R.id.activity_conversations_list);
        returnButton = (ImageButton) findViewById(R.id.activity_return);
        conversationAdapter = new ConversationAdapter(this);
        conversationslv.setAdapter(conversationAdapter);
        restAdapter = new RestAdapter.Builder().setEndpoint(getResources().getString(R.string.server_adress)).build();
        returnButton.setOnClickListener(returnListener);
        bus.register(this);
        EventBus.getDefault().post(new ReceivedUsersEvent());

    }

    public void onEventBackgroundThread(ReceivedUsersEvent event) {
        IUsersService usersService = restAdapter.create(IUsersService.class);
        //users = usersService.getUsers();
        for (User user : usersService.getUsers()) {
            if (!user.getEmail().equals(myemail)) {
                users.add(user);
            }
        }
        runThread();
    }

    private void runThread() {
        new Thread() {
            public void run() {
                try {
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Log.e("users", users.toString());
                            conversationAdapter.clear();
                            conversationAdapter.addAll(users);
                            conversationAdapter.notifyDataSetChanged();
                        }
                    });
                    Thread.sleep(300);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }.start();
    }


    private View.OnClickListener returnListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            Intent i = new Intent(v.getContext(), HomeActivity.class);
            startActivity(i);
        }
    };

}