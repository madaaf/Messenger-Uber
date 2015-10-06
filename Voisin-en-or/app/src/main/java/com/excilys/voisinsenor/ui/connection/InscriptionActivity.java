package com.excilys.voisinsenor.ui.connection;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import com.excilys.voisinsenor.R;
import com.excilys.voisinsenor.model.User;
import com.excilys.voisinsenor.network.event.GetUserEvent;
import com.excilys.voisinsenor.utils.Internet;
import com.excilys.voisinsenor.utils.PopupActivity;

import java.util.Calendar;

import de.greenrobot.event.EventBus;

/**
 * Created by mada on 21/09/15.
 */
public class InscriptionActivity extends Activity {

    private Button suivant;
    private ImageButton retour;
    private EditText firstname = null;
    private EditText lastname = null;
    private EditText mail = null;
    private EditText tel = null;
    private EditText mdp = null;
    private EditText adress = null;
    private static Button birthday = null;
    private RadioGroup radioSexGroup = null;
    private RadioButton radioSexButton = null;
    private static String birthday_s = null;
    private PopupActivity popupActivity = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_inscription);
        suivant = (Button) findViewById(R.id.activity_inscription_suivant);
        retour = (ImageButton) findViewById(R.id.activity_return);
        birthday = (Button) findViewById(R.id.activity_inscription_bouton_date_naissance);
        tel = (EditText) findViewById(R.id.activity_inscription_phone);
        mdp = (EditText) findViewById(R.id.activity_inscription_mdp);
        firstname = (EditText) findViewById(R.id.activity_inscription_prenom);
        lastname = (EditText) findViewById(R.id.activity_inscription_nom);
        mail = (EditText) findViewById(R.id.activity_inscription_email);
        adress = (EditText) findViewById(R.id.activity_inscription_adress);
        radioSexGroup = (RadioGroup) findViewById(R.id.activity_inscription_radiouGroup);
        popupActivity = new PopupActivity(this);
        retour.setOnClickListener(retourListener);
        suivant.setOnClickListener(suivantListener);
        birthday.setOnClickListener(birthdayListener);

    }

    private View.OnClickListener suivantListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            String lastname_s = lastname.getText().toString();
            String firstname_s = firstname.getText().toString();
            String mdp_s = mdp.getText().toString();
            String tel_s = tel.getText().toString();
            String mail_s = mail.getText().toString();
            String adress_s = adress.getText().toString();
            String sex = null;

            // get selected radio button from radioGroup
            int selectedId = radioSexGroup.getCheckedRadioButtonId();
            // find the radiobutton by returned id
            if (selectedId != -1) {
                radioSexButton = (RadioButton) findViewById(selectedId);
                sex = (String) radioSexButton.getText();

                if (sex.equals("Femme")) {
                    sex = "female";
                } else if (sex.equals("Homme")) {
                    sex = "male";
                }
            }

            String error = null;
            if(firstname_s.equals("")) {
                error = getResources().getString(R.string.inscription_error11);
            }else if (lastname_s.equals("")) {
                error = getResources().getString(R.string.inscription_error1);
            }else if (adress_s.equals("")) {
                error = getResources().getString(R.string.inscription_error12);
            } else if (tel_s.equals("")) {
                error = getResources().getString(R.string.inscription_error3);
            }else if (mail_s.equals("")) {
                error = getResources().getString(R.string.inscription_error2);
            } else if (mdp_s.equals("")) {
                error = getResources().getString(R.string.inscription_error4);
            } else if (sex == null) {
                error = getResources().getString(R.string.inscription_error5);
            } else if (birthday_s == null) {
                error = getResources().getString(R.string.inscription_error6);
            }

            if(error!=null) {
                 popupActivity.afficherPopup(error, null);
            }else{
                User user = new User();
                user.setFirstName(firstname_s);
                user.setName(lastname_s);
                user.setBirthday(birthday_s);
                user.setPhoneNumber(tel_s);
                user.setEmail(mail_s);
                user.setGender(sex);
                user.setAddress(adress_s);
                GetUserEvent eventUser = new GetUserEvent(user);
                EventBus.getDefault().postSticky(eventUser);
                Intent i = new Intent(InscriptionActivity.this, InscriptionBisActivity.class);
                startActivity(i);
            }
        }
    };


    private View.OnClickListener retourListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            Intent i = new Intent(InscriptionActivity.this, FirstScreenActivity.class);
            startActivity(i);
        }
    };

    private View.OnClickListener birthdayListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            DialogFragment newFragment = new DatePickerFragment();
            newFragment.show(getFragmentManager(), "datePicker");
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
            birthday.setText(day_s + "/" + month_s + "/" + year);
            birthday_s = year + "-" + month_s + "-" + day_s;
            System.out.println("date_birthday" + birthday_s);
        }
    }
}
