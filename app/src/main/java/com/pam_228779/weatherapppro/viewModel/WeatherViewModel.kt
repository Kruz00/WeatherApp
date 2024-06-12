package com.pam_228779.weatherapppro.viewModel

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.CreationExtras
import com.pam_228779.weatherapppro.data.api.WeatherApiClient
import com.pam_228779.weatherapppro.data.db.AppDatabase
import com.pam_228779.weatherapppro.repository.WeatherRepository
import com.pam_228779.weatherapppro.data.db.entities.LocationEntity
import com.pam_228779.weatherapppro.data.db.entities.WeatherEntity
import com.pam_228779.weatherapppro.repository.LocationRepository
import kotlinx.coroutines.launch

class WeatherViewModel(
    private val repository: WeatherRepository
) : ViewModel() {
    fun getWeather(locationId: Int): LiveData<WeatherEntity> {
        return repository.getWeather(locationId)
    }

    fun refreshWeather(location: LocationEntity) {
        viewModelScope.launch {
            repository.refreshWeather(location)
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
                val application = checkNotNull(extras[ViewModelProvider.AndroidViewModelFactory.APPLICATION_KEY])
                val weatherApiClient = WeatherApiClient()
                val weatherDao = AppDatabase.getDatabase(application).weatherDao()
                val weatherRepository = WeatherRepository(weatherDao, weatherApiClient)

                // Create a SavedStateHandle for this ViewModel from extras
                return WeatherViewModel(
                    weatherRepository
                ) as T
            }
        }
    }







//    private val repository: WeatherRepository
//    private val _favouriteLocations = MutableLiveData<List<LocationEntity>>()
//    val favouriteLocations: LiveData<List<LocationEntity>> = _favouriteLocations
//
//    init {
//        val favouriteLocationDao = AppDatabase.getDatabase(application).favouriteLocationDao()
//        val weatherApiClient = WeatherApiClient()
//        repository = WeatherRepository(favouriteLocationDao, weatherApiClient)
//
//        loadFavoriteLocations()
//    }
//
//    private fun loadFavoriteLocations() {
//        viewModelScope.launch {
//            _favouriteLocations.postValue(repository.getAllFavoriteLocations())
//        }
//    }
//
//    fun fetchWeather(location: LocationEntity) {
//        repository.getWeather(location)?.run {
//            loadFavoriteLocations()
//        }
//    }
//
//    fun fetchWeatherForAll() {
//        favouriteLocations.value?.forEach { fetchWeather(it) }
//    }

}