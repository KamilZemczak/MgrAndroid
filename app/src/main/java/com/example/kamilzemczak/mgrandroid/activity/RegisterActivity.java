package com.example.kamilzemczak.mgrandroid.activity;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.example.kamilzemczak.mgrandroid.R;
import com.example.kamilzemczak.mgrandroid.backgroundworker.RegisterBackgroundWorker;
import com.example.kamilzemczak.mgrandroid.backgroundworker.UniqueBackgroundWorker;

import java.text.ParseException;
import java.util.Calendar;
import java.util.concurrent.ExecutionException;

public class RegisterActivity extends AppCompatActivity {

    private static final String TAG = "RegisterActivity";

    private DatePickerDialog.OnDateSetListener mDateSetListener;
    private EditText mDisplayDate, name, surname, username, password, passwordConfirm, dateOfBirth;
    private TextView chooseGender;
    private Button registerButton;
    private RadioGroup genderRadioGroup;
    private RadioButton genderRadioButton;
    private String gender;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        mDisplayDate = (EditText) findViewById(R.id.registerActivity_etDateOfBirth);
        name = (EditText) findViewById(R.id.registerActivity_etName);
        surname = (EditText) findViewById(R.id.registerActivity_etSurname);
        username = (EditText) findViewById(R.id.registerActivity_etUsername);
        dateOfBirth = (EditText) findViewById(R.id.registerActivity_etDateOfBirth);
        password = (EditText) findViewById(R.id.registerActivity_etPassword);
        passwordConfirm = (EditText) findViewById(R.id.registerActivity_etPasswordConfirm);
        genderRadioGroup = (RadioGroup) findViewById(R.id.registerActivity_rgGender);
        registerButton = (Button) findViewById(R.id.registerActivity_bRegister);
        chooseGender = (TextView) findViewById(R.id.registerActivity_tvGender);

        mDisplayDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Calendar cal = Calendar.getInstance();
                int year = cal.get(Calendar.YEAR);
                int month = cal.get(Calendar.MONTH);
                int day = cal.get(Calendar.DAY_OF_MONTH);

                DatePickerDialog dialog = new DatePickerDialog(RegisterActivity.this, android.R.style.Theme_Holo_Light_Dialog_MinWidth, mDateSetListener, year,month,day);
                dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                dialog.show();
            }
        });

        mDateSetListener = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker datePicker, int year, int month, int dayOfMonth) {
                month = month + 1;
                Log.d(TAG, "onDateSet: dd/mm/yyyy: " + dayOfMonth + "/" + month + "/" + year);

                String date = dayOfMonth + "/" + month + "/" + year;
                mDisplayDate.setText(date);
            }
        };
    }

    public void openLogin(View view) {
        startActivity(new Intent(this, WelcomeActivity.class));
    }

    public void register(View view) throws ParseException {
        if (!validate()) {
            registerFailed();
            return;
        }
        String username = this.username.getText().toString();
        String password = this.password.getText().toString();
        String passwordConfirm = this.passwordConfirm.getText().toString();
        String name = this.name.getText().toString();
        String surname = this.surname.getText().toString();
        String stringDate = this.dateOfBirth.getText().toString();
        String type = "register";
        RegisterBackgroundWorker registerBackgroundWorker = new RegisterBackgroundWorker(this);
        registerBackgroundWorker.execute(type, username, password, passwordConfirm, name, surname, stringDate, gender);
        registerSuccess();
        openLogin(view);
    }

    public void radioButtonClick(View view) {
        int radioButtonId = genderRadioGroup.getCheckedRadioButtonId();
        genderRadioButton = (RadioButton) findViewById(radioButtonId);
        String genderCheck = genderRadioButton.getText().toString();
        if (genderCheck.equals("MĘŻCZYZNA") && genderCheck != null) {
            gender = "male";
        } else if (genderCheck.equals("KOBIETA") && genderCheck != null) {
            gender = "female";
        } else {
            return;
        }
    }

    public boolean validate() {
        boolean valid = true;

        String name = this.name.getText().toString();
        String surname = this.surname.getText().toString();
        String username = this.username.getText().toString();
        String dateOfBirth = this.dateOfBirth.getText().toString();
        String password = this.password.getText().toString();
        String passwordConfirm = this.passwordConfirm.getText().toString();

        valid = validName(valid, name);
        valid = validSurname(valid, surname);
        valid = validUsername(valid, username);
        //valid = validDate(valid, date);
        valid = validPassword(valid, password, passwordConfirm);
        valid = validGender(valid);

        return valid;
    }

    private boolean validGender(boolean valid) {
        if (genderRadioGroup.getCheckedRadioButtonId() <= 0) {
            chooseGender.setError("Wybierz płeć.");
            valid = false;
        } else {
            chooseGender.setError(null);
        }
        return valid;
    }

    private boolean validPassword(boolean valid, String password, String passwordConfirm) {
        if (password.isEmpty() || password.length() < 8 || password.length() > 26) {
            this.password.setError("Hasło musi zawierać co najmniej 8 znaków.");
            valid = false;
        } else if (!passwordConfirm.equals(password)) {
            this.password.setError("Wybrane hasła się od siebie różnią.");
            valid = false;
        } else {
            this.password.setError(null);
        }
        return valid;
    }

    private boolean validUsername(boolean valid, String username) {
        if (username.isEmpty() || username.length() < 4 || username.length() > 26) {
            this.username.setError("Nazwa użytkownika musi zawierać minimum cztery znaki.");
            valid = false;
        } else if (!isUniqueUser(username)) {
            this.username.setError("Nazwa użytkownika jest już używana.");
            valid = false;
        } else {
            this.username.setError(null);
        }
        return valid;
    }

    private boolean validSurname(boolean valid, String surname) {
        if (surname.isEmpty() || surname.length() < 3 || surname.length() > 26) {
            this.surname.setError("Nazwisko musi zawierać minimum trzy znaki.");
            valid = false;
        } else if (!Character.isUpperCase(surname.charAt(0))) {
            this.surname.setError("Nazwisko musi zaczynać się od dużej litery");
            valid = false;
        } else {
            this.surname.setError(null);
        }
        return valid;
    }

    private boolean validName(boolean valid, String name) {
        if (name.isEmpty() || name.length() < 3 || name.length() > 26) {
            this.name.setError("Imię musi zawierać minimum trzy znaki.");
            valid = false;
        } else if (!Character.isUpperCase(name.charAt(0))) {
            this.name.setError("Imię musi zaczynać się od dużej litery.");
            valid = false;
        } else {
            this.name.setError(null);
        }
        return valid;
    }

    private boolean isUniqueUser(String str_username) {
        String type = "unique_user";
        Boolean result = true;
        UniqueBackgroundWorker uniqueBackgroundWorker = new UniqueBackgroundWorker(this);
        try {
            result = uniqueBackgroundWorker.execute(type, str_username).get();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }
        return result;
    }

    public void registerFailed() {
        Toast.makeText(getBaseContext(), "Rejestracja nieudana.", Toast.LENGTH_LONG).show();
        registerButton.setEnabled(true);
    }

    public void registerSuccess() {
        Toast.makeText(getBaseContext(), "Rejestracja udana.", Toast.LENGTH_LONG).show();
        registerButton.setEnabled(true);
    }
}
