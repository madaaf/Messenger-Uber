package com.excilys.voisinsenor.ui.chat;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.excilys.voisinsenor.R;
import com.excilys.voisinsenor.model.Message;
import com.excilys.voisinsenor.network.event.GetMessageEvent;
import com.excilys.voisinsenor.network.service.IConversationService;
import com.excilys.voisinsenor.network.service.IMessageService;

import java.util.ArrayList;
import java.util.List;

import de.greenrobot.event.EventBus;
import retrofit.Callback;
import retrofit.RestAdapter;
import retrofit.RetrofitError;
import retrofit.client.Response;
import timber.log.Timber;

/**
 * Created by mada on 14/09/15.
 */
public class ChatActivity extends Activity {
    private ListView messageslv;
    private ImageView sendButton;
    private EditText messagetosend;
    private TextView myfriend;
    private MessageAdapter messageAdapter;
    private RestAdapter restAdapter;
    private String myfriendEmail;
    private String myfriendPhoto;
    private EventBus bus = EventBus.getDefault();
    private String myemail;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);

        Bundle args = getIntent().getExtras();
        String myfriendName = args.getString("username");
        myfriendEmail = args.getString("email");
        myfriendPhoto = args.getString("photo");
        messageslv = (ListView) findViewById(R.id.activity_chat_list_message);
        sendButton = (ImageView) findViewById(R.id.activity_chat_sendmessage);
        messagetosend = (EditText) findViewById(R.id.activity_chat_edittext);
        myfriend = (TextView) findViewById(R.id.activity_chat_myfriend);

        myfriend.setText(myfriendName);
        messageAdapter = new MessageAdapter(this);
        messageslv.setAdapter(messageAdapter);
        String server = getResources().getString(R.string.server_adress);
        restAdapter = new RestAdapter.Builder().setEndpoint(server).build();

        IConversationService conversationService = restAdapter.create(IConversationService.class);

        SharedPreferences sharedPref = getSharedPreferences(getResources().getString(R.string.myPref), Context.MODE_PRIVATE);
        myemail = sharedPref.getString(getResources().getString(R.string.myemail), null);

        conversationService.getConversation(myfriendEmail, myemail, new Callback<List<Message>>() {
            @Override
            public void success(List<Message> mes, Response response) {
                for(Message message : mes){
                    messageAdapter.add(message);
                }
                messageAdapter.notifyDataSetChanged();
                // scroll down list view
                messageslv.setSelection(messageAdapter.getCount() - 1);
            }

            @Override
            public void failure(RetrofitError error) {
                Timber.e("message failure ");
            }
        });
        sendButton.setOnClickListener(sendMessageListener);
    }


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

    View.OnClickListener sendMessageListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            IMessageService messageService = restAdapter.create(IMessageService.class);
            final Message message = new Message();
            message.setMessage(messagetosend.getText().toString());
            message.setSender(myemail);
            message.setReceiver(myfriendEmail);
            message.setPhoto(myfriendPhoto);
            messageService.sendMessage(message, new Callback<Message>() {
                @Override
                public void success(Message message1, Response response) {
                    Log.e("succes send message", "pl");
                    messageAdapter.add(message);
                    messageAdapter.notifyDataSetChanged();
                    // scroll down list view
                    messageslv.setSelection(messageAdapter.getCount() - 1);
                    //clear edit text
                    messagetosend.setText("");
                }

                @Override
                public void failure(RetrofitError error) {
                    Log.e("failure sens message ", "berk" + error.toString());
                }
            });
        }
    };


    public void onEventMainThread(GetMessageEvent messageEvent) {
        messageAdapter.add(messageEvent.getMessage());
        messageAdapter.notifyDataSetChanged();
        // scroll down list view
        messageslv.setSelection(messageAdapter.getCount() - 1);
    }
}
