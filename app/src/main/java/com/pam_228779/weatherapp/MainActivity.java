package com.pam_228779.weatherapp;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager2.widget.ViewPager2;

import android.os.Bundle;
import android.widget.ImageButton;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
    WeatherPagerAdapter pagerAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ImageButton settingsButton = findViewById(R.id.buttonSettings);
        ImageButton localizationsButton = findViewById(R.id.buttonLocalizations);
        ImageButton refreshButton = findViewById(R.id.buttonRefreshData);
        ViewPager2 viewPager2 = findViewById(R.id.viewPager);

        ArrayList<WeatherData> weatherDataList = new ArrayList<>();
        for (int i = 0; i < 3; i++) {
            weatherDataList.add(new WeatherData());
        }
        pagerAdapter = new WeatherPagerAdapter(this, weatherDataList);

        viewPager2.setAdapter(pagerAdapter);
    }
}