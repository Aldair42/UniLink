package com.example.unilink.services

import com.example.unilink.client.HttpClientProvider
import com.example.unilink.models.WeatherInfo
import com.example.unilink.models.OpenWeatherResponse
import io.ktor.client.call.*
import io.ktor.client.request.*


object WeatherService {


    private const val API_KEY = "0ba2542c0e649e37910618502b889dbd"


    suspend fun getWeather(city: String): WeatherInfo {


        val response: OpenWeatherResponse =
            HttpClientProvider.client.get(
                "https://api.openweathermap.org/data/2.5/weather"
            ) {

                url {
                    parameters.append(
                        "q",
                        city
                    )

                    parameters.append(
                        "appid",
                        API_KEY
                    )

                    parameters.append(
                        "units",
                        "metric"
                    )

                }
            }.body()



        return WeatherInfo(
            city = city,
            temperature = response.main.temp,
            description = response.weather[0].description
        )
    }
}