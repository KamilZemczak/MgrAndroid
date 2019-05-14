package com.example.kamilzemczak.mgrandroid.activity;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.kamilzemczak.mgrandroid.R;
import com.example.kamilzemczak.mgrandroid.backgroundworker.LoginBackgroundWorker;
import com.example.kamilzemczak.mgrandroid.backgroundworker.UserBackgroundWorker;
import com.example.kamilzemczak.mgrandroid.helper.MySingleton;
import com.example.kamilzemczak.mgrandroid.model.User;
import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;
import java.util.Date;
import java.util.concurrent.ExecutionException;

public class LoginActivity extends AppCompatActivity {

    MySingleton singleton =  MySingleton.getInstance();

    private EditText username, password;
    private Button loginButton;

    public static String userCurrentName, userCurrentSurname, userCurrentUsername, userCurrentGender, userCurrentFavourite;
    public static Date userCurrentDateOfBirth;
    public static Integer userCurrentId, userCurrentWeight, userCurrentHeight;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        username = (EditText) findViewById(R.id.loginActivity_etName);
        password = (EditText) findViewById(R.id.loginActivity_etPassword);
        loginButton = (Button) findViewById(R.id.loginActivity_bLogin);
    }

    public void login(View view) {
        if (!validate()) {
            loginFailed();
            return;
        }

        String type = "login";
        String username = this.username.getText().toString();
        String password = this.password.getText().toString();
        String result = null;
        LoginBackgroundWorker loginBackgroundWorker = new LoginBackgroundWorker(this);
        try {
            result = loginBackgroundWorker.execute(type, username, password).get();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }
        if (result.equals("Wrong.")) {
            loginFailed();
            this.username.setError("Niewłaściwy login lub hasło.");
            this.password.setError("Niewłaściwy login lub hasło.");
            return;
        } else if (result.startsWith("<!DOCTYPE")) {
            getUserDetails();
            loginSuccess();
            startActivity(new Intent(this, WelcomeActivity.class));
        }
    }

    public void getUserDetails() {
        String username = this.username.getText().toString();
        String type = "user_details";
        UserBackgroundWorker userBackgroundWorker = new UserBackgroundWorker(this);
        try {
            User currentUser;
            ObjectMapper mapper = new ObjectMapper();
            String userJson = userBackgroundWorker.execute(type, username).get();
            currentUser = mapper.readValue(userJson, User.class);
            prepareUserData(currentUser);
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        } catch (JsonParseException e) {
            e.printStackTrace();
        } catch (JsonMappingException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void prepareUserData(User currentUser) {
        userCurrentId = currentUser.getId();
        userCurrentName = currentUser.getName();
        singleton.setCurrentName(currentUser.getName());
        singleton.setWeight(currentUser.getWeight());
        singleton.setHeight(currentUser.getHeight());
        singleton.setDateOfBirth(currentUser.getDateOfBirth());
        singleton.setFavourite(currentUser.getFavourite());
        userCurrentSurname = currentUser.getSurname();
        userCurrentUsername = currentUser.getUsername();
        userCurrentDateOfBirth = currentUser.getDateOfBirth();
        userCurrentGender = currentUser.getGender();
        if (currentUser.getWeight() != null) {
            userCurrentWeight = currentUser.getWeight();
        }
        if (currentUser.getHeight() != null) {
            userCurrentHeight = currentUser.getHeight();
        }
        if (currentUser.getFavourite() != null) {
            userCurrentFavourite = currentUser.getFavourite();
        }
    }

    public boolean validate() {
        boolean valid = true;

        String username = this.username.getText().toString();
        String password = this.password.getText().toString();

        if (username.isEmpty()) {
            this.username.setError("Nie wpisano nazwy użytkownika.");
            valid = false;
        } else {
            this.username.setError(null);
        }

        if (password.isEmpty()) {
            this.password.setError("Nie wpisano hasła.");
            valid = false;
        } else {
            this.password.setError(null);
        }

        return valid;
    }

    public void loginFailed() {
        Toast.makeText(getBaseContext(), "Logowanie nieudane.", Toast.LENGTH_LONG).show();
        loginButton.setEnabled(true);
    }

    public void loginSuccess() {
        Toast.makeText(getBaseContext(), "Logowanie udane.", Toast.LENGTH_LONG).show();
        loginButton.setEnabled(true);
    }


    public void openRegister(View view) {
        startActivity(new Intent(this, RegisterActivity.class));
    }
}
