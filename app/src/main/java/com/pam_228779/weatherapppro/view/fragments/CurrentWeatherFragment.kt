package com.pam_228779.weatherapppro.view.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import com.pam_228779.weatherapppro.R
import com.pam_228779.weatherapppro.data.db.entities.LocationEntity
import com.pam_228779.weatherapppro.data.model.WeatherData
import com.pam_228779.weatherapppro.utils.speedUnit
import com.pam_228779.weatherapppro.utils.temperatureUnit
import com.pam_228779.weatherapppro.viewModel.WeatherViewModel
import kotlin.math.roundToInt

class CurrentWeatherFragment : Fragment() {

    private val weatherViewModel: WeatherViewModel by activityViewModels()
    private lateinit var location: LocationEntity

    private lateinit var locationNameTextView: TextView
    private lateinit var weatherDescription: TextView
    private lateinit var currentTemperature: TextView
    private lateinit var windSpeed: TextView
    private lateinit var pressure: TextView
    private lateinit var feelsLikeTemperature: TextView

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_current_weather, container, false)

        // Initialize UI elements
        locationNameTextView = view.findViewById(R.id.current_locationName)
        weatherDescription = view.findViewById(R.id.current_weatherDescription)
        currentTemperature = view.findViewById(R.id.current_temperature)
        windSpeed = view.findViewById(R.id.current_wind_speed)
        pressure = view.findViewById(R.id.current_pressure)
        feelsLikeTemperature = view.findViewById(R.id.current_feels_like_temperature)

        location = arguments?.getParcelable("locationEntity")!!

        locationNameTextView.text = location.name

        // Observe the weather data
        weatherViewModel.getWeather(location).observe(viewLifecycleOwner) { weatherData ->
            weatherData?.let {
                updateUI(it)
            }
        }

        return view
    }

    private fun updateUI(weatherData: WeatherData) {
        weatherDescription.text = weatherData.current.weather.first().description
        currentTemperature.text = "${weatherData.current.temp.roundToInt()}${temperatureUnit[weatherData.units]}"
        windSpeed.text = getString(R.string.wind_speed_text, weatherData.current.wind_speed, speedUnit[weatherData.units])
        pressure.text = getString(R.string.pressure_text, weatherData.current.pressure, "hPa")
        feelsLikeTemperature.text = getString(R.string.feels_like_text, weatherData.current.feels_like.roundToInt(), temperatureUnit[weatherData.units])
    }
}