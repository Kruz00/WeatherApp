package com.pam_228779.weatherapppro.repository

import androidx.lifecycle.LiveData
import com.pam_228779.weatherapppro.data.api.WeatherApiClient
import com.pam_228779.weatherapppro.data.model.Location
import com.pam_228779.weatherapppro.data.db.dao.LocationDao
import com.pam_228779.weatherapppro.data.db.entities.LocationEntity
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class LocationRepository(
    private val locationDao: LocationDao,
    private val weatherApiClient: WeatherApiClient
) {
    val allLocations: LiveData<List<LocationEntity>> = locationDao.getAllLocations()

    suspend fun addLocation(location: Location) {
        locationDao.insert(locationToEntity(location))
    }

    suspend fun deleteLocation(location: LocationEntity) {
        locationDao.delete(location)
    }


    suspend fun searchLocations(query: String): List<Location>? {
        return withContext(Dispatchers.IO) {
            weatherApiClient.getLocations(query, 5)
        }
    }

    private fun locationToEntity(location: Location) : LocationEntity {
        return LocationEntity(
            id = 0, // 0 or null so Room will auto increment id
            name = location.name,
            lat = location.lat,
            lon = location.lon,
            country = location.country,
            state = location.state
        )
    }


}