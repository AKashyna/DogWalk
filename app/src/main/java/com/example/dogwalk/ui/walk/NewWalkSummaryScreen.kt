package com.example.dogwalk.ui.walk


import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
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
import com.example.dogwalk.ui.components.TopBarWithLogo
import com.example.dogwalk.ui.settings.ProfileViewModel


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun NewWalkSummaryScreen(navController: NavController) {
    val draft = navController.previousBackStackEntry
        ?.savedStateHandle
        ?.get<WalkDraft>("walkDraft")

    val walkViewModel: WalkViewModel = viewModel()

    val profileViewModel: ProfileViewModel = viewModel()
    LaunchedEffect(Unit) { profileViewModel.loadUserData() }

    var selectedPets by remember { mutableStateOf(setOf<String>()) }
    var selectedFriends by remember { mutableStateOf(setOf<String>()) }

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
            TopBarWithLogo(
                title = "Podsumowanie spaceru",
                showMenu = true,
                navController = navController,
                onMenuItemClick = { route -> navController.navigate(route) }
            )
        }
    )
    { padding ->
        Column(
            modifier = Modifier
                .padding(padding)
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            Text("Czas: ${draft.duration} min")
            Text("Dystans: ${draft.distance} km")

            Text("Z jakimi zwierzakami?")
            profileViewModel.pets.map { it.name }.plus("z żadnym").forEach { name ->
                val selected = name in selectedPets
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Checkbox(
                        checked = selected,
                        onCheckedChange = {
                            selectedPets = if (name == "z żadnym") {
                                if (it) setOf("z żadnym") else emptySet()
                            } else {
                                if ("z żadnym" in selectedPets) selectedPets - "z żadnym"
                                if (it) selectedPets + name else selectedPets - name
                            }
                        },
                        enabled = name == "z żadnym" || "z żadnym" !in selectedPets
                    )
                    Text(name)
                }
            }


            Text("Z kim szłaś/eś?")
            profileViewModel.friends.map { it.username }.plus("z żadnym").forEach { name ->
                val selected = name in selectedFriends
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Checkbox(
                        checked = selected,
                        onCheckedChange = {
                            selectedFriends = if (name == "z żadnym") {
                                if (it) setOf("z żadnym") else emptySet()
                            } else {
                                if ("z żadnym" in selectedFriends) selectedFriends - "z żadnym"
                                if (it) selectedFriends + name else selectedFriends - name
                            }
                        },
                        enabled = name == "z żadnym" || "z żadnym" !in selectedFriends
                    )
                    Text(name)
                }
            }


            OutlinedTextField(
                value = description,
                onValueChange = { description = it },
                label = { Text("Opis (opcjonalnie)") }
            )

            Button(
                onClick = {
                    val user = auth.currentUser ?: return@Button
                    val db = Firebase.firestore

                    db.collection("users").document(user.uid).get()
                        .addOnSuccessListener { userDoc ->
                            val username = userDoc.getString("username") ?: user.email?.substringBefore("@") ?: "Nieznajomy"

                            val dateFormat = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
                            val timeFormat = SimpleDateFormat("HH:mm", Locale.getDefault())
                            val dateObj = Date(draft.startTime)

                            val data = Walk(
                                userId = user.uid,
                                username = username,
                                pets = if ("z żadnym" in selectedPets) emptyList() else selectedPets.toList(),
                                date = dateFormat.format(dateObj),
                                startTime = timeFormat.format(dateObj),
                                durationMinutes = draft.duration,
                                distanceKm = draft.distance,
                                walkedWith = if ("z żadnym" in selectedFriends) emptyList() else selectedFriends.toList()
                            )

                            db.collection("walks")
                                .add(data)
                                .addOnSuccessListener {
                                    navController.navigate("map_screen") {
                                        popUpTo("home") { inclusive = false }
                                    }

                                }
                        }
                }
                ,
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Zapisz spacer")
            }
        }
    }
}
