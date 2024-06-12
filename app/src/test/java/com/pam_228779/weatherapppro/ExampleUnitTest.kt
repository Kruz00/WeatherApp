package com.pam_228779.weatherapppro

import com.pam_228779.weatherapppro.data.api.WeatherApiClient
import org.junit.Test

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * See [testing documentation](http://d.android.com/tools/testing).
 */
class ExampleUnitTest {

    @Test
    fun oneCallApiTest() {
        println("ok")
        val apiKey = "01197c9af7c8ccccf5cd00f1300e7d0f"
        val weatherApiClient = WeatherApiClient()

        val latitude = 51.7687323
        val longitude = 19.4569911
        val language = "pl"
        val units = "metric"

        try {
            val weatherData = weatherApiClient.getOneCall(latitude, longitude, language, units, "minutely,alerts")
            // Handle the weather data here
            println("Temperature: ${weatherData?.current?.temp}")
            println("Description: ${weatherData?.current?.weather?.get(0)?.description}")
        } catch (e: Exception) {
            // Handle the error
            println("Error: ${e.message}")
        }
    }

    @Test
    fun geocodingapiTest() {
        println("ok")
        val apiKey = "01197c9af7c8ccccf5cd00f1300e7d0f"
        val weatherApiClient = WeatherApiClient()

        val city = "Łódź"
        val limit = 5

        try {
            val citiesData = weatherApiClient.getLocations(city, limit)

            println("City: ${citiesData?.get(0)}")

            val latitude = citiesData?.get(0)?.lat
            val longitude = citiesData?.get(0)?.lon
            val language = "pl"
            val units = "metric"

            val weatherData = weatherApiClient.getOneCall(latitude!!, longitude!!, language, units, "minutely,alerts")
            // Handle the weather data here
            println("Temperature: ${weatherData?.current?.temp}")
            println("Description: ${weatherData?.current?.weather?.get(0)?.description}")

        } catch (e: Exception) {
            // Handle the error
            println("Error: ${e.message}")
        }

        try {

        } catch (e: Exception) {
            // Handle the error
            println("Error: ${e.message}")
        }
    }
}