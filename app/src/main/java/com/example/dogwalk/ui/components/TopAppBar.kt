package com.example.dogwalk.ui.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.NavHostController
import com.example.dogwalk.R

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TopBarWithLogo(
    title: String,
    showBack: Boolean = false,
    showMenu: Boolean = false,
    navController: NavController, // Zmieniamy na non-null
    onMenuItemClick: ((String) -> Unit)? = null
) {
    TopAppBar(
        modifier = Modifier.height(56.dp),
        title = {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .fillMaxHeight(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Start
            ) {
                Image(
                    painter = painterResource(id = R.drawable.dw_logo),
                    contentDescription = "Logo",
                    modifier = Modifier
                        .size(28.dp)
                        .padding(end = 8.dp)
                )
                Text(title)
            }
        },
        navigationIcon = {
            if (showBack) {
                IconButton(onClick = { navController.popBackStack() }) {
                    Icon(Icons.Default.ArrowBack, contentDescription = "Wstecz")
                }
            }
        },
        actions = {
            if (showMenu) {
                var expanded by remember { mutableStateOf(false) }

                IconButton(onClick = { expanded = true }) {
                    Icon(Icons.Default.MoreVert, contentDescription = "Menu")
                }

                DropdownMenu(
                    expanded = expanded,
                    onDismissRequest = { expanded = false }
                ) {
                    DropdownMenuItem(
                        text = { Text("Motyw") },
                        onClick = {
                            expanded = false
                            onMenuItemClick?.invoke("theme_settings")
                        }
                    )
                    DropdownMenuItem(
                        text = { Text("Dodaj zwierzaka") },
                        onClick = {
                            expanded = false
                            onMenuItemClick?.invoke("add_pet")
                        }
                    )
                    DropdownMenuItem(
                        text = { Text("Dodaj przyjaciela") },
                        onClick = {
                            expanded = false
                            onMenuItemClick?.invoke("add_friend")
                        }
                    )
                    DropdownMenuItem(
                        text = { Text("Mój profil") },
                        onClick = {
                            expanded = false
                            onMenuItemClick?.invoke("profile")
                        }
                    )
                }
            }
        }
    )
}
