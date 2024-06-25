package com.pam_228779.weatherapppro.view.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.pam_228779.weatherapppro.R
import com.pam_228779.weatherapppro.data.model.Location

class LocationSearchAdapter(
    private val locations: List<Location>,
    private val onItemClick: (Location) -> Unit
) : RecyclerView.Adapter<LocationSearchAdapter.LocationViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): LocationViewHolder {
        val itemView = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_search_location, parent, false)
        return LocationViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: LocationViewHolder, position: Int) {
        val current = locations[position]
        holder.bind(current)
    }

    override fun getItemCount() = locations.size

    inner class LocationViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        fun bind(location: Location) {
            itemView.findViewById<TextView>(R.id.searchLocationName).text = location.name
            itemView.findViewById<TextView>(R.id.searchLocationInfo).text = "${location.state}, ${location.country}"
            itemView.setOnClickListener { onItemClick(location) }
        }
    }
}