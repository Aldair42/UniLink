package com.example.unilink.repository

import com.example.unilink.models.WeatherInfo
import com.example.unilink.network.ApiClient

class WeatherRepository(
    private val apiClient: ApiClient = ApiClient()
) {
    suspend fun getWeather(city: String): WeatherInfo {
        return apiClient.get(
            path = "/weather",
            queryParameters = mapOf("city" to city)
        )
    }
}
