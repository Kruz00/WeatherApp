package com.pam_228779.weatherapppro.viewModel

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProvider.AndroidViewModelFactory.Companion.APPLICATION_KEY
import androidx.lifecycle.liveData
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.CreationExtras
import com.pam_228779.weatherapppro.data.api.WeatherApiClient
import com.pam_228779.weatherapppro.data.db.AppDatabase
import com.pam_228779.weatherapppro.data.db.entities.LocationEntity
import com.pam_228779.weatherapppro.data.model.Location
import com.pam_228779.weatherapppro.repository.LocationRepository
import kotlinx.coroutines.launch

class LocationViewModel(
    private val repository: LocationRepository
) : ViewModel() {
    val allLocations: LiveData<List<LocationEntity>> = repository.allLocations

    fun addLocation(location: LocationEntity) {
        viewModelScope.launch {
            repository.addLocation(location)
        }
    }

    fun deleteLocation(location: LocationEntity) {
        viewModelScope.launch {
            repository.deleteLocation(location)
        }
    }

    fun searchLocations(query: String): LiveData<List<Location>> = liveData {
        repository.searchLocations(query)?.let { emit(it) }
    }

    companion object {

        val Factory: ViewModelProvider.Factory = object : ViewModelProvider.Factory {
            @Suppress("UNCHECKED_CAST")
            override fun <T : ViewModel> create(
                modelClass: Class<T>,
                extras: CreationExtras
            ): T {
                // Get the Application object from extras
                val application = checkNotNull(extras[APPLICATION_KEY])
                val weatherApiClient = WeatherApiClient()
                val locationDao = AppDatabase.getDatabase(application).locationDao()
                val locationRepository = LocationRepository(locationDao, weatherApiClient)

                // Create a SavedStateHandle for this ViewModel from extras
                return LocationViewModel(
                    locationRepository
                ) as T
            }
        }
    }
}