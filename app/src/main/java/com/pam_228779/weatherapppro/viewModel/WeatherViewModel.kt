package com.pam_228779.weatherapppro.viewModel

import android.content.SharedPreferences
import android.util.Log
import android.widget.Toast
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.CreationExtras
import androidx.preference.PreferenceManager
import com.pam_228779.weatherapppro.data.api.WeatherApiClient
import com.pam_228779.weatherapppro.data.db.AppDatabase
import com.pam_228779.weatherapppro.data.db.entities.LocationEntity
import com.pam_228779.weatherapppro.data.model.WeatherData
import com.pam_228779.weatherapppro.repository.WeatherRepository
import com.pam_228779.weatherapppro.utils.NetworkUtils
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.time.Instant
import java.time.temporal.ChronoUnit

private const val MINUTES_TO_REFRESH_DEFAULT = 15


class WeatherViewModel(
    private val repository: WeatherRepository,
    private val preferences: SharedPreferences,
    private val networkUtils: NetworkUtils
) : ViewModel() {
    private val TAG = "WeatherViewModel"
    private val weatherDataMap = mutableMapOf<Int, LiveData<WeatherData?>>()

    private val _userMessage = MutableLiveData<String>()
    val userMessage: LiveData<String> get() = _userMessage

    fun getWeather(location: LocationEntity): LiveData<WeatherData?> {
        return weatherDataMap.getOrPut(location.id) {
            val weatherData = repository.getWeather(location.id)
            if (weatherData.value == null) {
                Log.i(TAG, "Weather for new locId: ${location.id}")
                refreshWeather(location)
            }
            return weatherData

        }
    }

    fun deleteWeather(locationId: Int) {
        viewModelScope.launch {
            repository.deleteWeather(locationId)
        }
    }

    fun refreshWeather(location: LocationEntity) {
        viewModelScope.launch {
            if(networkUtils.isInternetAvailable()) {
                withContext(Dispatchers.IO) {
                    repository.refreshWeather(location)
                }
            } else {
                _userMessage.postValue("No internet connection")
            }
        }

    }

    fun refreshAllWeathers() {
        // check last update time and refresh
        viewModelScope.launch {
            withContext(Dispatchers.IO) {
                val oldestWeather = repository.getOldestWeather()
                if (oldestWeather != null) {
                    val lastRefreshInstant =
                        Instant.ofEpochSecond(oldestWeather.current.dt.toLong())
                    val minutesToRefresh =
                        preferences.getString("refresh_interval",
                            MINUTES_TO_REFRESH_DEFAULT.toString()
                        )!!.toLong()
                    val now = Instant.now()
                    Log.i(TAG, "lastRefresh: ${lastRefreshInstant}\nminutesToRefresh: $minutesToRefresh\nInstantNow: $now"
                    )
                    if (now.isAfter(lastRefreshInstant.plus(minutesToRefresh, ChronoUnit.MINUTES))
                    ) {
                        if(networkUtils.isInternetAvailable()) {
                            repository.refreshAllWeathers()
                            _userMessage.postValue("Forecasts updated!")
                        } else {
                            _userMessage.postValue("No internet connection, forecast may be not accurate")
                        }
                    }
                }
            }
        }
    }

    fun forceRefreshAllWeathers() {
        viewModelScope.launch {
            if(networkUtils.isInternetAvailable()) {
                withContext(Dispatchers.IO) {
                    repository.refreshAllWeathers()
                }
                _userMessage.postValue("Forecasts updated!!")
            } else {
                _userMessage.postValue("No internet connection!")
            }
        }

    }

    companion object {

        val Factory: ViewModelProvider.Factory = object : ViewModelProvider.Factory {
            @Suppress("UNCHECKED_CAST")
            override fun <T : ViewModel> create(
                modelClass: Class<T>,
                extras: CreationExtras,
            ): T {
                // Get the Application object from extras
                val application =
                    checkNotNull(extras[ViewModelProvider.AndroidViewModelFactory.APPLICATION_KEY])
                val weatherApiClient = WeatherApiClient()
                val weatherDao = AppDatabase.getDatabase(application).weatherDao()
                val weatherRepository = WeatherRepository(weatherDao, weatherApiClient)
                val sharedPreferences =
                    PreferenceManager.getDefaultSharedPreferences(application.applicationContext)
                val networkUtils = NetworkUtils(application.applicationContext)

                // Create a SavedStateHandle for this ViewModel from extras
                return WeatherViewModel(
                    weatherRepository,
                    sharedPreferences,
                    networkUtils
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