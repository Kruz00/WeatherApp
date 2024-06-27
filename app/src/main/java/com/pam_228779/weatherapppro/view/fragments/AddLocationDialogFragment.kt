package com.pam_228779.weatherapppro.view.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.viewModelScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.pam_228779.weatherapppro.R
import com.pam_228779.weatherapppro.view.adapter.LocationSearchAdapter
import com.pam_228779.weatherapppro.viewModel.LocationViewModel
import com.pam_228779.weatherapppro.viewModel.WeatherViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.launch

class AddLocationDialogFragment : DialogFragment() {

    private val locationViewModel: LocationViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        return inflater.inflate(R.layout.fragment_add_location, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val searchButton: Button = view.findViewById(R.id.searchButton)
        val locationInput: EditText = view.findViewById(R.id.locationInput)
        val searchResultsRecyclerView: RecyclerView =
            view.findViewById(R.id.searchResultsRecyclerView)

        searchResultsRecyclerView.layoutManager = LinearLayoutManager(requireContext())

        searchButton.setOnClickListener {
            val locationName = locationInput.text.toString()
            locationViewModel.searchLocations(locationName)
        }

        locationViewModel.searchResults.observe(viewLifecycleOwner) { locations ->
            searchResultsRecyclerView.adapter =
                LocationSearchAdapter(locations) { selectedLocation ->
                    locationViewModel.addLocation(selectedLocation)
                    dismiss()
                }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        locationViewModel.resetSearchLocations()
    }
}