package com.pam_228779.weatherapppro.viewModel

import android.content.SharedPreferences
import android.util.Log
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


private const val LAST_WEATHERS_UPDATE_KEY = "lastWeathersUpdate"

private const val REFRESH_INTERVAL_KEY = "refresh_interval"

class WeatherViewModel(
    private val repository: WeatherRepository,
    private val preferences: SharedPreferences,
    private val networkUtils: NetworkUtils,
) : ViewModel() {
    private val TAG = "WeatherViewModel"

    private val _userMessage = MutableLiveData<String>()
    val userMessage: LiveData<String> get() = _userMessage

    init {
        Log.i(TAG, "Init $TAG")
        refreshAllWeathers()
    }


    fun getWeather(location: LocationEntity): LiveData<WeatherData?> {
        val weatherData = repository.getLiveWeather(location.id)
        if (weatherData.value == null) {
//            Log.i(TAG, "Weather for new locId: ${location.id}")
            updateIfNoWeather(location)
        }
        return weatherData
    }

    fun deleteWeather(locationId: Int) {
        viewModelScope.launch {
            repository.deleteWeather(locationId)
        }
    }

    private fun updateIfNoWeather(location: LocationEntity) {
        viewModelScope.launch {
            withContext(Dispatchers.IO) {
                if (!repository.isWeatherForLocation(location)) {
                    Log.i(TAG, "Weather for new locId: ${location.id}")
                    if (networkUtils.isInternetAvailable()) {
                        repository.newWeatherLocation(
                            location,
                            preferences.getString("units", "metric")!!
                        )
                    } else {
                        _userMessage.postValue("No internet connection")
                    }
                }
            }
        }
    }

    private fun refreshAllWeathers() {
        // check last update time and refresh
        viewModelScope.launch {
            val lastUpdate = preferences.getLong(LAST_WEATHERS_UPDATE_KEY, 0)
            val lastUpdateInstant = Instant.ofEpochSecond(lastUpdate)
            val minutesToRefresh =
                preferences.getString(REFRESH_INTERVAL_KEY, MINUTES_TO_REFRESH_DEFAULT.toString())!!.toLong()
            val now = Instant.now()
            Log.i(TAG, "lastRefresh: ${lastUpdateInstant}\nminutesToRefresh: $minutesToRefresh\nInstantNow: $now")
            if (now.isAfter(lastUpdateInstant.plus(minutesToRefresh, ChronoUnit.MINUTES))) {
                Log.i(TAG, "Refreshing weathers")
                withContext(Dispatchers.IO) {
                    if (networkUtils.isInternetAvailable()) {
                        repository.refreshAllWeathers(
                            preferences.getString(
                                "units",
                                "metric"
                            )!!
                        )
                        preferences.edit().putLong(LAST_WEATHERS_UPDATE_KEY, now.epochSecond)
                            .apply()
                        _userMessage.postValue("Forecasts updated!")
                    } else {
                        _userMessage.postValue("No internet connection, forecast may be outdated")
                    }
                }
            }
        }
    }

    fun forceRefreshAllWeathers() {
        viewModelScope.launch {
            if (networkUtils.isInternetAvailable()) {
                withContext(Dispatchers.IO) {
                    repository.refreshAllWeathers(preferences.getString("units", "metric")!!)
                }
                preferences.edit().putLong(LAST_WEATHERS_UPDATE_KEY, Instant.now().epochSecond)
                    .apply()
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

                Log.i("WeatherViewModel.Factory", "WeatherViewModel creating")

                // Create a SavedStateHandle for this ViewModel from extras
                return WeatherViewModel(
                    weatherRepository,
                    sharedPreferences,
                    networkUtils
                ) as T
            }
        }
    }
}