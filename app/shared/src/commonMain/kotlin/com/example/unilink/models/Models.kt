package com.example.unilink.models

import kotlinx.serialization.Serializable

@Serializable
data class User(
    val id: Int = 0,
    val name: String = "",
    val email: String = "",
    val password: String = ""
)

@Serializable
data class Post(
    val id: Int = 0,
    val title: String = "",
    val content: String = "",
    val author: String = ""
)

@Serializable
data class WeatherInfo(
    val city: String,
    val temperature: Double,
    val description: String
)

@Serializable
data class LoginResponse(
    val message: String,
    val user: User
)

@Serializable
data class ApiErrorResponse(
    val message: String
)
