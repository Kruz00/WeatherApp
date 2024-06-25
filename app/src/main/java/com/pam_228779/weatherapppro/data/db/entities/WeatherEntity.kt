package com.pam_228779.weatherapppro.data.db.entities

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "weathers")
data class WeatherEntity(
    @PrimaryKey val locationId: Int,
    val lat: Double,
    val lon: Double,
    var weatherData: String?             // JSON-serialized Weather object
)
