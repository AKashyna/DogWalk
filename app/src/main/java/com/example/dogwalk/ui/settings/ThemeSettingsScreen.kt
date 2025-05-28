package com.example.dogwalk.ui.settings

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.dogwalk.data.AppTheme
import com.example.dogwalk.data.ThemePreferenceManager
import kotlinx.coroutines.launch
import com.example.dogwalk.ui.components.TopBarWithLogo


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ThemeSettingsScreen(navController: NavController) {
    val context = LocalContext.current
    val themeManager = remember { ThemePreferenceManager(context) }
    val coroutineScope = rememberCoroutineScope()

    val themeMap = mapOf(
        "Jasny" to AppTheme.LIGHT,
        "Ciemny" to AppTheme.DARK,
        "Zielony" to AppTheme.GREEN,
        "Fioletowy" to AppTheme.PURPLE
    )

    val currentTheme by themeManager.selectedTheme.collectAsState(initial = AppTheme.LIGHT)
    var selectedOption by remember { mutableStateOf(currentTheme.name.lowercase().replaceFirstChar { it.uppercase() }) }

    Scaffold(
        topBar = {
            TopBarWithLogo(
                title = "Motyw",
                showBack = true,
                showMenu = true,
                navController = navController,
                onMenuItemClick = { route ->
                    navController.navigate(route)
                }
            )
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .padding(padding)
                .fillMaxSize()
                .padding(24.dp)
        ) {
            Text("Wybierz motyw:", style = MaterialTheme.typography.titleLarge)
            Spacer(Modifier.height(16.dp))

            themeMap.forEach { (label, appTheme) ->
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 4.dp)
                        .clickable {
                            selectedOption = label
                            coroutineScope.launch {
                                themeManager.saveTheme(appTheme)
                            }
                        },
                    colors = CardDefaults.cardColors(
                        containerColor = if (selectedOption == label)
                            MaterialTheme.colorScheme.primary.copy(alpha = 0.2f)
                        else
                            MaterialTheme.colorScheme.surface
                    )
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        RadioButton(
                            selected = selectedOption == label,
                            onClick = {
                                selectedOption = label
                                coroutineScope.launch {
                                    themeManager.saveTheme(appTheme)
                                }
                            }
                        )
                        Spacer(Modifier.width(8.dp))
                        Text(label)
                    }
                }
            }
        }
    }
}

