package com.example.unilink.repository

import com.example.unilink.models.LoginResponse
import com.example.unilink.models.User
import com.example.unilink.network.ApiClient

class AuthRepository(
    private val apiClient: ApiClient = ApiClient()
) {
    suspend fun register(name: String, email: String, password: String): User {
        return apiClient.post(
            path = "/auth/register",
            body = User(name = name, email = email, password = password)
        )
    }

    suspend fun login(email: String, password: String): LoginResponse {
        return apiClient.post(
            path = "/auth/login",
            body = User(email = email, password = password)
        )
    }
}
