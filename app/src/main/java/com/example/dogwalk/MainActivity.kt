package com.example.dogwalk

import com.example.dogwalk.ui.theme.DogWalkTheme
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import androidx.navigation.compose.*
import androidx.lifecycle.viewmodel.compose.viewModel
import com.google.android.gms.maps.model.LatLng
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.dogwalk.WalkViewModel
import com.example.dogwalk.MapScreen
import com.example.dogwalk.ui.feed.ActivityFeed
import com.example.dogwalk.ui.walk.NewWalkSummaryScreen
import com.example.dogwalk.ui.walk.NewActivityScreen
import com.example.dogwalk.ui.settings.*


class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            DogWalkTheme {
                val navController = rememberNavController()
                NavHost(
                    navController = navController,
                    startDestination = "splash"
                ) {
                    composable("splash") { SplashScreen(navController) }
                    composable("login") { LoginScreen(navController) }
                    composable("main") { MainScreen() }

                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainScreen() {
    val navController = rememberNavController()

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("DogWalk") },
                actions = {
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
                                navController.navigate("theme_settings")
                            }
                        )
                        DropdownMenuItem(
                            text = { Text("Dodaj zwierzaka") },
                            onClick = {
                                expanded = false
                                navController.navigate("add_pet")
                            }
                        )
                        DropdownMenuItem(
                            text = { Text("Dodaj przyjaciela") },
                            onClick = {
                                expanded = false
                                navController.navigate("add_friend")
                            }
                        )
                    }

                }
            )
        },
        bottomBar = {
            BottomNavigationBar(navController)
        }
    ) { innerPadding ->
        NavHost(
            navController = navController,
            startDestination = "home",
            modifier = Modifier.padding(innerPadding)
        ) {
            composable("home") { HomeScreen() }
            composable("new_activity") { NewActivityScreen(navController) }
            composable("walk_summary") {
                NewWalkSummaryScreen(navController)
            }
            composable("calendar") {
                val activity = LocalContext.current as ComponentActivity
                CalendarScreen(activity)
            }
            composable("map_screen") {
                val walkViewModel: WalkViewModel = viewModel()
                MapScreen(route = walkViewModel.currentRoute)
            }
            //zebatka ustawienia
            composable("theme_settings") { ThemeSettingsScreen() }
            composable("add_pet") { AddPetScreen(navController) }
            composable("add_friend") { AddFriendScreen(navController) }

        }
    }
}

@Composable
fun HomeScreen(navController: NavHostController = rememberNavController()) {
    val walkViewModel: WalkViewModel = viewModel()
    val walks = walkViewModel.walkList

    var visibleCount by remember { mutableStateOf(15) }

    LaunchedEffect(Unit) {
        walkViewModel.loadWalks()
    }

    ActivityFeed(
        walks = walks.take(visibleCount),
        onClick = { walk ->
            // TODO: przejście do szczegółów aktywności
        },
        onLoadMore = {
            visibleCount += 15
        }
    )
}



@Composable
fun BottomNavigationBar(navController: NavHostController) {
    NavigationBar {
        NavigationBarItem(
            icon = { Icon(Icons.Default.Home, contentDescription = "Home") },
            selected = currentRoute(navController) == "home",
            onClick = { navController.navigate("home") }
        )
        NavigationBarItem(
            icon = { Icon(Icons.Default.Add, contentDescription = "Nowa aktywność") },
            selected = currentRoute(navController) == "new_activity",
            onClick = { navController.navigate("new_activity") }
        )
        NavigationBarItem(
            icon = { Icon(Icons.Filled.CalendarToday, contentDescription = "Kalendarz") },
            selected = currentRoute(navController) == "calendar",
            onClick = { navController.navigate("calendar") }
        )
    }
}

@Composable
fun currentRoute(navController: NavHostController): String? {
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    return navBackStackEntry?.destination?.route
}
