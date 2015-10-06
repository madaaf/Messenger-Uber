package com.excilys.voisinsenor.ui.home;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.excilys.voisinsenor.R;
import com.excilys.voisinsenor.model.Track;
import com.excilys.voisinsenor.network.service.ITrackService;

import java.util.List;

import retrofit.Callback;
import retrofit.RestAdapter;
import retrofit.RetrofitError;
import retrofit.client.Response;

/**
 * Created by mada on 28/09/15.
 */
public class TrackFragment extends Fragment{

    private ListView listView;
    private TrackAdapter trackAdapter;
    private RestAdapter restAdapter;

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_trajet, container, false);
        listView = (ListView) view.findViewById(R.id.fragment_trajet_lv);
        trackAdapter = new TrackAdapter(getActivity());
        listView.setAdapter(trackAdapter);
        restAdapter = new RestAdapter.Builder().setEndpoint(getResources().getString(R.string.server_adress)).build();

        ITrackService service = restAdapter.create(ITrackService.class);
        service.getTrack(new Callback<List<Track>>() {
            @Override
            public void success(List<Track> tracks, Response response) {
                trackAdapter.addAll(tracks);
                trackAdapter.notifyDataSetChanged();
                listView.setSelection(trackAdapter.getCount()-1);
            }

            @Override
            public void failure(RetrofitError error) {

            }
        });
        return view;
    }
}
