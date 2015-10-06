package com.excilys.voisinsenor.ui.map;

import android.animation.AnimatorSet;
import android.animation.ValueAnimator;
import android.app.Fragment;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.DecelerateInterpolator;
import android.widget.ImageView;

import com.excilys.voisinsenor.R;
import com.excilys.voisinsenor.model.POI;
import com.excilys.voisinsenor.network.event.GetPoisEvent;
import com.excilys.voisinsenor.ui.trajet.TrajetActivity;
import com.mapbox.mapboxsdk.api.ILatLng;
import com.mapbox.mapboxsdk.geometry.BoundingBox;
import com.mapbox.mapboxsdk.geometry.LatLng;
import com.mapbox.mapboxsdk.overlay.Icon;
import com.mapbox.mapboxsdk.overlay.Marker;
import com.mapbox.mapboxsdk.overlay.PathOverlay;
import com.mapbox.mapboxsdk.tileprovider.tilesource.MapboxTileLayer;
import com.mapbox.mapboxsdk.views.MapView;
import com.mapbox.mapboxsdk.views.MapViewListener;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import de.greenrobot.event.EventBus;
import timber.log.Timber;

/**
 * Created by mada on 09/09/15.
 */
public class MapFragment extends Fragment {
    static final String TAG = "CardFrontFragment";
    final static String COORDINATES_KEY = "coordinates";

    private MapView mv;
    private List<POI> POIs;
    private List<POI> waypoints;
    private List<Marker> markerList;
    private ImageView reset;
    private ImageView ok;
    private ImageView mylocButton;
    private List<PathOverlay> lines;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        lines = new ArrayList<>();
        View v = inflater.inflate(R.layout.fragment_map, container, false);
        markerList = new ArrayList<>();
        waypoints = new ArrayList<>();
        initMap(v);
        GetPoisEvent pois = EventBus.getDefault().getStickyEvent(GetPoisEvent.class);
        if (pois != null) {
            POIs = pois.getPOIs();
            if (!POIs.isEmpty()) {
                updateCoordinates();
            }
        }
        /*----------------------------------*/
        /*-------Map center listener--------*/
        /*----------------------------------*/

        mylocButton = (ImageView) v.findViewById(R.id.mylocButton);
        reset = (ImageView) v.findViewById(R.id.fragment_map_reset);
        ok = (ImageView) v.findViewById(R.id.fragment_map_ok);

        ok.setOnClickListener(okListener);
        reset.setOnClickListener(resetListener);

        mylocButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                LatLng userLocation = mv.getUserLocation();
                if (userLocation != null) {
                    runCenterMapAnim(userLocation.getLatitude(), userLocation.getLongitude());
                }
            }
        });
        return v;
    }

    private void initMap(View v) {
        mv = (MapView) v.findViewById(R.id.mapview);
        mv.setAccessToken(getResources().getString(R.string.pk_accessToken));
        mv.setTileSource(new MapboxTileLayer(getResources().getString(R.string.mapID)));

        SharedPreferences sharedPref = getActivity().getSharedPreferences(getResources().getString(R.string.myPref), Context.MODE_PRIVATE);
        float latitude = sharedPref.getFloat(getResources().getString(R.string.latitude), (float) 48.8534100);
        float longitude = sharedPref.getFloat(getResources().getString(R.string.longitude), (float) 2.3488000);

        System.out.println(latitude + " " + longitude);
        mv.setCenter(new LatLng(latitude, longitude)); // Paris coordinates
        // Zoom € [0., 22.]
        //mv.setZoom(6.5f);
        mv.setZoom(15f);
        mv.setUserLocationEnabled(true);

    }

    public void updateCoordinates() {
        if (mv == null) {
            return;
        }
        // Creating shop location marker
        for (POI poi : POIs) {
            Marker marker = new Marker(poi.getName(), poi.getAddress(), new LatLng(poi.getLat(), poi.getLng()));
            marker.setIcon(new Icon(new BitmapDrawable(getActivity().getResources(),
                    BitmapFactory.decodeResource(getActivity().getResources(), R.drawable.mapmarkerinactive))));
            mv.addMarker(marker);
            mv.setMapViewListener(mapViewListener);
        }
    }

    private View.OnClickListener okListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            for (Marker marker1 : markerList) {
                Timber.e("marker " + marker1.getDescription() + " " + marker1.getTitle());
                LatLng lg = marker1.getPoint();
                POI poi = new POI(marker1.getTitle(),marker1.getDescription(),lg.getLatitude(),lg.getLatitude());
                waypoints.add(poi);
            }
            Intent i = new Intent(getActivity(), TrajetActivity.class);
            Bundle bundle = new Bundle();
            bundle.putSerializable("waypoints", (Serializable) waypoints);
            i.putExtras(bundle);
            startActivity(i);
        }
    };

    private View.OnClickListener resetListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            for (Marker marker1 : markerList) {
                marker1.setIcon(new Icon(new BitmapDrawable(getActivity().getResources(),
                        BitmapFactory.decodeResource(getActivity().getResources(), R.drawable.mapmarkerinactive))));
            }
            for(PathOverlay line : lines){
                line.removeAllPoints();
            }

            markerList.clear();
            waypoints.clear();
            //line = new PathOverlay(Color.BLACK, 3);

        }
    };

    private MapViewListener mapViewListener = new MapViewListener() {

        @Override
        public void onShowMarker(MapView mapView, Marker marker) {
           PathOverlay line = new PathOverlay(Color.BLACK, 3);
            markerList.add(marker);
            for (Marker marker1 : markerList) {
                marker1.setIcon(new Icon(new BitmapDrawable(getActivity().getResources(),
                        BitmapFactory.decodeResource(getActivity().getResources(), R.drawable.mapmarkerorange))));
                line.addPoint(new LatLng(marker1.getPoint().getLatitude(), marker1.getPoint().getLongitude()));
            }
            markerList.get(markerList.size() - 1).setIcon(new Icon(new BitmapDrawable(getActivity().getResources(),
                    BitmapFactory.decodeResource(getActivity().getResources(), R.drawable.mapmarkerred))));
            markerList.get(0).setIcon(new Icon(new BitmapDrawable(getActivity().getResources(),
                    BitmapFactory.decodeResource(getActivity().getResources(), R.drawable.mapmarkergreen))));

            mapView.getOverlays().add(line);
            lines.add(line);

        }

        @Override
        public void onHideMarker(MapView mapView, Marker marker) {

        }

        @Override
        public void onTapMarker(MapView mapView, Marker marker) {

        }

        @Override
        public void onLongPressMarker(MapView mapView, Marker marker) {

        }

        @Override
        public void onTapMap(MapView mapView, ILatLng iLatLng) {

        }

        @Override
        public void onLongPressMap(MapView mapView, ILatLng iLatLng) {

        }
    };

    /**
     * Ce lance en dernier( OnCreate, OnStart, onResume)
     */
    @Override
    public void onResume() {
        super.onResume();

    }

    /**
     * Fluid animation for map centering.
     */
    public void runCenterMapAnim(double lat, double lng) {
        final float latInit = (float) mv.getCenter().getLatitude();
        float lngInit = (float) mv.getCenter().getLongitude();

        ValueAnimator animationLat;
        ValueAnimator animationLng;

        animationLat = ValueAnimator.ofFloat(latInit, (float) lat);
        animationLng = ValueAnimator.ofFloat(lngInit, (float) lng);

        animationLat.setInterpolator(new DecelerateInterpolator());
        animationLat.setDuration(500);
        animationLng.setInterpolator(new DecelerateInterpolator());
        animationLng.setDuration(500);

        animationLat.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                if (animation.getAnimatedValue() instanceof Float) {
                    float latValue = ((Float) animation.getAnimatedValue());
                    mv.setCenter(new LatLng(latValue, mv.getCenter().getLongitude()));
                }
            }
        });
        animationLng.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                if (animation.getAnimatedValue() instanceof Float) {
                    float lngValue = ((Float) animation.getAnimatedValue());
                    mv.setCenter(new LatLng(mv.getCenter().getLatitude(), lngValue));
                }
            }
        });
        AnimatorSet a = new AnimatorSet();
        a.playTogether(animationLat, animationLng);
        mv.setZoom(17f);
        a.start();
    }

    /**
     * recupere le rectangle qui contient les pois entré en parametre
     */
    private BoundingBox boxFromPoints(List<POI> pois) {
        double topPadding = 0.005;
        double bottomPadding = 0.005;
        double leftPadding = 0.005;
        double rightPadding = 0.005;
        BoundingBox box = new BoundingBox(findTopMost(pois).getLat() + topPadding,
                findRightMost(pois).getLng() + rightPadding,
                findBottomMost(pois).getLat() - bottomPadding,
                findLeftMost(pois).getLng() - leftPadding);
        return box;
    }

    /**
     * Récupere le poi qui est le plus au nords parmis la liste des pois
     */
    private POI findTopMost(List<POI> points) {
        POI result = points.get(0);
        for (POI p : points) {
            if (result.getLat() < p.getLat()) {
                result = p;
            }
        }
        return result;
    }

    /**
     * Récupere le poi qui est le plus au sud parmis la liste des pois
     */
    private POI findBottomMost(List<POI> points) {
        POI result = points.get(0);
        for (POI p : points) {
            if (result.getLat() > p.getLat()) {
                result = p;
            }
        }
        return result;
    }

    /**
     * Récupere le poi qui est le plus à l'ouest parmis la liste des pois
     */
    private POI findLeftMost(List<POI> points) {
        POI result = points.get(0);
        for (POI p : points) {
            if (result.getLng() > p.getLng()) {
                result = p;
            }
        }
        return result;
    }

    /**
     * Récupere le poi qui est le plus à l'est parmis la liste des pois
     */
    private POI findRightMost(List<POI> points) {
        POI result = points.get(0);
        for (POI p : points) {
            if (result.getLng() < p.getLng()) {
                result = p;
            }
        }
        return result;
    }

}
