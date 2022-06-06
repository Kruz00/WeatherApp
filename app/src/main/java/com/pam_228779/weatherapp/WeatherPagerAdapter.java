package com.pam_228779.weatherapp;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import java.util.ArrayList;

public class WeatherPagerAdapter extends FragmentStateAdapter {

    private ArrayList<WeatherData> wDataList;

    public WeatherPagerAdapter(@NonNull FragmentActivity fragmentActivity, ArrayList<WeatherData> wDataList) {
        super(fragmentActivity);
        this.wDataList = wDataList;
    }

    @NonNull
    @Override
    public Fragment createFragment(int position) {
        return WeatherPageFragment.newInstance(wDataList.get(position));
    }

    @Override
    public int getItemCount() {
        return wDataList.size();
    }
}
