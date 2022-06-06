package com.pam_228779.weatherapp;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

public class WeatherPageFragment extends Fragment {

    WeatherData weatherData;

    public static WeatherPageFragment newInstance(WeatherData weatherData) {
        WeatherPageFragment fragment = new WeatherPageFragment();
        Bundle args = new Bundle();
        args.putSerializable("weatherData", weatherData);
        fragment.setArguments(args);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_weather_page, container, false);
        Bundle bundle=getArguments();
        if(bundle !=null) {
            weatherData = (WeatherData) bundle.getSerializable("weatherData");
        }
        return view;
    }
}