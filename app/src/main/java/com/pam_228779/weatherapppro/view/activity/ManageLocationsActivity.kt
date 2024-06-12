package com.pam_228779.weatherapppro.view.activity

import android.os.Bundle
import android.view.MenuItem
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.RecyclerView
import com.pam_228779.weatherapppro.R
import com.pam_228779.weatherapppro.data.api.WeatherApiClient
import com.pam_228779.weatherapppro.data.db.AppDatabase
import com.pam_228779.weatherapppro.repository.LocationRepository
import com.pam_228779.weatherapppro.repository.WeatherRepository
import com.pam_228779.weatherapppro.viewModel.LocationViewModel
import com.pam_228779.weatherapppro.viewModel.ViewModelFactory

class ManageLocationsActivity : AppCompatActivity() {

    private val locationViewModel: LocationViewModel  by viewModels { LocationViewModel.Factory }
    private lateinit var locationAdapter: LocationAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_manage_locations)

        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        val recyclerView = findViewById<RecyclerView>(R.id.recyclerview)
        locationAdapter = LocationAdapter { location -> locationViewModel.deleteLocation(location) }
        recyclerView.adapter = locationAdapter
        recyclerView.layoutManager = LinearLayoutManager(this)

        locationViewModel.allLocations.observe(this) { locations ->
//            adapter.submitList(locations)
        }

        // Setup RecyclerView and adapter to display and manage locations
    }

}