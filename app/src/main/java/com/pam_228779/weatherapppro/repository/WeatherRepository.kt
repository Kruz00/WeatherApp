package com.pam_228779.weatherapppro.repository

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.map
import com.google.gson.Gson
import com.pam_228779.weatherapppro.data.model.WeatherData
import com.pam_228779.weatherapppro.data.api.WeatherApiClient
import com.pam_228779.weatherapppro.data.db.dao.WeatherDao
import com.pam_228779.weatherapppro.data.db.entities.LocationEntity
import com.pam_228779.weatherapppro.data.db.entities.WeatherEntity
import com.pam_228779.weatherapppro.data.model.Weather
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.launch
import java.time.LocalDateTime

class WeatherRepository(
    private val weatherDao: WeatherDao,
    private val weatherApiClient: WeatherApiClient
) {
    private val TAG = "WeatherRepository"

    private val language = "pl"
    private val units = "metric"
    private val exclude = "minutely,alerts"


    fun getWeather(locationId: Int): LiveData<WeatherData?> {
        val liveWeatherEntity = weatherDao.getWeather(locationId)
        return liveWeatherEntity.map { weatherEntity ->
            if (weatherEntity != null) {
                return@map Gson().fromJson(
                    weatherEntity.weatherData,
                    WeatherData::class.java
                )
            }
            return@map null
        }

    }

    suspend fun deleteWeather(locationId: Int) {
        weatherDao.deleteById(locationId)
    }

    suspend fun refreshWeather(location: LocationEntity) {
        Log.i(TAG, " Start refreshing weather for locId: ${location.id}")
        val weather =
            weatherApiClient.getOneCall(location.lat, location.lon, language, units, exclude)
                ?.let { WeatherData(it) }
        val weatherEntity =
            WeatherEntity(location.id, location.lat, location.lon, Gson().toJson(weather))
        Log.i(TAG, "End refreshing weather for locId: ${weatherEntity.locationId}")
        weatherDao.insert(weatherEntity)    // TODO if livedata don't work, make update then insert if not already exist
    }

    suspend fun refreshAllWeathers() {
        val allWeathers = weatherDao.getAllWeathers()
        coroutineScope {
            allWeathers.forEach { weatherEntity ->
                launch(Dispatchers.IO) {
                    Log.i(TAG, " Start refreshing weather for locId: ${weatherEntity.locationId}")
                    weatherEntity.weatherData = Gson().toJson(
                        weatherApiClient.getOneCall(
                            weatherEntity.lat,
                            weatherEntity.lon,
                            language,
                            units,
                            exclude
                        )
                            ?.let { WeatherData(it) }
                    )
                    Log.i(TAG, "End refreshing weather for locId: ${weatherEntity.locationId}")
                }
            }
        }
        weatherDao.updateWeathers(allWeathers)
    }

    suspend fun getOldestWeather() : WeatherData? {
        val allWeathers = weatherDao.getAllWeathers().map { weatherEntity ->
            Gson().fromJson(
                weatherEntity.weatherData,
                WeatherData::class.java
            )
        }
        return allWeathers.minByOrNull { it.current.dt }
    }



//    fun getWeather(location: LocationEntity): LocationEntity? {
//        weatherApiClient.getOneCall(location.lat, location.lon, language, units, exclude)?.let {
//            val weatherData = WeatherData(it)
//            run {
//                val updatedLocation = location.copy(
//                    currentWeatherData = Gson().toJson(weatherData.current),
//                    hourlyWeatherData = Gson().toJson(weatherData.hourly),
//                    dailyWeatherData = Gson().toJson(weatherData.daily),
//                    lastUpdated = System.currentTimeMillis()
//                )
//                CoroutineScope(Dispatchers.IO).launch {
//                    locationDao.insert(updatedLocation)
//                }
//                return updatedLocation
//            }
//        }
//        return null
//    }
//
//    suspend fun getSavedFavoriteLocation(locationId: Int): LocationEntity? {
//        return locationDao.getLocation(locationId)
//    }
//
//    suspend fun getAllFavoriteLocations(): List<LocationEntity> {
//        return locationDao.getAllLocations()
//    }


}