package com.pam_228779.weatherapppro.repository

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.map
import com.google.gson.Gson
import com.pam_228779.weatherapppro.data.api.WeatherApiClient
import com.pam_228779.weatherapppro.data.db.dao.WeatherDao
import com.pam_228779.weatherapppro.data.db.entities.LocationEntity
import com.pam_228779.weatherapppro.data.db.entities.WeatherEntity
import com.pam_228779.weatherapppro.data.model.WeatherData
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.launch

class WeatherRepository(
    private val weatherDao: WeatherDao,
    private val weatherApiClient: WeatherApiClient,
) {
    private val TAG = "WeatherRepository"

    private val language = "pl"

    //    private val units = "metric"
    private val exclude = "minutely,alerts"

    val weatherDataMap = mutableMapOf<Int, LiveData<WeatherData?>>()


    fun getLiveWeather(locationId: Int): LiveData<WeatherData?> {
        return weatherDataMap.getOrPut(locationId) {
            return weatherDao.getWeather(locationId).map {
                Gson().fromJson(
                    it?.weatherData,
                    WeatherData::class.java
                )
            }
        }
    }

    suspend fun deleteWeather(locationId: Int) {
        weatherDao.deleteById(locationId)
    }

    suspend fun newWeatherLocation(location: LocationEntity, units: String) {
        Log.i(TAG, "newWeatherLocation - Start updating weather for locId: ${location.id}")
        val weather =
            weatherApiClient.getOneCall(location.lat, location.lon, language, units, exclude)
                ?.let { WeatherData(it, units) }
        val weatherEntity =
            WeatherEntity(location.id, location.lat, location.lon, Gson().toJson(weather))
        Log.i(TAG, "End updating weather for locId: ${weatherEntity.locationId}")
        weatherDao.insert(weatherEntity)    // TODO if livedata don't work, make update then insert if not already exist
    }

    suspend fun refreshAllWeathers(units: String) {
        val allWeathers = weatherDao.getAllWeathers()
        coroutineScope {
            allWeathers.forEach { weatherEntity ->
                launch(Dispatchers.IO) {
                    Log.i(TAG, "refreshAllWeathers - Start refreshing weather for locId: ${weatherEntity.locationId}")
                    weatherEntity.weatherData = Gson().toJson(
                        weatherApiClient.getOneCall(
                            weatherEntity.lat,
                            weatherEntity.lon,
                            language,
                            units,
                            exclude
                        )
                            ?.let { WeatherData(it, units) }
                    )
                    Log.i(TAG, "End refreshing weather for locId: ${weatherEntity.locationId}")
                }
            }
        }
        weatherDao.updateWeathers(allWeathers)
    }

    suspend fun isWeatherForLocation(location: LocationEntity): Boolean {
        return weatherDao.isWeatherExist(location.id)
    }
}