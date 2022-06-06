package com.pam_228779.weatherapp;

import java.io.Serializable;
import java.util.UUID;

public class WeatherData implements Serializable {
    private UUID mUUID;


    public WeatherData() {
        mUUID = UUID.randomUUID();
    }

    public UUID getUUID() {
        return mUUID;
    }
}
