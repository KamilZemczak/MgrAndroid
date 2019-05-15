package com.example.kamilzemczak.mgrandroid.activity;

import android.content.Intent;
import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.kamilzemczak.mgrandroid.R;
import com.smarteist.autoimageslider.DefaultSliderView;
import com.smarteist.autoimageslider.IndicatorAnimations;
import com.smarteist.autoimageslider.SliderLayout;
import com.smarteist.autoimageslider.SliderView;

import static android.view.View.VISIBLE;

public class WelcomeActivity extends AppCompatActivity {

    SliderLayout sliderLayout;
    private LoginActivity loginActivity;
    private TextView welcomeText;
    private Button profileButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome);
        welcomeText = (TextView) findViewById(R.id.welcomeActivity_tvInfo2);
        profileButton = (Button) findViewById(R.id.welcomeActivity_bProfile);
        sliderLayout = findViewById(R.id.imageSlider);
        profileButton.setVisibility(VISIBLE);
        profileButton.setBackgroundColor(Color.TRANSPARENT);
        profileButton.setTextColor(Color.TRANSPARENT);
        welcomeText.setText("Zalogowany jako " + loginActivity.userCurrentName + " " + loginActivity.userCurrentSurname);
        sliderLayout.setIndicatorAnimation(IndicatorAnimations.FILL); //set indicator animation by using SliderLayout.Animations. :WORM or THIN_WORM or COLOR or DROP or FILL or NONE or SCALE or SCALE_DOWN or SLIDE and SWAP!!
        sliderLayout.setScrollTimeInSec(3); //set scroll delay in seconds :
        setSliderViews();
    }

    private void setSliderViews() {
        for (int i = 0; i <= 3; i++) {
            DefaultSliderView sliderView = new DefaultSliderView(this);
            switch (i) {
                case 0:
                    sliderView.setImageDrawable(R.drawable.carousel1);
                    break;
                case 1:
                    sliderView.setImageDrawable(R.drawable.carousel2);
                    break;
                case 2:
                    sliderView.setImageDrawable(R.drawable.carousel3);
                    break;
                case 3:
                    sliderView.setImageDrawable(R.drawable.carousel4);
                    break;
            }

            sliderView.setImageScaleType(ImageView.ScaleType.CENTER_CROP);
            final int finalI = i;
            sliderView.setOnSliderClickListener(new SliderView.OnSliderClickListener() {
                @Override
                public void onSliderClick(SliderView sliderView) {
                    Toast.makeText(WelcomeActivity.this, "This is slider " + (finalI + 1), Toast.LENGTH_SHORT).show();
                }
            });
            sliderLayout.addSliderView(sliderView);
        }
    }

    public void openProfile(View view) {
        startActivity(new Intent(this, ProfileActivity.class));
    }
}
