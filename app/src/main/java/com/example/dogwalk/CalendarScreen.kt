// CalendarScreen.kt

package com.example.dogwalk

import android.Manifest
import android.content.Context
import android.provider.CalendarContract
import android.util.Log
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import android.app.Activity
import android.content.pm.PackageManager
import java.text.SimpleDateFormat
import java.util.*
import java.time.LocalDate
import com.vanpra.composematerialdialogs.MaterialDialog
import com.vanpra.composematerialdialogs.datetime.date.datepicker
import com.vanpra.composematerialdialogs.rememberMaterialDialogState
import com.example.dogwalk.ui.components.TopBarWithLogo
import androidx.compose.ui.platform.LocalContext
import androidx.navigation.NavHostController


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CalendarScreen(activity: Activity, navController: NavHostController) {
    val dialogState = rememberMaterialDialogState()
    var selectedDate by remember { mutableStateOf<LocalDate?>(null) }

    Scaffold(
        topBar = {
            TopBarWithLogo(
                title = "Kalendarz",
                showMenu = true,
                navController = navController,
                onMenuItemClick = { route -> navController.navigate(route) }
            )
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(16.dp)
        ) {
            Text("Wybierz datę z kalendarza", style = MaterialTheme.typography.titleLarge)
            Spacer(modifier = Modifier.height(16.dp))

            Button(onClick = { dialogState.show() }) {
                Text("Pokaż kalendarz")
            }

            selectedDate?.let {
                Spacer(modifier = Modifier.height(8.dp))
                Text("Wybrana data: $it")
            }

            MaterialDialog(
                dialogState = dialogState,
                buttons = {
                    positiveButton("OK")
                    negativeButton("Anuluj")
                }
            ) {
                datepicker { date ->
                    selectedDate = date
                }
            }
        }
    }
}


fun getCalendarEvents(context: Context): List<String> {
    val events = mutableListOf<String>()
    try {
        val projection = arrayOf(
            CalendarContract.Events.TITLE,
            CalendarContract.Events.DTSTART
        )

        val cursor = context.contentResolver.query(
            CalendarContract.Events.CONTENT_URI,
            projection,
            null,
            null,
            CalendarContract.Events.DTSTART + " ASC"
        )

        cursor?.use {
            val titleIndex = it.getColumnIndex(CalendarContract.Events.TITLE)
            val startIndex = it.getColumnIndex(CalendarContract.Events.DTSTART)

            while (it.moveToNext()) {
                val title = it.getString(titleIndex) ?: "Bez tytułu"
                val startMillis = it.getLong(startIndex)
                val date = Date(startMillis)
                val formatted = SimpleDateFormat("dd.MM.yyyy HH:mm", Locale.getDefault()).format(date)
                events.add("$title - $formatted")
            }
        }
    } catch (e: Exception) {
        Log.e("CalendarEvents", "Błąd przy pobieraniu wydarzeń", e)
    }

    return events
}

