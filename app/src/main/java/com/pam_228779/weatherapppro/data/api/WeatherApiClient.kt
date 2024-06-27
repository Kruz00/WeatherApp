package com.pam_228779.weatherapppro.data.api

import com.pam_228779.weatherapppro.data.model.Location
import com.pam_228779.weatherapppro.data.model.OneCallApiResponse
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class WeatherApiClient {
    private val weatherService: WeatherService

    private val apiKey = "01197c9af7c8ccccf5cd00f1300e7d0f"

    init {
        val retrofit= Retrofit.Builder()
            .baseUrl("https://api.openweathermap.org/data/2.5/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        weatherService = retrofit.create(WeatherService::class.java)
    }

    fun getLocations(
        query: String,
        limit: Int
    ): List<Location>? {
        val call = weatherService.getLocation(query, limit, apiKey)

        val response = call.execute()

        if (response.isSuccessful) {
            return response.body()
        } else {
            val error = response.errorBody()?.string()
            throw Exception(error ?: "Unknown error")
        }
    }

    fun getOneCall(
        latitude: Double,
        longitude: Double,
        language: String,
        units: String,
        exclude: String
    ): OneCallApiResponse? {
        val call = weatherService.getOneCall(latitude, longitude, apiKey, language, units, exclude)

        val response = call.execute()

        if (response.isSuccessful) {
            return response.body()
        } else {
            val error = response.errorBody()?.string()
            throw Exception(error ?: "Unknown error")
        }
    }
}

