package com.pam_228779.weatherapppro.view.activity

import android.os.Bundle
import android.util.Log
import android.view.MenuItem
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.ItemTouchHelper.ACTION_STATE_IDLE
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.pam_228779.weatherapppro.R
import com.pam_228779.weatherapppro.data.db.entities.LocationEntity
import com.pam_228779.weatherapppro.view.adapter.ManageLocationsAdapter
import com.pam_228779.weatherapppro.view.fragments.AddLocationDialogFragment
import com.pam_228779.weatherapppro.viewModel.LocationViewModel
import com.pam_228779.weatherapppro.viewModel.WeatherViewModel


class ManageLocationsActivity : AppCompatActivity() {
    private val TAG = "ManageLocationsActivity"

    private val locationViewModel: LocationViewModel by viewModels { LocationViewModel.Factory }
    private val weatherViewModel: WeatherViewModel by viewModels { WeatherViewModel.Factory }
    private lateinit var locationsAdapter: ManageLocationsAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_manage_locations)

        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        val recyclerView = findViewById<RecyclerView>(R.id.manageLocationRecyclerView)
        locationsAdapter =
            ManageLocationsAdapter { location ->
                locationViewModel.deleteLocation(location)
                locationViewModel.updateLocationOrder(locationsAdapter.currentList)
            }
        recyclerView.adapter = locationsAdapter
        recyclerView.layoutManager = LinearLayoutManager(this)

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
                Log.i(TAG, "onMove - item move from $fromPosition to $toPosition")
                locationsAdapter.moveItem(fromPosition, toPosition)
                return true
            }

            override fun onSelectedChanged(
                viewHolder: RecyclerView.ViewHolder?,
                actionState: Int
            ) {
                super.onSelectedChanged(viewHolder, actionState)
                Log.i(TAG, "onSelectedChanged - item moved")
                if(actionState == ACTION_STATE_IDLE) {
                    locationViewModel.updateLocationOrder(locationsAdapter.currentList)
                    Log.i(TAG, "onSelectedChanged - update order")
                }
            }

            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                val position = viewHolder.adapterPosition
                val location = locationsAdapter.getLocationAtPosition(position)
                locationViewModel.deleteLocation(location)
                Log.i(TAG, "onSwiped - deleted location: ${location.name}")
            }
        }

        val itemTouchHelper = ItemTouchHelper(itemTouchHelperCallback)
        itemTouchHelper.attachToRecyclerView(recyclerView)

        locationViewModel.allLocations.observe(this, Observer { locations ->
            locations?.let { locationsAdapter.submitList(it) }
            Log.i(TAG, "location observer - submit new list")
        })

        val fab = findViewById<FloatingActionButton>(R.id.fab)
        fab.setOnClickListener {
            AddLocationDialogFragment().show(supportFragmentManager, "AddLocationDialogFragment")
        }

        Log.i(TAG, "$TAG Activity created")


        // Setup RecyclerView and adapter to display and manage locations
    }

    override fun onPause() {
        super.onPause()
        Log.i(TAG, "$TAG Activity paused")
        // update location list order
//        val currentList: MutableList<LocationEntity> = locationsAdapter.currentList
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