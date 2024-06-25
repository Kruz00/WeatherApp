package com.pam_228779.weatherapppro.viewModel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProvider.AndroidViewModelFactory.Companion.APPLICATION_KEY
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.CreationExtras
import com.pam_228779.weatherapppro.data.api.WeatherApiClient
import com.pam_228779.weatherapppro.data.db.AppDatabase
import com.pam_228779.weatherapppro.data.db.entities.LocationEntity
import com.pam_228779.weatherapppro.data.model.Location
import com.pam_228779.weatherapppro.repository.LocationRepository
import com.pam_228779.weatherapppro.utils.NetworkUtils
import kotlinx.coroutines.launch

class LocationViewModel(
    private val repository: LocationRepository,
    private val networkUtils: NetworkUtils
) : ViewModel() {

    val allLocations: LiveData<List<LocationEntity>> = repository.allLocations

    private val _searchResults = MutableLiveData<List<Location>>()
    val searchResults: LiveData<List<Location>> get() = _searchResults

    private val _userMessage = MutableLiveData<String>()
    val userMessage: LiveData<String> get() = _userMessage

    fun addLocation(location: Location) {
        viewModelScope.launch {
            repository.addLocation(location)
        }
    }

    fun deleteLocation(location: LocationEntity) {
        viewModelScope.launch {
            repository.deleteLocation(location)
        }
    }

    fun updateLocationOrder(newOrderedList: List<LocationEntity>) {
        viewModelScope.launch {
            repository.updateLocationOrder(newOrderedList)
        }
    }

    fun searchLocations(query: String) {
        viewModelScope.launch {
            if(networkUtils.isInternetAvailable()) {
                val results = repository.searchLocations(query)
                _searchResults.postValue(results)
            } else {
                _userMessage.postValue("No internet connection!")
            }
        }
    }

    fun resetSearchLocations() {
        viewModelScope.launch {
            _searchResults.postValue(emptyList())
        }
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
                val networkUtils = NetworkUtils(application.applicationContext)

                // Create a SavedStateHandle for this ViewModel from extras
                return LocationViewModel(
                    locationRepository,
                    networkUtils
                ) as T
            }
        }
    }
}