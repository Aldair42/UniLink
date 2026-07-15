package com.example.unilink

import com.example.unilink.routes.configureAuthRoutes
import com.example.unilink.routes.configurePostRoutes
import com.example.unilink.routes.configureWeatherRoutes
import io.ktor.serialization.kotlinx.json.*
import io.ktor.server.application.*
import io.ktor.server.engine.*
import io.ktor.server.netty.*
import io.ktor.server.plugins.contentnegotiation.*
import io.ktor.server.response.*
import io.ktor.server.routing.*

fun main() {
    val port = System.getenv("PORT")?.toIntOrNull() ?: 8080

    embeddedServer(
        Netty,
        port = port,
        host = "0.0.0.0",
        module = Application::module
    ).start(wait = true)
}


fun Application.module() {

    install(ContentNegotiation) {
        json()
    }


    routing {

        get("/") {
            call.respondText("UniLink API se encuentra funcionando")
        }
    }


    configureAuthRoutes()
    configurePostRoutes()
    configureWeatherRoutes()
}