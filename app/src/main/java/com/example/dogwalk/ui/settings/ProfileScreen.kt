package com.example.dogwalk.ui.settings

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.dogwalk.ui.components.TopBarWithLogo

@Composable
fun ProfileScreen(navController: NavController, viewModel: ProfileViewModel = viewModel()) {
    var inputUsername by remember { mutableStateOf("") }

    val username = viewModel.username
    LaunchedEffect(username) {
        inputUsername = username
    }

    LaunchedEffect(Unit) {
        viewModel.loadUserData()
    }

    Scaffold(
        topBar = {
            TopBarWithLogo(
                title = "Mój profil",
                showBack = true,
                showMenu = true,
                navController = navController,
                onMenuItemClick = { route -> navController.navigate(route) }
            )
        }
    ) { padding ->
        LazyColumn(
            contentPadding = padding,
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            item {
                ProfileCard(title = "Email", content = viewModel.email, editable = false)
            }

            item {
                Column {
                    ProfileCard(
                        title = "Nazwa użytkownika",
                        content = inputUsername,
                        editable = true
                    ) {
                        inputUsername = it
                    }
                    Button(
                        onClick = {
                            viewModel.updateUsername(inputUsername)
                        },
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Text("Zmień nazwę")
                    }
                }
            }

            item {
                Text("Moi znajomi:", style = MaterialTheme.typography.titleMedium)
            }

            items(viewModel.friends) { friend ->
                Card(modifier = Modifier.fillMaxWidth()) {
                    Row(
                        modifier = Modifier
                            .padding(16.dp)
                            .fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(friend.username, style = MaterialTheme.typography.bodyLarge)

                        IconButton(onClick = {
                            viewModel.removeFriend(friend.uid)
                        }) {
                            Icon(Icons.Default.Delete, contentDescription = "Usuń znajomego")
                        }
                    }
                }
            }

            item {
                Text("Moje zwierzaki:", style = MaterialTheme.typography.titleMedium)
            }

            items(viewModel.pets) { pet ->
                val emoji = when (pet.type.lowercase()) {
                    "pies", "dog" -> "🐶"
                    "kot", "cat" -> "🐱"
                    "koń", "horse" -> "🐴"
                    "rybka", "fish" -> "🐟"
                    "królik", "rabbit" -> "🐰"
                    "mysz", "mouse" -> "🐭"
                    else -> "🐾"
                }
                val details = "$emoji ${pet.name} (${pet.type}${if (pet.breed.isNotBlank()) ", ${pet.breed}" else ""})"

                Card(modifier = Modifier.fillMaxWidth()) {
                    Row(
                        modifier = Modifier
                            .padding(16.dp)
                            .fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(details, style = MaterialTheme.typography.bodyLarge)

                        IconButton(onClick = {
                            viewModel.removePet(pet)
                        }) {
                            Icon(Icons.Default.Delete, contentDescription = "Usuń zwierzaka")
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun ProfileCard(
    title: String,
    content: String,
    editable: Boolean,
    onValueChange: ((String) -> Unit)? = null
) {
    Card(modifier = Modifier.fillMaxWidth()) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(title, style = MaterialTheme.typography.labelSmall)
            Spacer(modifier = Modifier.height(8.dp))
            if (editable && onValueChange != null) {
                OutlinedTextField(
                    value = content,
                    onValueChange = onValueChange,
                    modifier = Modifier.fillMaxWidth()
                )
            } else {
                Text(content, style = MaterialTheme.typography.bodyLarge)
            }
        }
    }
}
