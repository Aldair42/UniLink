package com.example.unilink.repository

import com.example.unilink.models.User

object UserRepository {

    private val users = mutableListOf<User>()

    private var nextId = 1


    fun getAll(): List<User> {
        return users
    }


    fun getById(id: Int): User? {
        return users.find { it.id == id }
    }


    fun getByEmail(email: String): User? {
        return users.find { it.email == email }
    }


    fun add(user: User): User {

        val newUser = user.copy(
            id = nextId++
        )

        users.add(newUser)

        return newUser
    }


    fun update(id: Int, updatedUser: User): User? {

        val index = users.indexOfFirst {
            it.id == id
        }

        return if (index != -1) {

            val userUpdated = updatedUser.copy(
                id = id
            )

            users[index] = userUpdated

            userUpdated

        } else {
            null
        }
    }


    fun delete(id: Int): Boolean {

        return users.removeIf {
            it.id == id
        }
    }
}