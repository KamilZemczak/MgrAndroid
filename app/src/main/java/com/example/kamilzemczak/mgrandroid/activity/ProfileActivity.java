package com.example.kamilzemczak.mgrandroid.activity;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.PopupMenu;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.example.kamilzemczak.mgrandroid.R;
import com.example.kamilzemczak.mgrandroid.backgroundworker.UniqueBackgroundWorker;
import com.example.kamilzemczak.mgrandroid.backgroundworker.UserBackgroundWorker;
import com.example.kamilzemczak.mgrandroid.helper.MySingleton;
import com.example.kamilzemczak.mgrandroid.model.User;
import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;
import java.text.Format;
import java.text.SimpleDateFormat;
import java.util.concurrent.ExecutionException;

public class ProfileActivity extends AppCompatActivity {

    MySingleton singleton = MySingleton.getInstance();

    private LoginActivity loginActivity;

    private EditText username, name, surname, dateOfBirth, gender, weight, height, favourite;
    private Button updateButton;
    private RadioButton genderRadioButton, rbMale, rbFemale;
    private RadioGroup genderRadioGroup;

    private Integer userCurrentId = loginActivity.userCurrentId;
    private String currentUsername = loginActivity.userCurrentUsername;
    private String genderOptionTwo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        username = (EditText) findViewById(R.id.profileActivity_etUsername);
        name = (EditText) findViewById(R.id.profileActivity_etName);
        surname = (EditText) findViewById(R.id.profileActivity_etSurname);
        dateOfBirth = (EditText) findViewById(R.id.profileActivity_etDateOfBirth);
        genderRadioGroup = (RadioGroup) findViewById(R.id.profileActivity_rgGender);
        weight = (EditText) findViewById(R.id.profileActivity_etWeight);
        height = (EditText) findViewById(R.id.profileActivity_etHeight);
        favourite = (EditText) findViewById(R.id.profileActivity_etFavourite);
        updateButton = (Button) findViewById(R.id.profileActivity_bEditProfile);
        rbMale = (RadioButton) findViewById(R.id.profileActivity_rbMale);
        rbFemale = (RadioButton) findViewById(R.id.profileActivity_rbFemale);
        setCurrentUserDetails();
        favourite.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PopupMenu popupMenu = new PopupMenu(ProfileActivity.this, favourite);
                popupMenu.getMenuInflater().inflate(R.menu.popup_menu, popupMenu.getMenu());

                popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem item) {
                        Toast.makeText(ProfileActivity.this, "Wybrano " + item.getTitle(), Toast.LENGTH_SHORT).show();
                        favourite.setText(" " + item.getTitle());
                        return true;
                    }
                });
                popupMenu.show();
            }
        });
    }

    public void updateUser(View view) {
        if (!validate()) {
            updateProfileFailed();
            return;
        }
        String type = "user_update";
        String id = Integer.toString(userCurrentId);
        String username = this.username.getText().toString();
        String name = this.name.getText().toString();
        String surname = this.surname.getText().toString();
        String stringDate = this.dateOfBirth.getText().toString();
        if (genderOptionTwo == null) {
            if (rbMale.isChecked()) {
                genderOptionTwo = "male";
            } else {
                genderOptionTwo = "female";
            }
        }
        String weight = this.weight.getText().toString();
        String height = this.height.getText().toString();
        String favourite = this.favourite.getText().toString();
        UserBackgroundWorker userBackgroundWorker = new UserBackgroundWorker(this);
        userBackgroundWorker.execute(type, id, username, name, surname, stringDate, genderOptionTwo, weight, height, favourite);
        updateProfileSuccess();
        getUserDetails();
        showProfile(view);
    }

    public void radioButtonClick(View view) {
        int radioButtonId = genderRadioGroup.getCheckedRadioButtonId();
        genderRadioButton = (RadioButton) findViewById(radioButtonId);
        String genderCheck = genderRadioButton.getText().toString();
        if (genderCheck.equals("MĘŻCZYZNA") && genderCheck != null) {
            genderOptionTwo = "male";
        } else if (genderCheck.equals("KOBIETA") && genderCheck != null) {
            genderOptionTwo = "female";
        } else {
            return;
        }
    }

    public void getUserDetails() {
        String type = "user_details";
        String username = this.username.getText().toString();
        UserBackgroundWorker userBackgroundWorker = new UserBackgroundWorker(this);
        try {
            ObjectMapper mapper = new ObjectMapper();
            User currentUser;
            String userJson = userBackgroundWorker.execute(type, username).get();
            currentUser = mapper.readValue(userJson, User.class);
            setCurrentUserValues(currentUser);
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

    public boolean validate() {
        boolean valid = true;

        String username = this.username.getText().toString();
        String name = this.name.getText().toString();
        String surname = this.surname.getText().toString();
        String weight = null;
        String height = null;
        Integer iWeight = null;
        Integer iHeight = null;

        if (this.weight.getText() != null) {
            weight = this.weight.getText().toString();
        }

        if (this.height.getText() != null) {
            height = this.height.getText().toString();
        }

        if (!weight.isEmpty()) {
            iWeight = Integer.parseInt(weight);
        }

        if (!height.isEmpty()) {
            iHeight = Integer.parseInt(height);
        }

        valid = validUsername(valid, username);
        valid = validWeight(valid, weight, iWeight);
        valid = validHeight(valid, height, iHeight);
        return valid;
    }

    public void updateProfileFailed() {
        Toast.makeText(getBaseContext(), "Zaktualizowanie profilu nieudane.", Toast.LENGTH_LONG).show();
        updateButton.setEnabled(true);
    }

    public void updateProfileSuccess() {
        Toast.makeText(getBaseContext(), "Zaktualizowanie profilu udane.", Toast.LENGTH_LONG).show();
        updateButton.setEnabled(true);
    }

    public void showProfile(View view) {
        startActivity(new Intent(this, WelcomeActivity.class));
    }

    public void logout(MenuItem menu) {
        startActivity(new Intent(this, LoginActivity.class));
        Toast.makeText(getBaseContext(), "Wylogowanie powiodło się!", Toast.LENGTH_LONG).show();
    }

    private void setCurrentUserDetails() {
        Format formatter = new SimpleDateFormat("dd-MM-yyyy");
        String stringDateOfBirth = formatter.format(loginActivity.userCurrentDateOfBirth);
        username.setText(loginActivity.userCurrentUsername, TextView.BufferType.EDITABLE);
        name.setText(loginActivity.userCurrentName, TextView.BufferType.EDITABLE);
        surname.setText(loginActivity.userCurrentSurname, TextView.BufferType.EDITABLE);
        dateOfBirth.setText(stringDateOfBirth, TextView.BufferType.EDITABLE);

        if (loginActivity.userCurrentGender.equals("male")) {
            rbMale.setChecked(true);

        } else {
            rbFemale.setChecked(true);
        }

        if (loginActivity.userCurrentGender != null && loginActivity.userCurrentGender.equals("M")) {
            gender.setText("Mężczyzna", TextView.BufferType.EDITABLE);
        }
        if (loginActivity.userCurrentGender != null && loginActivity.userCurrentGender.equals("F")) {
            gender.setText("Kobieta", TextView.BufferType.EDITABLE);
        }
        if ((singleton.getWeight()) != null) {
            weight.setText(Integer.toString(singleton.getWeight()), TextView.BufferType.EDITABLE);
        }
        if ((singleton.getHeight()) != null) {
            height.setText(Integer.toString(singleton.getHeight()), TextView.BufferType.EDITABLE);
        }
        if (singleton.getFavourite() != null) {
            favourite.setText(singleton.getFavourite(), TextView.BufferType.EDITABLE);
        }
    }

    private void setCurrentUserValues(User currentUser) {
        loginActivity.userCurrentId = currentUser.getId();
        loginActivity.userCurrentUsername = currentUser.getUsername();
        loginActivity.userCurrentName = currentUser.getName();
        loginActivity.userCurrentSurname = currentUser.getSurname();
        loginActivity.userCurrentDateOfBirth = currentUser.getDateOfBirth();
        loginActivity.userCurrentGender = currentUser.getGender();
        if (singleton.getWeight() != null) {
            singleton.setWeight(currentUser.getWeight());
            loginActivity.userCurrentWeight = currentUser.getWeight();
        }
        if (singleton.getHeight() != null) {
            singleton.setHeight(currentUser.getHeight());
            loginActivity.userCurrentHeight = currentUser.getHeight();
        }
        if (singleton.getFavourite() != null) {
            singleton.setFavourite(currentUser.getFavourite());
            loginActivity.userCurrentFavourite = currentUser.getFavourite();
        }
    }

    private boolean validHeight(boolean valid, String height, Integer iHeight) {
        if (!height.isEmpty() && (height.length() < 2 || height.length() > 3)) {
            this.height.setError("Niepoprawny format wzrostu.");
            valid = false;
        } else if (iHeight != null && iHeight < 80) {
            this.height.setError("Najniższy wzrost dostępny w serwisie to 80cm.");
            valid = false;
        } else if (iHeight != null && iHeight > 250) {
            this.height.setError("Najwyższy wzrost dostępny w serwisie to 250cm.");
            valid = false;
        } else {
            this.height.setError(null);
        }
        return valid;
    }

    private boolean validWeight(boolean valid, String weight, Integer iWeight) {
        if (!weight.isEmpty() && (weight.length() < 1 || weight.length() > 3)) {
            this.weight.setError("Niepoprawny format wagi.");
            valid = false;
        } else if (iWeight != null && iWeight < 30) {
            this.weight.setError("Najniższa waga dostępna w serwisie to 30kg.");
            valid = false;
        } else if (iWeight != null && iWeight > 200) {
            this.weight.setError("Najwyższa waga dostępna w serwisie to 200kg.");
            valid = false;
        } else {
            this.weight.setError(null);
        }
        return valid;
    }

    private boolean validUsername(boolean valid, String username) {
        if (username.isEmpty() || username.length() < 4 || username.length() > 26) {
            this.username.setError("Nazwa użytkownika musi zawierać minimum cztery znaki.");
            valid = false;
        } else if (!isUniqueUser(username) && !sameUsername(username)) {
            this.username.setError("Nazwa użytkownika jest już używana.");
            valid = false;
        } else {
            this.username.setError(null);
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

    private boolean sameUsername(String username) {
        Boolean result = false;
        if (username.equals(currentUsername)) {
            result = true;
        }
        return result;
    }
}
