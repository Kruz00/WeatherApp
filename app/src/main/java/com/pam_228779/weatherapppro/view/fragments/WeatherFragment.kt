package com.pam_228779.weatherapppro.view.fragments

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.pam_228779.weatherapppro.R
import com.pam_228779.weatherapppro.data.db.entities.LocationEntity
import com.pam_228779.weatherapppro.view.adapter.DailyForecastAdapter
import com.pam_228779.weatherapppro.view.adapter.HourlyForecastAdapter
import com.pam_228779.weatherapppro.viewModel.WeatherViewModel

class WeatherFragment : Fragment() {

    private val weatherViewModel: WeatherViewModel by activityViewModels()
    private lateinit var location: LocationEntity

    private lateinit var hourlyRecyclerView: RecyclerView
    private lateinit var dailyRecyclerView: RecyclerView

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        val view = inflater.inflate(R.layout.fragment_weather, container, false)

        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        // TODO ogarnac reset przy obrocie ekranu
        super.onViewCreated(view, savedInstanceState)

        hourlyRecyclerView = view.findViewById(R.id.hourly_forecast_recyclerview)
        dailyRecyclerView = view.findViewById(R.id.daily_forecast_recyclerview)


        location = arguments?.getParcelable("locationEntity")!!

        val hourlyLayoutManager =
            LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
        hourlyRecyclerView.layoutManager = hourlyLayoutManager

        val dailyLayoutManager = LinearLayoutManager(context)
        dailyRecyclerView.layoutManager = dailyLayoutManager

        val currentWeatherFragment = CurrentWeatherFragment()
        currentWeatherFragment.arguments = Bundle().apply {
            putParcelable("locationEntity", location)
        }
        childFragmentManager.beginTransaction()
            .replace(R.id.currentWeatherContainer, currentWeatherFragment)
            .commit()

        weatherViewModel.getWeather(location).observe(viewLifecycleOwner) { weatherData ->
            // Setup hourly forecast RecyclerView
            val hourlyAdapter = HourlyForecastAdapter(requireContext(), weatherData)
            hourlyRecyclerView.adapter = hourlyAdapter

            // Setup daily forecast RecyclerView
            val dailyAdapter = DailyForecastAdapter(requireContext(), weatherData)
            dailyRecyclerView.adapter = dailyAdapter
        }

        Log.i("WeatherFragment", "onViewCreated - order: ${location.order}")
    }
}