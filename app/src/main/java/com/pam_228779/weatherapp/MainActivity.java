package com.pam_228779.weatherapp;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager2.widget.ViewPager2;

import android.os.Bundle;
import android.widget.ImageButton;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ImageButton settingsButton = findViewById(R.id.buttonSettings);
        ImageButton localizationsButton = findViewById(R.id.buttonLocalizations);
        ImageButton refreshButton = findViewById(R.id.buttonRefreshData);

        ViewPager2 viewPager2 = findViewById(R.id.viewPager);
    }
}