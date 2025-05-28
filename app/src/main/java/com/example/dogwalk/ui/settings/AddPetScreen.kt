package com.example.dogwalk.ui.settings

import android.widget.Toast
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.dogwalk.data.PetRepository
import com.example.dogwalk.ui.components.TopBarWithLogo

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddPetScreen(navController: NavController) {
    val context = LocalContext.current

    var selectedType by remember { mutableStateOf("") }
    var breed by remember { mutableStateOf("") }
    var name by remember { mutableStateOf("") }
    var expanded by remember { mutableStateOf(false) }

    val types = listOf("🐶 pies", "🐱 kot", "🐴 koń", "🐟 rybka", "🐰 królik", "🐭 mysz")
    val typeMap = mapOf(
        "🐶 pies" to "pies",
        "🐱 kot" to "kot",
        "🐴 koń" to "koń",
        "🐟 rybka" to "rybka",
        "🐰 królik" to "królik",
        "🐭 mysz" to "mysz"
    )

    Scaffold(
        topBar = {
            TopBarWithLogo(
                title = "Dodaj zwierzaka",
                showBack = true,
                showMenu = true,
                navController = navController,
                onMenuItemClick = { navController.navigate(it) }
            )
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .padding(padding)
                .padding(16.dp)
                .fillMaxSize(),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            ExposedDropdownMenuBox(
                expanded = expanded,
                onExpandedChange = { expanded = !expanded }
            ) {
                TextField(
                    value = selectedType,
                    onValueChange = {},
                    readOnly = true,
                    label = { Text("Gatunek") },
                    trailingIcon = {
                        ExposedDropdownMenuDefaults.TrailingIcon(expanded = expanded)
                    },
                    modifier = Modifier.menuAnchor()
                )

                ExposedDropdownMenu(
                    expanded = expanded,
                    onDismissRequest = { expanded = false }
                ) {
                    types.forEach { item ->
                        DropdownMenuItem(
                            text = { Text(item) },
                            onClick = {
                                selectedType = item
                                expanded = false
                            }
                        )
                    }
                }
            }

            OutlinedTextField(
                value = breed,
                onValueChange = { breed = it },
                label = { Text("Rasa (opcjonalnie)") },
                modifier = Modifier.fillMaxWidth()
            )

            OutlinedTextField(
                value = name,
                onValueChange = { name = it },
                label = { Text("Imię") },
                modifier = Modifier.fillMaxWidth()
            )

            Button(
                onClick = {
                    if (selectedType.isBlank() || name.isBlank()) {
                        Toast.makeText(context, "Wypełnij gatunek i imię", Toast.LENGTH_SHORT).show()
                        return@Button
                    }

                    val pet = Pet(
                        type = typeMap[selectedType] ?: selectedType,
                        breed = breed,
                        name = name
                    )

                    PetRepository.addPet(
                        pet = pet,
                        onSuccess = {
                            Toast.makeText(context, "Zapisano zwierzaka", Toast.LENGTH_SHORT).show()
                            navController.popBackStack()
                        },
                        onFailure = {
                            Toast.makeText(context, "Błąd zapisu", Toast.LENGTH_SHORT).show()
                        }
                    )
                },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Zapisz")
            }
        }
    }
}
