package com.example.unilink.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.unilink.models.User
import com.example.unilink.repository.AuthRepository
import com.example.unilink.session.EmptySessionStorage
import com.example.unilink.session.SessionStorage
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

data class LoginUiState(
    val email: String = "",
    val password: String = "",
    val name: String = "",
    val currentUser: User? = null,
    val isLoading: Boolean = false,
    val errorMessage: String? = null,
    val successMessage: String? = null
) {
    val isLoggedIn: Boolean = currentUser != null
}

class LoginViewModel(
    private val authRepository: AuthRepository = AuthRepository(),
    private val sessionStorage: SessionStorage = EmptySessionStorage
) : ViewModel() {
    private val _uiState = MutableStateFlow(LoginUiState())
    val uiState: StateFlow<LoginUiState> = _uiState.asStateFlow()

    init {
        loadSavedSession()
    }

    private fun loadSavedSession() {
        viewModelScope.launch {
            val savedUser = sessionStorage.getSavedUser()
            if (savedUser != null) {
                _uiState.update {
                    it.copy(
                        email = savedUser.email,
                        currentUser = savedUser
                    )
                }
            }
        }
    }

    fun updateEmail(email: String) {
        _uiState.update { it.copy(email = email, errorMessage = null) }
    }

    fun updatePassword(password: String) {
        _uiState.update { it.copy(password = password, errorMessage = null) }
    }

    fun updateName(name: String) {
        _uiState.update { it.copy(name = name, errorMessage = null) }
    }

    fun login() {
        val state = _uiState.value
        if (state.email.isBlank() || state.password.isBlank()) {
            _uiState.update { it.copy(errorMessage = "Ingresa correo y contraseña.") }
            return
        }

        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true, errorMessage = null, successMessage = null) }
            runCatching {
                authRepository.login(state.email.trim(), state.password)
            }.onSuccess { response ->
                sessionStorage.saveUser(response.user)
                _uiState.update {
                    it.copy(
                        currentUser = response.user,
                        isLoading = false,
                        successMessage = response.message
                    )
                }
            }.onFailure { error ->
                _uiState.update {
                    it.copy(
                        isLoading = false,
                        errorMessage = error.message ?: "No se pudo iniciar sesion."
                    )
                }
            }
        }
    }

    fun register() {
        val state = _uiState.value
        if (state.name.isBlank() || state.email.isBlank() || state.password.isBlank()) {
            _uiState.update { it.copy(errorMessage = "Completa nombre, correo y contraseña.") }
            return
        }

        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true, errorMessage = null, successMessage = null) }
            runCatching {
                authRepository.register(
                    name = state.name.trim(),
                    email = state.email.trim(),
                    password = state.password
                )
            }.onSuccess { user ->
                sessionStorage.saveUser(user)
                _uiState.update {
                    it.copy(
                        currentUser = user,
                        isLoading = false,
                        successMessage = "Registro correcto"
                    )
                }
            }.onFailure { error ->
                _uiState.update {
                    it.copy(
                        isLoading = false,
                        errorMessage = error.message ?: "No se pudo registrar el usuario."
                    )
                }
            }
        }
    }

    fun logout() {
        viewModelScope.launch {
            sessionStorage.clearSession()
        }
        _uiState.update {
            LoginUiState(email = it.email)
        }
    }

    fun clearMessages() {
        _uiState.update { it.copy(errorMessage = null, successMessage = null) }
    }
}
