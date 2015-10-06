package com.excilys.voisinsenor.ui.chat;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.excilys.voisinsenor.R;
import com.excilys.voisinsenor.model.Message;
import com.koushikdutta.urlimageviewhelper.UrlImageViewHelper;

/**
 * Created by mada on 14/09/15.
 */
public class MessageAdapter extends ArrayAdapter<Message>{

    private static class ViewHolder{
        TextView message ;
        ImageView photo;
    }

    public MessageAdapter(Context context) {
        super(context, R.layout.item_chat);
        Log.e("IN Constructure", "ok");
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent){
        ViewHolder viewHolder;
        Log.e("IN GET VIEW","ok");
        if(convertView==null){
            LayoutInflater inflater = LayoutInflater.from(getContext());
            convertView = inflater.inflate(R.layout.item_chat, parent, false);
            // initialize the view holder
            viewHolder = new ViewHolder();
            viewHolder.message = (TextView) convertView.findViewById(R.id.item_chat_message);
            viewHolder.photo = (ImageView) convertView.findViewById(R.id.item_chat_photo);
            convertView.setTag(viewHolder);

        }else{
            // recycle the already inflated view
            viewHolder = (ViewHolder) convertView.getTag();
        }

        // get registration id
        SharedPreferences sharedPrefId = getContext().getSharedPreferences(getContext().getResources().getString(R.string.myPref), Context.MODE_PRIVATE);
        final String myemail = sharedPrefId.getString(getContext().getResources().getString(R.string.myemail), null);

        final Message messageSend = getItem(position);

        if(messageSend.getSender().equals(myemail)){
            RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, (RelativeLayout.LayoutParams.WRAP_CONTENT));
            viewHolder.photo.setVisibility(View.INVISIBLE);
            viewHolder.message.setBackgroundResource(R.drawable.sender);
        params.addRule(RelativeLayout.ALIGN_PARENT_RIGHT,RelativeLayout.TRUE);
        viewHolder.message.setLayoutParams(params);
        }else{
            RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, (RelativeLayout.LayoutParams.WRAP_CONTENT));
            viewHolder.photo.setVisibility(View.VISIBLE);
            viewHolder.message.setBackgroundResource(R.drawable.receiver);
            params.addRule(RelativeLayout.ALIGN_PARENT_LEFT, RelativeLayout.TRUE);
            viewHolder.message.setLayoutParams(params);
            /*
                Here if the personne have no photo, we don't send the default image because we don't have the parameter user.getPhoto()
             */
            String url = getContext().getResources().getString(R.string.server_adress)+"images/download/"+messageSend.getSender();
            UrlImageViewHelper.setUrlDrawable(viewHolder.photo, url);

        }

        Log.e("MESSAGE HJOLDER", messageSend.getMessage());
        viewHolder.message.setText(messageSend.getMessage());
        return convertView;
    }
}
