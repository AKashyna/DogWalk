package com.example.dogwalk.ui.settings

import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.foundation.layout.*
import androidx.compose.ui.unit.dp

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ThemeSettingsScreen() {
    Scaffold(
        topBar = { TopAppBar(title = { Text("Wybierz motyw") }) }
    ) { padding ->
        Column(
            modifier = Modifier
                .padding(padding)
                .padding(16.dp)
        ) {
            Text("Tu będą opcje wyboru motywu (jasny, ciemny, zielony, fioletowy)")
        }
    }
}