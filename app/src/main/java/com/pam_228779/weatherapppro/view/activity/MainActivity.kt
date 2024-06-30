package com.pam_228779.weatherapppro.view.activity

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.preference.PreferenceManager
import androidx.viewpager2.widget.ViewPager2
import com.pam_228779.weatherapppro.R
import com.pam_228779.weatherapppro.view.adapter.WeatherPagerAdapter
import com.pam_228779.weatherapppro.viewModel.LocationViewModel
import com.pam_228779.weatherapppro.viewModel.WeatherViewModel


class MainActivity : AppCompatActivity() {
    private val TAG = "MainActivity"

    private lateinit var viewPager: ViewPager2
    private lateinit var adapter: WeatherPagerAdapter
    private val locationViewModel: LocationViewModel by viewModels { LocationViewModel.Factory }
    private val weatherViewModel: WeatherViewModel by viewModels { WeatherViewModel.Factory }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        viewPager = findViewById(R.id.mainViewPager)

        PreferenceManager.setDefaultValues(this, R.xml.preferences, true)

        adapter = WeatherPagerAdapter(this)
        viewPager.adapter = adapter

        locationViewModel.allLocations.observe(this) { locations ->
            locations?.let {
                adapter.submitList(it)
                Log.i(TAG, "locations updated by observator")
            }
        }

        weatherViewModel.userMessage.observe(this) { message ->
            Toast.makeText(applicationContext, message, Toast.LENGTH_SHORT).show()
        }
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

            R.id.refresh_weather -> {
                weatherViewModel.refreshAllWeathers()
                true
            }

            else -> super.onOptionsItemSelected(item)
        }
    }

    override fun onPause() {
        super.onPause()
        Log.i(TAG, "$TAG Activity paused")
    }

    override fun onResume() {
        super.onResume()
        Log.i(TAG, "$TAG Activity resumed")
    }


}
