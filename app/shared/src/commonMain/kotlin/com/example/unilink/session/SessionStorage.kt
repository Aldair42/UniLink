package com.example.unilink.session

import com.example.unilink.models.User

interface SessionStorage {
    suspend fun getSavedUser(): User?
    suspend fun saveUser(user: User)
    suspend fun clearSession()
}

object EmptySessionStorage : SessionStorage {
    override suspend fun getSavedUser(): User? = null

    override suspend fun saveUser(user: User) {
    }

    override suspend fun clearSession() {
    }
}
