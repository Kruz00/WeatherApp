package com.pam_228779.weatherapppro.data.model

data class WeatherData(private val oneCallApiResponse: OneCallApiResponse, val units: String) {
    val current: Current = oneCallApiResponse.current
    val hourly: List<Hourly> = oneCallApiResponse.hourly
    val daily: List<Daily> = oneCallApiResponse.daily
    val timezone: String = oneCallApiResponse.timezone
}
