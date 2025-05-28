package com.example.dogwalk.ui.settings

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.dogwalk.data.FriendRepository
import com.example.dogwalk.ui.components.TopBarWithLogo
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddFriendScreen(navController: NavController) {
    val auth = FirebaseAuth.getInstance()
    val db = Firebase.firestore

    var searchQuery by remember { mutableStateOf("") }
    var foundUser by remember { mutableStateOf<Pair<String, String>?>(null) } // Pair<UID, username>
    var errorMessage by remember { mutableStateOf<String?>(null) }

    Scaffold(
        topBar = {
            TopBarWithLogo(
                title = "Dodaj przyjaciela",
                showBack = true,
                showMenu = true,
                navController = navController,
                onMenuItemClick = { route -> navController.navigate(route) }
            )
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .padding(padding)
                .padding(16.dp)
                .fillMaxSize()
        ) {
            OutlinedTextField(
                value = searchQuery,
                onValueChange = { searchQuery = it },
                label = { Text("Nazwa użytkownika") },
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(8.dp))

            Button(
                onClick = {
                    db.collection("users")
                        .whereEqualTo("username", searchQuery)
                        .get()
                        .addOnSuccessListener { result ->
                            val userDoc = result.documents.firstOrNull()
                            if (userDoc != null) {
                                val foundUid = userDoc.id
                                if (foundUid == auth.currentUser?.uid) {
                                    errorMessage = "Nie możesz obserwować samego siebie."
                                    foundUser = null
                                } else {
                                    foundUser = Pair(foundUid, userDoc.getString("username") ?: "")
                                    errorMessage = null
                                }
                            } else {
                                errorMessage = "Nie znaleziono użytkownika."
                                foundUser = null
                            }

                        }
                        .addOnFailureListener {
                            errorMessage = "Błąd połączenia z bazą."
                        }
                },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Szukaj")
            }

            Spacer(modifier = Modifier.height(16.dp))

            errorMessage?.let {
                Text(it, color = MaterialTheme.colorScheme.error)
            }

            foundUser?.let { (uid, username) ->
                Text("Znaleziono użytkownika: $username")

                Spacer(modifier = Modifier.height(8.dp))

                Button(
                    onClick = {
                        FriendRepository.addFriend(
                            uid,
                            onSuccess = {
                                errorMessage = null
                                foundUser = null
                                searchQuery = ""
                            },
                            onFailure = {
                                errorMessage = "Nie udało się dodać znajomego."
                            }
                        )
                    }
                ) {
                    Text("Dodaj znajomego")
                }
            }
        }
    }
}
