package com.example.dogwalk

import androidx.compose.animation.core.*
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import kotlinx.coroutines.delay
import com.example.dogwalk.R
import com.example.dogwalk.ui.login.PawTrail

@Composable
fun SplashScreen(navController: NavController) {
    // uruchamia przejście po czasie
    LaunchedEffect(Unit) {
        delay(2000) // czas trwania splash screena
        navController.navigate("login") {
            popUpTo("splash") { inclusive = true }
        }
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
    ) {
        PawTrail()

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(24.dp),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Image(
                painter = painterResource(id = R.drawable.dw_logo),
                contentDescription = "DogWalk Logo",
                modifier = Modifier
                    .size(180.dp)
                    .padding(bottom = 32.dp)
            )
        }
    }
}

