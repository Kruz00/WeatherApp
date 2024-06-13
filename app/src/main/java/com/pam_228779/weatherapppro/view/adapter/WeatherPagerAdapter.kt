package com.pam_228779.weatherapppro.view.adapter

import android.os.Bundle
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.pam_228779.weatherapppro.data.db.entities.LocationEntity
import com.pam_228779.weatherapppro.view.fragments.WeatherFragment


class WeatherPagerAdapter(activity: FragmentActivity) : FragmentStateAdapter(activity) {

    private var locations: List<LocationEntity> = emptyList()

    override fun getItemCount(): Int = locations.size

    override fun createFragment(position: Int): Fragment {
        val fragment = WeatherFragment()
        fragment.arguments = Bundle().apply {
//            putParcelable("location", locations[position])
            putParcelable("locationEntity", locations[position])
        }
        return fragment
    }

    fun submitList(newLocations: List<LocationEntity>) {
        locations = newLocations
        notifyDataSetChanged()
    }
}