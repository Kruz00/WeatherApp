package com.pam_228779.weatherapppro.view.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.TextView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.pam_228779.weatherapppro.R
import com.pam_228779.weatherapppro.data.db.entities.LocationEntity


class ManageLocationsAdapter(private val onDeleteClick: (LocationEntity) -> Unit) :
    ListAdapter<LocationEntity, ManageLocationsAdapter.LocationViewHolder>(LocationsComparator()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): LocationViewHolder {
        val itemView = LayoutInflater.from(parent.context)
            .inflate(R.layout.location_item, parent, false)
        return LocationViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: LocationViewHolder, position: Int) {
        val current = getItem(position)
        holder.bind(current)
    }

    fun getLocationAtPosition(position: Int): LocationEntity {
        return getItem(position)
    }

    fun moveItem(fromPosition: Int, toPosition: Int) {
        val updatedList = currentList.toMutableList()
        val item = updatedList.removeAt(fromPosition)
        updatedList.add(toPosition, item)
        submitList(updatedList)
    }

    inner class LocationViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        fun bind(location: LocationEntity) {
            itemView.findViewById<TextView>(R.id.locationName).text = location.name
            itemView.findViewById<ImageButton>(R.id.deleteButton)
                .setOnClickListener { onDeleteClick(location) }
        }
    }

    class LocationsComparator : DiffUtil.ItemCallback<LocationEntity>() {
        override fun areItemsTheSame(oldItem: LocationEntity, newItem: LocationEntity): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: LocationEntity, newItem: LocationEntity): Boolean {
            return oldItem == newItem
        }
    }
}