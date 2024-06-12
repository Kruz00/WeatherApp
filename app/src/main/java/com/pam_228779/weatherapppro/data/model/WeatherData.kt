package com.pam_228779.weatherapppro.data.model

data class WeatherData(private val oneCallApiResponse: OneCallApiResponse) {
    val current: Current = oneCallApiResponse.current
    val hourly: List<Hourly> = oneCallApiResponse.hourly
    val daily: List<Daily> = oneCallApiResponse.daily
//    val lat: Double = oneCallApiResponse.lat
//    val lon: Double = oneCallApiResponse.lon
//    val timezone: String = oneCallApiResponse.timezone
//    val timezone_offset: Int = oneCallApiResponse.timezone_offset
}
