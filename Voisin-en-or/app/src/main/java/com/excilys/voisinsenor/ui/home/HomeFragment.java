package com.excilys.voisinsenor.ui.home;

import android.app.Fragment;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.excilys.voisinsenor.R;
import com.excilys.voisinsenor.ui.connection.ConnectionActivity;
import com.excilys.voisinsenor.ui.connection.FirstScreenActivity;
import com.excilys.voisinsenor.ui.connection.LoadingActivity;
import com.excilys.voisinsenor.ui.map.MapActivity;
import com.excilys.voisinsenor.ui.messenger.ConversationAdapter;
import com.excilys.voisinsenor.ui.messenger.ConversationsActivity;
import com.excilys.voisinsenor.ui.profile.ProfilActivity;

import java.io.File;
import java.util.Timer;

import timber.log.Timber;

/**
 * Created by mada on 18/09/15.
 */
public class HomeFragment extends Fragment {

    private LinearLayout messagerie;
    private LinearLayout aide;
    private LinearLayout deconnection;
    private LinearLayout profil;
    private LinearLayout map;
    private Context context;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_home, container, false);
        messagerie = (LinearLayout) v.findViewById(R.id.fragment_home_messagerie);
        aide = (LinearLayout) v.findViewById(R.id.fragment_home_aide);
        deconnection = (LinearLayout) v.findViewById(R.id.fragment_home_deconnexion);
        profil = (LinearLayout) v.findViewById(R.id.fragment_home_profil);
        map = (LinearLayout) v.findViewById(R.id.fragment_home_map);

        messagerie.setOnClickListener(messagerieListener);
        deconnection.setOnClickListener(deconnectionListener);
        map.setOnClickListener(mapListener);
        profil.setOnClickListener(profilListener);


        return v;
    }



    private View.OnClickListener mapListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            Intent i = new Intent(getActivity(), MapActivity.class);
            startActivity(i);
        }
    };

    private View.OnClickListener profilListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            Intent i = new Intent(getActivity(), ProfilActivity.class);
            startActivity(i);
        }
    };


    private View.OnClickListener messagerieListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            Intent i = new Intent(getActivity(), ConversationsActivity.class);
            startActivity(i);
        }
    };


    private View.OnClickListener deconnectionListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            SharedPreferences settings = getActivity().getSharedPreferences(getResources().getString(R.string.myPref), Context.MODE_PRIVATE);
            settings.edit().remove(getResources().getString(R.string.server_adress)).commit();
            File fichier = new File(getResources().getString(R.string.sdcard_selfie));
            fichier.delete();
            Intent i = new Intent(getActivity(), FirstScreenActivity.class);
            startActivity(i);
        }
    };
}
