package com.pam_228779.weatherapppro.data.api

import com.pam_228779.weatherapppro.data.model.Location
import com.pam_228779.weatherapppro.data.model.OneCallApiResponse
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

interface WeatherService {

    @GET("/geo/1.0/direct")
    fun getLocation(
        @Query("q") query: String,
        @Query("limit") limit: Int,
        @Query("appid") apiKey: String
    ): Call<List<Location>>

    @GET("/data/3.0/onecall")
    fun getOneCall(
        @Query("lat") latitude: Double,
        @Query("lon") longitude: Double,
        @Query("appid") apiKey: String,
        @Query("lang") language: String,
        @Query("units") units: String,
        @Query("exclude") exclude: String
    ): Call<OneCallApiResponse>
}