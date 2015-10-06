package com.excilys.voisinsenor.ui.home;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.excilys.voisinsenor.R;
import com.excilys.voisinsenor.model.Image;
import com.excilys.voisinsenor.model.Message;
import com.excilys.voisinsenor.model.POI;
import com.excilys.voisinsenor.model.Track;
import com.koushikdutta.urlimageviewhelper.UrlImageViewHelper;

import java.util.List;

/**
 * Created by mada on 29/09/15.
 */
public class TrackAdapter extends ArrayAdapter<Track>{

    private Context context;

    private static class ViewHolder{
        ImageView photo;
        TextView date;
        TextView username;
        TextView departure;
        TextView destination;

    }
    public TrackAdapter(Context context) {
        super(context, R.layout.item_track);
        this.context = context;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent){
        ViewHolder viewHolder;
        if(convertView==null){
            LayoutInflater inflater = LayoutInflater.from(getContext());
            convertView = inflater.inflate(R.layout.item_track,parent,false);
            viewHolder = new ViewHolder();
            viewHolder.departure = (TextView) convertView.findViewById(R.id.item_track_departure);
            viewHolder.destination = (TextView) convertView.findViewById(R.id.item_track_destination);
            viewHolder.username = (TextView) convertView.findViewById(R.id.item_track_username);
            viewHolder.photo = (ImageView) convertView.findViewById(R.id.item_track_photo);

            convertView.setTag(viewHolder);
        }else{
            viewHolder = (ViewHolder) convertView.getTag();
        }
        final Track item = getItem(position);
        List<POI> POIS = item.getWaypoints();
        viewHolder.departure.setText(POIS.get(0).getAddress());
        viewHolder.destination.setText(POIS.get(POIS.size() - 1).getAddress());
        if(item.getUserEmail()!=null){
            viewHolder.username.setText(item.getUserName());
        }
        String url = getContext().getResources().getString(R.string.server_adress)+"images/download/"+item.getUserEmail();
        UrlImageViewHelper.setUrlDrawable(viewHolder.photo, url);
        return convertView;
    }
}
