package com.example.dogwalk

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.example.dogwalk.data.AppTheme
import com.example.dogwalk.data.ThemePreferenceManager
import com.example.dogwalk.ui.feed.HomeScreen
import com.example.dogwalk.ui.settings.*
import com.example.dogwalk.ui.theme.DogWalkTheme
import com.example.dogwalk.ui.walk.NewActivityScreen
import com.example.dogwalk.ui.walk.NewWalkSummaryScreen

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            val context = LocalContext.current
            val themeManager = remember { ThemePreferenceManager(context) }
            val selectedTheme by themeManager.selectedTheme.collectAsState(initial = AppTheme.LIGHT)

            DogWalkTheme(selectedTheme = selectedTheme) {
                DogWalkApp()
            }
        }
    }
}


@Composable
fun DogWalkApp() {
    val navController = rememberNavController()

    Scaffold(
        bottomBar = {
            if (shouldShowBottomBar(navController)) {
                BottomNavigationBar(navController)
            }
        }
    ) { innerPadding ->
        NavHost(
            navController = navController,
            startDestination = "splash",
            modifier = Modifier.padding(innerPadding)
        ) {
            composable("splash") { SplashScreen(navController) }
            composable("login") { LoginScreen(navController) }
            composable("home") {
                HomeScreen(
                    navController = navController,
                    modifier = Modifier.padding(innerPadding)
                )
            }
            composable("new_activity") { NewActivityScreen(navController) }
            composable("walk_summary") { NewWalkSummaryScreen(navController) }
            composable("calendar") {
                val activity = LocalContext.current as ComponentActivity
                CalendarScreen(activity, navController)
            }

            composable("map_screen") {
                val walkViewModel: WalkViewModel = viewModel()
                MapScreen(route = walkViewModel.currentRoute, navController=navController)
            }
            composable("theme_settings") { ThemeSettingsScreen(navController) }
            composable("add_pet") { AddPetScreen(navController) }
            composable("add_friend") { AddFriendScreen(navController) }
            composable("profile") { ProfileScreen(navController) }
        }
    }
}

@Composable
fun BottomNavigationBar(navController: NavHostController) {
    NavigationBar {
        NavigationBarItem(
            icon = { Icon(Icons.Default.Home, contentDescription = "Home") },
            selected = currentRoute(navController) == "home",
            onClick = {
                navController.navigate("home") {
                    popUpTo(navController.graph.startDestinationId)
                    launchSingleTop = true
                }
            }
        )
        NavigationBarItem(
            icon = { Icon(Icons.Default.Add, contentDescription = "Nowa aktywność") },
            selected = currentRoute(navController) == "new_activity",
            onClick = {
                navController.navigate("new_activity") {
                    launchSingleTop = true
                }
            }
        )
        NavigationBarItem(
            icon = { Icon(Icons.Default.CalendarToday, contentDescription = "Kalendarz") },
            selected = currentRoute(navController) == "calendar",
            onClick = {
                navController.navigate("calendar") {
                    launchSingleTop = true
                }
            }
        )
    }
}

@Composable
fun currentRoute(navController: NavHostController): String? {
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    return navBackStackEntry?.destination?.route
}

@Composable
fun shouldShowBottomBar(navController: NavHostController): Boolean {
    val route = currentRoute(navController)
    return when (route) {
        "home", "new_activity", "calendar" -> true
        else -> false
    }
}