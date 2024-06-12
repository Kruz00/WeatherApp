package com.pam_228779.weatherapppro.repository

import androidx.lifecycle.LiveData
import com.google.gson.Gson
import com.pam_228779.weatherapppro.data.model.WeatherData
import com.pam_228779.weatherapppro.data.api.WeatherApiClient
import com.pam_228779.weatherapppro.data.db.dao.WeatherDao
import com.pam_228779.weatherapppro.data.db.entities.LocationEntity
import com.pam_228779.weatherapppro.data.db.entities.WeatherEntity

class WeatherRepository(
    private val weatherDao: WeatherDao,
    private val weatherApiClient: WeatherApiClient
) {
    private val language = "pl"
    private val units = "metric"
    private val exclude = "minutely,alerts"


    fun getWeather(locationId: Int): LiveData<WeatherEntity> {
        return weatherDao.getWeather(locationId)
    }

    suspend fun refreshWeather(location: LocationEntity) {
        val weather =
            weatherApiClient.getOneCall(location.lat, location.lon, language, units, exclude)
                ?.let { WeatherData(it) }
        val weatherEntity = WeatherEntity(location.id, Gson().toJson(weather))
        weatherDao.insert(weatherEntity)    // TODO if livedata don't work, make update then insert if not already exist
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