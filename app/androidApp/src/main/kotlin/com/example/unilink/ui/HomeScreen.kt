package com.example.unilink.ui

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.unilink.viewmodel.LoginUiState
import com.example.unilink.viewmodel.WeatherViewModel

@Composable
fun HomeScreen(
    loginState: LoginUiState,
    viewModel: WeatherViewModel,
    onOpenPosts: () -> Unit,
    onLogout: () -> Unit
) {
    val state by viewModel.uiState.collectAsStateWithLifecycle()

    LaunchedEffect(Unit) {
        if (state.weather == null) {
            viewModel.loadWeather()
        }
    }

    Column(
        verticalArrangement = Arrangement.spacedBy(16.dp),
        modifier = Modifier.fillMaxWidth()
    ) {
        Row(
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.fillMaxWidth()
        ) {
            Column {
                Text(
                    text = "Hola, ${loginState.currentUser?.name.orEmpty()}",
                    style = MaterialTheme.typography.titleLarge
                )
                Text(
                    text = loginState.currentUser?.email.orEmpty(),
                    style = MaterialTheme.typography.bodySmall
                )
            }
            TextButton(onClick = onLogout) {
                Text("Salir")
            }
        }

        OutlinedTextField(
            value = state.city,
            onValueChange = viewModel::updateCity,
            label = { Text("Ciudad") },
            singleLine = true,
            modifier = Modifier.fillMaxWidth()
        )

        Row(
            horizontalArrangement = Arrangement.spacedBy(12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Button(
                onClick = viewModel::loadWeather,
                enabled = !state.isLoading
            ) {
                Text("Consultar clima")
            }
            Button(onClick = onOpenPosts) {
                Text("Publicaciones")
            }
            if (state.isLoading) {
                CircularProgressIndicator()
            }
        }

        state.weather?.let { weather ->
            Column(
                verticalArrangement = Arrangement.spacedBy(6.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(
                    text = weather.city,
                    style = MaterialTheme.typography.titleMedium
                )
                Text(
                    text = "${weather.temperature} C",
                    style = MaterialTheme.typography.headlineSmall
                )
                Text(
                    text = weather.description,
                    style = MaterialTheme.typography.bodyMedium
                )
            }
        }

        state.errorMessage?.let {
            Text(
                text = it,
                color = MaterialTheme.colorScheme.error,
                style = MaterialTheme.typography.bodySmall
            )
        }
    }
}
