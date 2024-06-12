package com.pam_228779.weatherapppro.view.activity

import android.os.Bundle
import android.view.MenuItem
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.pam_228779.weatherapppro.R
import com.pam_228779.weatherapppro.view.adapter.LocationAdapter
import com.pam_228779.weatherapppro.view.fragments.AddLocationDialogFragment
import com.pam_228779.weatherapppro.viewModel.LocationViewModel
import com.pam_228779.weatherapppro.viewModel.WeatherViewModel

class ManageLocationsActivity : AppCompatActivity() {

    private val locationViewModel: LocationViewModel  by viewModels { LocationViewModel.Factory }
    private val weatherViewModel: WeatherViewModel  by viewModels { WeatherViewModel.Factory }
    private lateinit var locationAdapter: LocationAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_manage_locations)

        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        val recyclerView = findViewById<RecyclerView>(R.id.recyclerview)
        locationAdapter = LocationAdapter { location -> locationViewModel.deleteLocation(location) }
        recyclerView.adapter = locationAdapter
        recyclerView.layoutManager = LinearLayoutManager(this)

        // ItemTouchHelper for drag-and-drop and swipe-to-delete
        val itemTouchHelperCallback = object : ItemTouchHelper.SimpleCallback(
            ItemTouchHelper.UP or ItemTouchHelper.DOWN,
            ItemTouchHelper.LEFT or ItemTouchHelper.RIGHT
        ) {
            override fun onMove(
                recyclerView: RecyclerView,
                viewHolder: RecyclerView.ViewHolder,
                target: RecyclerView.ViewHolder
            ): Boolean {
                val fromPosition = viewHolder.adapterPosition
                val toPosition = target.adapterPosition
                locationAdapter.moveItem(fromPosition, toPosition)
                return true
            }

            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                val position = viewHolder.adapterPosition
                val location = locationAdapter.getLocationAtPosition(position)
                locationViewModel.deleteLocation(location)
            }
        }

        val itemTouchHelper = ItemTouchHelper(itemTouchHelperCallback)
        itemTouchHelper.attachToRecyclerView(recyclerView)

        locationViewModel.allLocations.observe(this, Observer { locations ->
            locations?.let { locationAdapter.submitList(it) }
        })

        val fab = findViewById<FloatingActionButton>(R.id.fab)
        fab.setOnClickListener {
            AddLocationDialogFragment().show(supportFragmentManager, "AddLocationDialogFragment")
        }



        // Setup RecyclerView and adapter to display and manage locations
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            android.R.id.home -> {
                onBackPressed()
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return true
    }

}