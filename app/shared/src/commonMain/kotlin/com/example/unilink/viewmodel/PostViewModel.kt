package com.example.unilink.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.unilink.models.Post
import com.example.unilink.repository.PostRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

data class PostUiState(
    val posts: List<Post> = emptyList(),
    val title: String = "",
    val content: String = "",
    val author: String = "",
    val editingPostId: Int? = null,
    val isLoading: Boolean = false,
    val errorMessage: String? = null
)

class PostViewModel(
    private val postRepository: PostRepository = PostRepository()
) : ViewModel() {
    private val _uiState = MutableStateFlow(PostUiState())
    val uiState: StateFlow<PostUiState> = _uiState.asStateFlow()

    fun loadPosts() {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true, errorMessage = null) }
            runCatching {
                postRepository.getPosts()
            }.onSuccess { posts ->
                _uiState.update {
                    it.copy(posts = posts, isLoading = false)
                }
            }.onFailure { error ->
                _uiState.update {
                    it.copy(
                        isLoading = false,
                        errorMessage = error.message ?: "No se pudieron cargar las publicaciones."
                    )
                }
            }
        }
    }

    fun updateTitle(title: String) {
        _uiState.update { it.copy(title = title, errorMessage = null) }
    }

    fun updateContent(content: String) {
        _uiState.update { it.copy(content = content, errorMessage = null) }
    }

    fun updateAuthor(author: String) {
        _uiState.update { it.copy(author = author, errorMessage = null) }
    }

    fun selectPost(post: Post) {
        _uiState.update {
            it.copy(
                title = post.title,
                content = post.content,
                author = post.author,
                editingPostId = post.id,
                errorMessage = null
            )
        }
    }

    fun savePost() {
        val state = _uiState.value
        if (state.title.isBlank() || state.content.isBlank() || state.author.isBlank()) {
            _uiState.update { it.copy(errorMessage = "Completa titulo, contenido y autor.") }
            return
        }

        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true, errorMessage = null) }
            runCatching {
                val editingId = state.editingPostId
                if (editingId == null) {
                    postRepository.createPost(
                        title = state.title.trim(),
                        content = state.content.trim(),
                        author = state.author.trim()
                    )
                } else {
                    postRepository.updatePost(
                        id = editingId,
                        title = state.title.trim(),
                        content = state.content.trim(),
                        author = state.author.trim()
                    )
                }
            }.onSuccess {
                clearForm()
                loadPosts()
            }.onFailure { error ->
                _uiState.update {
                    it.copy(
                        isLoading = false,
                        errorMessage = error.message ?: "No se pudo guardar la publicacion."
                    )
                }
            }
        }
    }

    fun deletePost(id: Int) {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true, errorMessage = null) }
            runCatching {
                postRepository.deletePost(id)
            }.onSuccess {
                loadPosts()
            }.onFailure { error ->
                _uiState.update {
                    it.copy(
                        isLoading = false,
                        errorMessage = error.message ?: "No se pudo eliminar la publicacion."
                    )
                }
            }
        }
    }

    fun clearForm() {
        _uiState.update {
            it.copy(
                title = "",
                content = "",
                author = "",
                editingPostId = null,
                isLoading = false,
                errorMessage = null
            )
        }
    }
}
