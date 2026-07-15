package com.example.unilink

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.unilink.session.AndroidSessionStorage
import com.example.unilink.ui.HomeScreen
import com.example.unilink.ui.LoginScreen
import com.example.unilink.ui.PostsScreen
import com.example.unilink.ui.RegisterScreen
import com.example.unilink.viewmodel.LoginViewModel
import com.example.unilink.viewmodel.PostViewModel
import com.example.unilink.viewmodel.WeatherViewModel

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        enableEdgeToEdge()
        super.onCreate(savedInstanceState)

        setContent {
            UniLinkApp()
        }
    }
}

private enum class AppScreen {
    Login,
    Register,
    Home,
    Posts
}

@Composable
fun UniLinkApp() {
    val context = LocalContext.current.applicationContext
    val sessionStorage = remember(context) { AndroidSessionStorage(context) }
    val loginViewModel = viewModel { LoginViewModel(sessionStorage = sessionStorage) }
    val weatherViewModel = viewModel { WeatherViewModel() }
    val postViewModel = viewModel { PostViewModel() }
    val loginState by loginViewModel.uiState.collectAsStateWithLifecycle()
    var currentScreen by remember { mutableStateOf(AppScreen.Login) }

    LaunchedEffect(loginState.isLoggedIn) {
        if (loginState.isLoggedIn) {
            currentScreen = AppScreen.Home
        }
    }

    MaterialTheme {
        Surface(
            modifier = Modifier.fillMaxSize(),
            color = MaterialTheme.colorScheme.background
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(20.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                when (currentScreen) {
                    AppScreen.Login -> LoginScreen(
                        viewModel = loginViewModel,
                        onOpenRegister = { currentScreen = AppScreen.Register }
                    )

                    AppScreen.Register -> RegisterScreen(
                        viewModel = loginViewModel,
                        onBackToLogin = { currentScreen = AppScreen.Login }
                    )

                    AppScreen.Home -> HomeScreen(
                        loginState = loginState,
                        viewModel = weatherViewModel,
                        onOpenPosts = { currentScreen = AppScreen.Posts },
                        onLogout = {
                            loginViewModel.logout()
                            currentScreen = AppScreen.Login
                        }
                    )

                    AppScreen.Posts -> PostsScreen(
                        viewModel = postViewModel,
                        onBack = { currentScreen = AppScreen.Home }
                    )
                }
            }
        }
    }
}

@Preview
@Composable
fun AppAndroidPreview() {
    UniLinkApp()
}
