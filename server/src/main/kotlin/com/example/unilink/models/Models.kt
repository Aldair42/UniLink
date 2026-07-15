package com.example.unilink.models

import kotlinx.serialization.Serializable

// Modelo de usuario
@Serializable
data class User(
    val id: Int,
    val name: String,
    val email: String,
    val password: String
)


// Modelo de publicaciones
@Serializable
data class Post(
    val id: Int,
    val title: String,
    val content: String,
    val author: String
)


// Modelo de información del clima
@Serializable
data class WeatherInfo(
    val city: String,
    val temperature: Double,
    val description: String
)


// Respuesta del login
@Serializable
data class LoginResponse(
    val message: String,
    val user: User
)
@Serializable
data class OpenWeatherResponse(
    val main: Main,
    val weather: List<WeatherDescription>
)


@Serializable
data class Main(
    val temp: Double
)


@Serializable
data class WeatherDescription(
    val description: String
)