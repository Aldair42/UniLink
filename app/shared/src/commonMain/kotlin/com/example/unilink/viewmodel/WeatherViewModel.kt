package com.example.unilink.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.unilink.models.WeatherInfo
import com.example.unilink.repository.WeatherRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

data class WeatherUiState(
    val city: String = "Mexico",
    val weather: WeatherInfo? = null,
    val isLoading: Boolean = false,
    val errorMessage: String? = null
)

class WeatherViewModel(
    private val weatherRepository: WeatherRepository = WeatherRepository()
) : ViewModel() {
    private val _uiState = MutableStateFlow(WeatherUiState())
    val uiState: StateFlow<WeatherUiState> = _uiState.asStateFlow()

    fun updateCity(city: String) {
        _uiState.update { it.copy(city = city, errorMessage = null) }
    }

    fun loadWeather() {
        val city = _uiState.value.city.ifBlank { "Mexico" }

        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true, errorMessage = null) }
            runCatching {
                weatherRepository.getWeather(city.trim())
            }.onSuccess { weather ->
                _uiState.update {
                    it.copy(weather = weather, isLoading = false)
                }
            }.onFailure { error ->
                _uiState.update {
                    it.copy(
                        isLoading = false,
                        errorMessage = error.message ?: "No se pudo cargar el clima."
                    )
                }
            }
        }
    }
}
