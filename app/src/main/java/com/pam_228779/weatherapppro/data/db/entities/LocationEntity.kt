package com.pam_228779.weatherapppro.data.db.entities

import android.os.Parcelable
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.parcelize.Parcelize

@Entity(tableName = "locations")
@Parcelize
data class LocationEntity(
//    @PrimaryKey(autoGenerate = true)
    @PrimaryKey
    val id: Int = 0,

    val name: String,
    val lat: Double,
    val lon: Double,
    val country: String,
    val state: String
) : Parcelable
