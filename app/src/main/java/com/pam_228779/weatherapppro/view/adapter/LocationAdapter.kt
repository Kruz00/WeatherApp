package com.pam_228779.weatherapppro.view.adapter


class LocationAdapter(private val onDeleteClick: (LocationEntity) -> Unit) :
    ListAdapter<LocationEntity, LocationAdapter.LocationViewHolder>(LocationsComparator()) {

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
            itemView.locationName.text = location.name
            itemView.deleteButton.setOnClickListener { onDeleteClick(location) }
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