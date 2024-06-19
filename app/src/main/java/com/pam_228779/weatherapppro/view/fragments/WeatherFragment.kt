package com.pam_228779.weatherapppro.view.fragments

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.pam_228779.weatherapppro.R
import com.pam_228779.weatherapppro.data.db.entities.LocationEntity
import com.pam_228779.weatherapppro.view.adapter.HourlyWeatherAdapter
import com.pam_228779.weatherapppro.viewModel.WeatherViewModel

class WeatherFragment : Fragment() {

    private val weatherViewModel: WeatherViewModel by activityViewModels()
    private lateinit var location: LocationEntity

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_weather, container, false)

        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val locationNameTextView: TextView = view.findViewById(R.id.WeatherLocationName)
        val hourlyRecyclerView: RecyclerView = view.findViewById(R.id.hourly_forecast_recyclerview)

        val locationEntity: LocationEntity? = arguments?.getParcelable("locationEntity")
        locationNameTextView.text = locationEntity?.name
        Log.i("WeatherFragment", "onViewCreated - order: ${locationEntity?.order}")


//        hourlyRecyclerView.layoutManager = LinearLayoutManager(requireContext(), RecyclerView.HORIZONTAL, false)
//        val weatherAdapter = HourlyWeatherAdapter()
//        hourlyRecyclerView.adapter = weatherAdapter
//
//        weatherViewModel.weatherData.observe(viewLifecycleOwner, Observer { weather ->
//            weather?.let {
//                weatherAdapter.submitList(it)
//            }
//        })
//
//        // Fetch weather data for the provided locationId
//        val locationId = arguments?.getInt("locationId") ?: 0
//        weatherViewModel.getWeather(locationId)
    }
}