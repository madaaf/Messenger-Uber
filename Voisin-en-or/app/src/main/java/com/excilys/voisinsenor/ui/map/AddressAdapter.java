package com.excilys.voisinsenor.ui.map;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.excilys.voisinsenor.R;
import com.excilys.voisinsenor.model.Address;
import com.excilys.voisinsenor.network.event.GetCoordAddressEvent;

import de.greenrobot.event.EventBus;

/**
 * Created by mada on 10/09/15.
 */
public class AddressAdapter extends ArrayAdapter<Address> {

    public AddressAdapter(Context context) {
        super(context,R.layout.item_map_address);
    }

    private static class ViewHolder {
        TextView text;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder;
        if (convertView == null) {
            LayoutInflater inflater = LayoutInflater.from(getContext());
            convertView = inflater.inflate(R.layout.item_map_address, parent, false);
            // initialize the view holder
            viewHolder = new ViewHolder();
            viewHolder.text = (TextView) convertView.findViewById(R.id.result_text);

            convertView.setTag(viewHolder);
        } else {
            // recycle the already inflated view
            viewHolder = (ViewHolder) convertView.getTag();
        }
        final Address address = getItem(position);

        viewHolder.text.setText(address.getAddress());

        viewHolder.text.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EventBus.getDefault().post(new GetCoordAddressEvent(address.getLat(), address.getLng()));
            }
        });
        return convertView;
    }

}
