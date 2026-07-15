package com.example.unilink.routes

import com.example.unilink.services.WeatherService
import io.ktor.server.application.*
import io.ktor.server.response.*
import io.ktor.server.routing.*


fun Application.configureWeatherRoutes() {

    routing {

        get("/weather") {


            val city =
                call.request.queryParameters["city"]
                    ?: "Mexico"



            val weather =
                WeatherService.getWeather(city)



            call.respond(weather)
        }
    }
}