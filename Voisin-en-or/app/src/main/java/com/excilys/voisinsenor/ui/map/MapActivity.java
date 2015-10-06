package com.excilys.voisinsenor.ui.map;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.SearchView;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.ImageButton;
import android.widget.ListView;

import com.excilys.voisinsenor.R;
import com.excilys.voisinsenor.model.Address;
import com.excilys.voisinsenor.model.gouvdata.Features;
import com.excilys.voisinsenor.model.gouvdata.GeoCode;
import com.excilys.voisinsenor.network.event.GetAddressEvent;
import com.excilys.voisinsenor.network.event.GetCoordAddressEvent;
import com.excilys.voisinsenor.network.event.GetListAddressEvent;
import com.excilys.voisinsenor.network.service.IAddressService;
import com.excilys.voisinsenor.ui.home.HomeActivity;

import java.util.ArrayList;
import java.util.List;

import de.greenrobot.event.EventBus;
import retrofit.RestAdapter;

public class MapActivity extends Activity {


    private MapFragment mapFragment;
    private SearchView searchView;
    private ListView resultsView;
    private IAddressService addressService;
    private AddressAdapter addressAdapter;
    private ImageButton returnButton;
    private GeoCode geo;
    private EventBus bus = EventBus.getDefault();

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
        setContentView(R.layout.activity_map);

        mapFragment = new MapFragment();
        mapFragment.setArguments(getIntent().getExtras());
        getFragmentManager()
                .beginTransaction()
                .add(R.id.map_container, mapFragment)
                .commit();

        searchView = (SearchView) findViewById(R.id.map_search);
        resultsView = (ListView) findViewById(R.id.map_results_list);
        returnButton = (ImageButton) findViewById(R.id.activity_return);
        addressAdapter = new AddressAdapter(this);
        resultsView.setAdapter(addressAdapter);

        returnButton.setOnClickListener(returnListener);
        // creation of the rest object
        RestAdapter restAdapter = new RestAdapter.Builder()
                .setEndpoint("http://api-adresse.data.gouv.fr/")
                .build();
        addressService = restAdapter.create(IAddressService.class);


        searchView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                resultsView.setVisibility(View.VISIBLE);
            }
        });

        searchView.setOnCloseListener(new SearchView.OnCloseListener() {
            @Override
            public boolean onClose() {
                refreshResults(null, View.INVISIBLE);
                return false;
            }
        });

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                System.out.println(newText);
                if (newText.isEmpty()) {
                    refreshResults(null, View.INVISIBLE);
                    return false;
                }
                EventBus.getDefault().post(new GetAddressEvent(newText));
                return true;
            }
        });

    }


    private void refreshResults(List<Address> addresses, int visibility) {
        addressAdapter.clear();
        if (addresses != null) {
            addressAdapter.addAll(addresses);
        }
        addressAdapter.notifyDataSetChanged();
        resultsView.setVisibility(visibility);
    }


    public void onEventMainThread(GetCoordAddressEvent coordAddressEvent) {
        //cache la liste
        refreshResults(null, View.INVISIBLE);
        mapFragment.runCenterMapAnim(coordAddressEvent.getLat(), coordAddressEvent.getLgn());
        //ferme le clavier
        InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(searchView.getWindowToken(), 0);
    }

    public void onEventMainThread(GetListAddressEvent listAddressEvent) {
        refreshResults(listAddressEvent.getAddresses(), View.VISIBLE);
    }

    public void onEventBackgroundThread(GetAddressEvent addressEvent) {
        List<Address> addresses = new ArrayList<>();
        System.out.println(addressEvent.getAddress());
        GeoCode geo = addressService.search(addressEvent.getAddress(), 5);
        for(Features f : geo.getFeatures()) {
            // Coordinates has longitude at index 0, latitude at index 1
            addresses.add(new Address(f.getProperties().getAddress(),
                    f.getGeometry().getCoordinates().get(1),
                    f.getGeometry().getCoordinates().get(0)));
        }
        EventBus.getDefault().post(new GetListAddressEvent(addresses));
    }

    public View.OnClickListener returnListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            Intent i = new Intent(v.getContext(), HomeActivity.class);
            startActivity(i);
        }
    };

}
