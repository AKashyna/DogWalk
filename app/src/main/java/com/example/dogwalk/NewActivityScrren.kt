package com.example.dogwalk

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.material3.TopAppBar


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun NewActivityScreen() {
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Nowa Aktywność") }
            )
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(16.dp)
        ) {
            Text("Tu będzie formularz dodawania nowej aktywności 🐾")
        }
    }
}
