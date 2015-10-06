package com.excilys.voisinsenor.ui.messenger;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import com.excilys.voisinsenor.R;
import com.excilys.voisinsenor.model.Image;
import com.excilys.voisinsenor.model.User;
import com.excilys.voisinsenor.ui.chat.ChatActivity;
import com.koushikdutta.async.future.FutureCallback;
import com.koushikdutta.ion.Ion;
import com.koushikdutta.urlimageviewhelper.UrlImageViewHelper;

/**
 * Created by mada on 14/09/15.
 */
public class ConversationAdapter extends ArrayAdapter<User> {
   private Context context;

    private static class ViewHolder {
        LinearLayout item;
        TextView name;
        ImageView photo;
    }

    public ConversationAdapter(Context context) {
        super(context, R.layout.item_conversation);
        this.context = context;
    }

    public View getView(int position, View convertView, ViewGroup parent) {
        final ViewHolder viewHolder;
        if (convertView == null) {
            LayoutInflater inflater = LayoutInflater.from(getContext());
            convertView = inflater.inflate(R.layout.item_conversation, parent, false);
            // initalise the view holder
            viewHolder = new ViewHolder();
            viewHolder.item = (LinearLayout) convertView.findViewById(R.id.item_conversation);
            viewHolder.name = (TextView) convertView.findViewById(R.id.item_conversation_username);
            viewHolder.photo = (ImageView) convertView.findViewById(R.id.activity_profil_photo);
            convertView.setTag(viewHolder);
        } else {
            // recycle the already inflated view
            viewHolder = (ViewHolder) convertView.getTag();
        }
        final User user = getItem(position);
        viewHolder.name.setText(user.getFirstName());
        if(user.getPhoto()!=null){
            String url = context.getResources().getString(R.string.server_adress)+"images/download/"+user.getPhoto();
            UrlImageViewHelper.setUrlDrawable(viewHolder.photo,url);
        }else{
            viewHolder.photo.setImageResource(R.drawable.profile_default);
        }
        viewHolder.item.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(context, ChatActivity.class);
                Bundle mBundle = new Bundle();
                mBundle.putString("username", user.getFirstName());
                mBundle.putString("email", user.getEmail());
                mBundle.putString("photo", user.getPhoto());
                i.putExtras(mBundle);
                context.startActivity(i);
            }
        });

        return convertView;
    }

}
