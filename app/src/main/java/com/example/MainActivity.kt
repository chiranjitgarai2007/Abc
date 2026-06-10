package com.example

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.ui.HomeScreen
import com.example.ui.MathRiddlesScreen
import com.example.ui.RiddleViewModel
import com.example.ui.theme.MathBackground
import com.example.ui.theme.MyApplicationTheme

class MainActivity : ComponentActivity() {
  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    enableEdgeToEdge()
    setContent {
      MyApplicationTheme {
        val snackbarHostState = remember { SnackbarHostState() }
        val navController = rememberNavController()
        
        Scaffold(
          modifier = Modifier.fillMaxSize(),
          snackbarHost = { SnackbarHost(hostState = snackbarHostState) },
          containerColor = MathBackground
        ) { innerPadding ->
          Surface(
            modifier = Modifier.padding(innerPadding).fillMaxSize(),
            color = MathBackground
          ) {
            NavHost(navController = navController, startDestination = "splash") {
              composable("splash") {
                com.example.ui.SplashScreen(
                  onNavigateToHome = {
                    navController.navigate("home") {
                      popUpTo("splash") { inclusive = true }
                    }
                  }
                )
              }
              composable("home") {
                HomeScreen(
                  onNavigateToRiddles = { navController.navigate("riddles") },
                  snackbarHostState = snackbarHostState
                )
              }
              composable("riddles") {
                val viewModel: RiddleViewModel = viewModel()
                MathRiddlesScreen(
                  viewModel = viewModel,
                  onBack = { navController.popBackStack() },
                  snackbarHostState = snackbarHostState
                )
              }
            }
          }
        }
      }
    }
  }
}

