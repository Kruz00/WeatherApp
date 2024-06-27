package com.pam_228779.weatherapppro.view.adapter

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.pam_228779.weatherapppro.data.db.entities.LocationEntity
import com.pam_228779.weatherapppro.view.fragments.WeatherFragment


class WeatherPagerAdapter(activity: FragmentActivity) : FragmentStateAdapter(activity) {

    private var locations: List<LocationEntity> = emptyList()

    override fun getItemCount(): Int = locations.size

    override fun getItemId(position: Int): Long {
        Log.i("WeatherPagerAdapter", "getItemId - position: $position, itemId: ${locations[position].id}, name:${locations[position].name}")
        return locations[position].id.toLong()
    }

    override fun containsItem(itemId: Long): Boolean {
        return locations.any { it.id.toLong() == itemId }
    }

    override fun createFragment(position: Int): Fragment {
        val fragment = WeatherFragment()
        fragment.arguments = Bundle().apply {
            putParcelable("locationEntity", locations[position])
            Log.i("WeatherPagerAdapter", "createFragment - position: $position, order: ${locations[position].order}")
        }
        return fragment
    }

    fun submitList(newLocations: List<LocationEntity>) {
        locations = newLocations
        notifyDataSetChanged()
    }
}