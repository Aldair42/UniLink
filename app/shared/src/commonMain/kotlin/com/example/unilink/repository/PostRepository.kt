package com.example.unilink.repository

import com.example.unilink.models.Post
import com.example.unilink.network.ApiClient

class PostRepository(
    private val apiClient: ApiClient = ApiClient()
) {
    suspend fun getPosts(): List<Post> {
        return apiClient.get("/posts")
    }

    suspend fun getPost(id: Int): Post {
        return apiClient.get("/posts/$id")
    }

    suspend fun createPost(title: String, content: String, author: String) {
        apiClient.postWithoutResponse(
            path = "/posts",
            body = Post(title = title, content = content, author = author)
        )
    }

    suspend fun updatePost(id: Int, title: String, content: String, author: String) {
        apiClient.patchWithoutResponse(
            path = "/posts/$id",
            body = Post(id = id, title = title, content = content, author = author)
        )
    }

    suspend fun deletePost(id: Int) {
        apiClient.delete("/posts/$id")
    }
}
