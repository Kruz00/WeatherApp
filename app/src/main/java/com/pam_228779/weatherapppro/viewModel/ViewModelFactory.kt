package com.pam_228779.weatherapppro.viewModel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.pam_228779.weatherapppro.repository.LocationRepository
import com.pam_228779.weatherapppro.repository.WeatherRepository


class ViewModelFactory(
    private val locationRepository: LocationRepository?,
    private val weatherRepository: WeatherRepository?
) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(LocationViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return locationRepository?.let { LocationViewModel(it) } as T
        }
        if (modelClass.isAssignableFrom(WeatherViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return weatherRepository?.let { WeatherViewModel(it) } as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
