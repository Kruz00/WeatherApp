package com.pam_228779.weatherapppro.data.model

data class Location(
    val name: String,
    val localNames: Map<String, String>,
    val lat: Double,
    val lon: Double,
    val country: String,
    val state: String
)
