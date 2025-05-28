package com.example.dogwalk.ui.feed

import android.content.Intent
import android.util.Log
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.core.content.ContextCompat.startActivity
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import com.example.dogwalk.WalkViewModel
import com.example.dogwalk.ui.components.TopBarWithLogo

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(
    navController: NavHostController,
    modifier: Modifier = Modifier
) {
    val walkViewModel: WalkViewModel = viewModel()
    val walks = walkViewModel.walkList
    var visibleCount by remember { mutableStateOf(15) }

    LaunchedEffect(Unit) {
        walkViewModel.loadWalks()
    }

    Scaffold(
        topBar = {
            TopBarWithLogo(
                title = "DogWalk",
                showMenu = true,
                navController = navController,
                onMenuItemClick = { route ->
                    navController.navigate(route)
                }
            )
        }
    ) { innerPadding ->

        val context = LocalContext.current
        ActivityFeed(
            walks = walks.take(visibleCount),
            onClick = { walk -> navController.navigate("activityDetail/${walk.id}") },
            onDelete = { walkViewModel.deleteWalk(it) },
            onShare = { walk ->
                val petText = if (walk.pets.isEmpty()) "nikim" else walk.pets.joinToString()
                val friendText = if (walk.walkedWith.isEmpty()) "nikim" else walk.walkedWith.joinToString()
                val message = """
            Poszłam/łem dzisiaj na spacer! Aż ${walk.distanceKm} km!
            Szłam z $petText i $friendText!
            (${walk.username})
        """.trimIndent()

                val intent = Intent(Intent.ACTION_SEND).apply {
                    type = "text/plain"
                    putExtra(Intent.EXTRA_TEXT, message)
                }

                val chooser = Intent.createChooser(intent, "Udostępnij spacer")
                startActivity(context, chooser, null)
            },
            onLoadMore = {
                visibleCount += 10
            },
            modifier = Modifier
                .padding(innerPadding)
                .fillMaxSize()
        )
        LaunchedEffect(walks) {
            Log.d("WALK_UI", "Feed otrzymał spacerów: ${walks.size}")
        }

    }
}
