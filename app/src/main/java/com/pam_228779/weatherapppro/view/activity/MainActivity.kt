package com.pam_228779.weatherapppro.view.activity

import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.viewpager2.widget.ViewPager2
import com.pam_228779.weatherapppro.R
import com.pam_228779.weatherapppro.data.api.WeatherApiClient
import com.pam_228779.weatherapppro.data.db.AppDatabase
import com.pam_228779.weatherapppro.repository.LocationRepository
import com.pam_228779.weatherapppro.repository.WeatherRepository
import com.pam_228779.weatherapppro.view.adapter.WeatherPagerAdapter
import com.pam_228779.weatherapppro.viewModel.LocationViewModel
import com.pam_228779.weatherapppro.viewModel.ViewModelFactory
import com.pam_228779.weatherapppro.viewModel.WeatherViewModel


class MainActivity : AppCompatActivity() {

    private lateinit var viewPager: ViewPager2
    private lateinit var adapter: WeatherPagerAdapter
    private val locationViewModel: LocationViewModel  by viewModels { LocationViewModel.Factory }
    private val weatherViewModel: WeatherViewModel by viewModels { WeatherViewModel.Factory }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        viewPager = findViewById(R.id.viewPager)

//        val weatherApiClient = WeatherApiClient()
//        val locationDao = AppDatabase.getDatabase(application).locationDao()
//        val weatherDao = AppDatabase.getDatabase(application).weatherDao()
//        val locationRepository = LocationRepository(locationDao, weatherApiClient)
//        val weatherRepository = WeatherRepository(weatherDao, weatherApiClient)
//        val viewModelFactory = ViewModelFactory(locationRepository, weatherRepository)
//
//        locationViewModel = ViewModelProvider(this, viewModelFactory).get(LocationViewModel::class.java)
//        weatherViewModel = ViewModelProvider(this, viewModelFactory).get(WeatherViewModel::class.java)

        adapter = WeatherPagerAdapter(this)
        viewPager.adapter = adapter

        // Observe locations and update adapter
        locationViewModel.allLocations.observe(this, Observer { locations ->
            locations?.let {
                adapter.submitList(it)
            }
        })
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.main_menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.menu_settings -> {
                startActivity(Intent(this, SettingsActivity::class.java))
                true
            }
            R.id.manage_locations -> {
                val intent = Intent(this, ManageLocationsActivity::class.java)
                startActivity(intent)
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }
}
