package com.pam_228779.weatherapppro.view.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.pam_228779.weatherapppro.R
import com.pam_228779.weatherapppro.view.adapter.LocationSearchAdapter
import com.pam_228779.weatherapppro.viewModel.LocationViewModel
import com.pam_228779.weatherapppro.viewModel.WeatherViewModel

class AddLocationDialogFragment : DialogFragment() {

    private val locationViewModel: LocationViewModel by activityViewModels()
    private val weatherViewModel: WeatherViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_add_location, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val searchButton: Button = view.findViewById(R.id.searchButton)
//        val addButton: Button = view.findViewById(R.id.addButton)
        val locationInput: EditText = view.findViewById(R.id.locationInput)
        val searchResultsRecyclerView: RecyclerView = view.findViewById(R.id.searchResultsRecyclerView)

        searchResultsRecyclerView.layoutManager = LinearLayoutManager(requireContext())

        searchButton.setOnClickListener {
            val locationName = locationInput.text.toString()
            locationViewModel.searchLocations(locationName)
        }

        locationViewModel.searchResults.observe(viewLifecycleOwner, Observer {locations ->
            searchResultsRecyclerView.adapter = LocationSearchAdapter(locations) { selectedLocation ->
                locationViewModel.addLocation(selectedLocation)
//              weatherViewModel.refreshWeather(locationEntity)

//                addButton.visibility = View.VISIBLE
//                addButton.setOnClickListener {
//                    locationViewModel.addLocation(selectedLocation)
////                    weatherViewModel.refreshWeather(locationEntity)
//                    dismiss()
//                }
            }
        })

//        addButton.setOnClickListener {
//            val selectedLocation: Location = // get selected location from your RecyclerView
//
//            val locationEntity = locationViewModel.addLocation(selectedLocation)
//            weatherViewModel.refreshWeather(locationEntity)
//            dismiss()
//        }
    }
}