package com.example.unilink.repository

import com.example.unilink.models.Post

object PostRepository {

    private val posts = mutableListOf(
        Post(
            id = 1,
            title = "Bienvenido",
            content = "Primer post de UniLink",
            author = "Administrador"
        )
    )

    fun getAll(): List<Post> = posts

    fun getById(id: Int): Post? =
        posts.find { it.id == id }

    fun add(post: Post) {
        posts.add(post)
    }

    fun update(id: Int, updatedPost: Post): Boolean {
        val index = posts.indexOfFirst { it.id == id }

        return if (index != -1) {
            posts[index] = updatedPost
            true
        } else {
            false
        }
    }

    fun delete(id: Int): Boolean {
        return posts.removeIf { it.id == id }
    }
}