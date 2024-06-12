package com.pam_228779.weatherapppro.repository

import androidx.lifecycle.LiveData
import com.pam_228779.weatherapppro.data.api.WeatherApiClient
import com.pam_228779.weatherapppro.data.model.Location
import com.pam_228779.weatherapppro.data.db.dao.LocationDao
import com.pam_228779.weatherapppro.data.db.entities.LocationEntity

class LocationRepository(
    private val locationDao: LocationDao,
    private val weatherApiClient: WeatherApiClient
) {
    val allLocations: LiveData<List<LocationEntity>> = locationDao.getAllLocations()

    suspend fun addLocation(location: LocationEntity) {
        locationDao.insert(location)
    }

    suspend fun deleteLocation(location: LocationEntity) {
        locationDao.delete(location)
    }


    fun searchLocations(query: String): List<Location>? {
        return weatherApiClient.getLocations(query, 5)
    }


}