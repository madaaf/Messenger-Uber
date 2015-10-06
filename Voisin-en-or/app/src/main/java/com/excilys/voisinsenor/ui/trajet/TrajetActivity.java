package com.excilys.voisinsenor.ui.trajet;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.TimePicker;
import com.excilys.voisinsenor.R;
import com.excilys.voisinsenor.model.POI;
import com.excilys.voisinsenor.model.Track;
import com.excilys.voisinsenor.network.service.ITrackService;
import com.excilys.voisinsenor.ui.home.HomeActivity;
import com.excilys.voisinsenor.utils.PopupActivity;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;

import retrofit.Callback;
import retrofit.RestAdapter;
import retrofit.RetrofitError;
import retrofit.client.Response;
import timber.log.Timber;

/**
 * Created by mada on 25/09/15.
 */
public class TrajetActivity extends Activity {
    private static TextView btnChangeTime;
    private static TextView date;
    private static String date_s;
    List<POI> waypoints;
    private TextView monday;
    private TextView tuesday;
    private TextView wednesday;
    private TextView thursday;
    private TextView friday;
    private TextView saturday;
    private TextView sunday;
    private CheckBox isRepetedButton;
    private Set<String> repetedDay;
    private LinearLayout myweek;
    private EditText description;
    private EditText passangers;
    private Button validate;
    private Boolean isRepeted;
    private static int hour;
    private static int minute;
    static final int TIME_DIALOG_ID = 999;
    private RestAdapter restAdapter;
    private PopupActivity popup;
    private String username;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_trajet);

        SharedPreferences sharedPref = getSharedPreferences(getResources().getString(R.string.myPref), Context.MODE_PRIVATE);
        username = sharedPref.getString(getResources().getString(R.string.username), null);

        date = (TextView) findViewById(R.id.activity_trajet_date);
        monday = (TextView) findViewById(R.id.activity_trajet_week_monday);
        tuesday = (TextView) findViewById(R.id.activity_trajet_week_tuesday);
        wednesday = (TextView) findViewById(R.id.activity_trajet_week_wednesday);
        thursday = (TextView) findViewById(R.id.activity_trajet_week_thursday);
        friday = (TextView) findViewById(R.id.activity_trajet_week_friday);
        saturday = (TextView) findViewById(R.id.activity_trajet_week_saturday);
        sunday = (TextView) findViewById(R.id.activity_trajet_week_sunday);
        isRepetedButton = (CheckBox) findViewById(R.id.activity_trajet_isrepeted_button);
        myweek = (LinearLayout) findViewById(R.id.activity_trajet_week);
        description = (EditText) findViewById(R.id.activity_trajet_desc);
        passangers = (EditText) findViewById(R.id.activity_trajet_passengers);
        validate = (Button) findViewById(R.id.activity_trajet_validate);
        validate.setOnClickListener(validateListener);
        date.setOnClickListener(dateListener);
        repetedDay = new TreeSet<>();
        restAdapter = new RestAdapter.Builder().setEndpoint(getResources().getString(R.string.server_adress)).build();
        popup = new PopupActivity(this);
        setCurrentTimeOnView();
        addListenerOnButton();
        Intent intent=this.getIntent();
        Bundle bundle=intent.getExtras();
        waypoints= (List<POI>)bundle.getSerializable("waypoints");

        isRepetedButton.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    isRepeted = true;
                    myweek.setVisibility(View.VISIBLE);
                    date.setVisibility(View.INVISIBLE);
                } else {
                    isRepeted = false;
                    myweek.setVisibility(View.GONE);
                    date.setVisibility(View.VISIBLE);
                }
            }
        });



        monday.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ColorDrawable drawable = (ColorDrawable) monday.getBackground();
                if (drawable.getColor() == getResources().getColor(R.color.swype_1)) {
                    monday.setBackgroundColor(getResources().getColor(R.color.sticky_item_text));
                    repetedDay.remove("monday");
                } else {
                    monday.setBackgroundColor(getResources().getColor(R.color.swype_1));
                    repetedDay.add("monday");
                }
            }
        });

        tuesday.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ColorDrawable drawable = (ColorDrawable) tuesday.getBackground();
                if(drawable.getColor()==getResources().getColor(R.color.swype_1)){
                    tuesday.setBackgroundColor(getResources().getColor(R.color.sticky_item_text));
                    repetedDay.remove("tuesday");
                }else{
                    tuesday.setBackgroundColor(getResources().getColor(R.color.swype_1));
                    repetedDay.add("tuesday");
                }
            }
        });
        wednesday.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ColorDrawable drawable = (ColorDrawable) wednesday.getBackground();
                if(drawable.getColor()==getResources().getColor(R.color.swype_1)){
                    wednesday.setBackgroundColor(getResources().getColor(R.color.sticky_item_text));
                    repetedDay.remove("wednesday");
                }else{
                    wednesday.setBackgroundColor(getResources().getColor(R.color.swype_1));
                    repetedDay.add("wednesday");
                }
            }
        });

        thursday.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ColorDrawable drawable = (ColorDrawable) thursday.getBackground();
                if(drawable.getColor()==getResources().getColor(R.color.swype_1)){
                    thursday.setBackgroundColor(getResources().getColor(R.color.sticky_item_text));
                    repetedDay.remove("thursday");
                }else{
                    thursday.setBackgroundColor(getResources().getColor(R.color.swype_1));
                    repetedDay.add("thursday");
                }
            }
        });
        friday.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ColorDrawable drawable = (ColorDrawable) friday.getBackground();
                if(drawable.getColor()==getResources().getColor(R.color.swype_1)){
                    friday.setBackgroundColor(getResources().getColor(R.color.sticky_item_text));
                    repetedDay.remove("friday");
                }else{
                    friday.setBackgroundColor(getResources().getColor(R.color.swype_1));
                    repetedDay.add("friday");
                }
            }
        });
        saturday.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ColorDrawable drawable = (ColorDrawable) saturday.getBackground();
                if(drawable.getColor()==getResources().getColor(R.color.swype_1)){
                    saturday.setBackgroundColor(getResources().getColor(R.color.sticky_item_text));
                    repetedDay.remove("saturday");
                }else{
                    saturday.setBackgroundColor(getResources().getColor(R.color.swype_1));
                    repetedDay.add("saturday");
                }
            }
        });
        sunday.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ColorDrawable drawable = (ColorDrawable) sunday.getBackground();
                if(drawable.getColor()==getResources().getColor(R.color.swype_1)){
                    sunday.setBackgroundColor(getResources().getColor(R.color.sticky_item_text));
                    repetedDay.remove("sunday");
                }else{
                    sunday.setBackgroundColor(getResources().getColor(R.color.swype_1));
                    repetedDay.add("sunday");
                }
            }
        });

    }
    private View.OnClickListener dateListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            DialogFragment newFragment = new DatePickerFragment();
            newFragment.show(getFragmentManager(), "datePicker");
        }
    };
    // display current time
    public void setCurrentTimeOnView() {
        final Calendar c = Calendar.getInstance();
        hour = c.get(Calendar.HOUR_OF_DAY);
        minute = c.get(Calendar.MINUTE);

    }

    public void addListenerOnButton() {
        btnChangeTime = (TextView) findViewById(R.id.btnChangeTime);
        btnChangeTime.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                showDialog(TIME_DIALOG_ID);
            }
        });
    }

    @Override
    protected Dialog onCreateDialog(int id) {
        switch (id) {
            case TIME_DIALOG_ID:
                // set time picker as current time
                return new TimePickerDialog(this, timePickerListener, hour, minute,
                        false);
        }
        return null;
    }

    private static TimePickerDialog.OnTimeSetListener timePickerListener = new TimePickerDialog.OnTimeSetListener() {
        public void onTimeSet(TimePicker view, int selectedHour,
                              int selectedMinute) {
            hour = selectedHour;
            minute = selectedMinute;
            if (minute >= 10)
                btnChangeTime.setText(hour+":"+minute);
            else
            btnChangeTime.setText(hour+": 0"+minute);
        }
    };


    public static class DatePickerFragment extends DialogFragment implements DatePickerDialog.OnDateSetListener {

        @Override
        public Dialog onCreateDialog(Bundle savedInstanceState) {
            // Use the current date as the default date in the picker
            final Calendar c = Calendar.getInstance();
            int year = c.get(Calendar.YEAR);
            int month = c.get(Calendar.MONTH);
            int day = c.get(Calendar.DAY_OF_MONTH);

            // Create a new instance of DatePickerDialog and return it
            return new DatePickerDialog(getActivity(), this, year, month, day);
        }

        public void onDateSet(DatePicker view, int year, int month, int day) {
            month = month + 1;
            String month_s = String.valueOf(month);
            String day_s = String.valueOf(day);
            if (month < 10) {
                month_s = "0" + month_s;
            }
            if (day < 10) {
                day_s = "0" + day;
            }
            date.setText(day_s + "/" + month_s + "/" + year);
            date_s = year + "-" + month_s + "-" + day_s;
        }
    }

    private View.OnClickListener validateListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {

            SharedPreferences sharedPref = getSharedPreferences(getResources().getString(R.string.myPref), Context.MODE_PRIVATE);
            String myemail = sharedPref.getString(getResources().getString(R.string.myemail), null);

            String time = hour+":"+minute;
            String desc = description.getText().toString();
            String pass = passangers.getText().toString();
            Timber.e(time+" "+desc+" "+pass+" "+date_s+" "+isRepeted);
            Timber.e(pass+" "+date_s+" "+isRepeted);
            Timber.e(date_s);
            Track track = new Track();
            if(isRepeted==true){
                track.setDescription(desc);
                track.setUserEmail(myemail);
                track.setDepartureTime(time);
                track.setUserName(username);
                track.setWaypoints(waypoints);
                List<String> formatRepetedDay = new ArrayList<>();
                for(String day: repetedDay){
                    formatRepetedDay.add(day);
                }
                track.setRepeatDays(formatRepetedDay);
                Timber.e(repetedDay.toArray().toString());
            }else{
                track.setDescription(desc);
                track.setUserEmail(myemail);
                track.setDepartureTime(time);
                track.setUserName(username);
                track.setWaypoints(waypoints);
                track.setDate(date_s);
                Timber.e(repetedDay.toArray().toString());
            }
            ITrackService service = restAdapter.create(ITrackService.class);
            service.postTrack(track, new Callback<String>() {
                @Override
                public void success(String s, Response response) {
                    Timber.e("response success", response);
                    popup.afficherPopup("Votre trajet à été proposé avec succès",new Redirection());
                    Intent i = new Intent(TrajetActivity.this, HomeActivity.class);
                    startActivity(i);
                }

                @Override
                public void failure(RetrofitError error) {
                    Timber.e("response error"+ error.getMessage());
                    popup.afficherPopup("Une erreur est survenue lors de la proposition de votre trajet", null);
                }
            });

        }
    };

    class Redirection implements android.content.DialogInterface.OnClickListener{
        public void onClick(DialogInterface dialog, int which) {
            Intent i = new Intent(TrajetActivity.this, HomeActivity.class);
            startActivity(i);
        }
    }

}
