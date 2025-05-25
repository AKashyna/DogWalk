package com.example.dogwalk.ui.walk


import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.dogwalk.WalkViewModel
import com.example.dogwalk.data.Walk
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import java.text.SimpleDateFormat
import java.util.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun NewWalkSummaryScreen(navController: NavController) {
    val draft = navController.previousBackStackEntry
        ?.savedStateHandle
        ?.get<WalkDraft>("walkDraft")

    val walkViewModel: WalkViewModel = viewModel()
    val auth = FirebaseAuth.getInstance()

    var pets by remember { mutableStateOf("") }
    var walkedWith by remember { mutableStateOf("") }
    var description by remember { mutableStateOf("") }

    if (draft == null) {
        Text("Brak danych spaceru")
        return
    }

    Scaffold(
        topBar = {
            TopAppBar(title = { Text("Podsumowanie spaceru") })
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .padding(padding)
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            Text("Czas: ${draft.duration} min")
            Text("Dystans: ${draft.distance} km")

            OutlinedTextField(
                value = pets,
                onValueChange = { pets = it },
                label = { Text("Z jakimi zwierzakami?") }
            )

            OutlinedTextField(
                value = walkedWith,
                onValueChange = { walkedWith = it },
                label = { Text("Z kim szłaś/eś?") }
            )

            OutlinedTextField(
                value = description,
                onValueChange = { description = it },
                label = { Text("Opis (opcjonalnie)") }
            )

            Button(
                onClick = {
                    val user = auth.currentUser ?: return@Button

                    val dateFormat = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
                    val timeFormat = SimpleDateFormat("HH:mm", Locale.getDefault())
                    val dateObj = Date(draft.startTime)

                    val data = Walk(
                        userId = user.uid,
                        pets = pets.split(", ").filter { it.isNotBlank() },
                        date = dateFormat.format(dateObj),
                        startTime = timeFormat.format(dateObj),
                        durationMinutes = draft.duration,
                        distanceKm = draft.distance,
                        walkedWith = walkedWith.split(", ").filter { it.isNotBlank() }
                    )

                    Firebase.firestore.collection("walks")
                        .add(data)
                        .addOnSuccessListener {
                            navController.navigate("map_screen") {
                                popUpTo("home") { inclusive = false }
                            }
                        }
                },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Zapisz spacer")
            }
        }
    }
}
