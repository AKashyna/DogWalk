package com.example.dogwalk.ui.feed

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.example.dogwalk.data.Walk
import android.content.Intent
import android.net.Uri
import androidx.compose.ui.platform.LocalContext
import androidx.core.content.ContextCompat.startActivity
import androidx.compose.material.icons.filled.Share


@Composable
fun ActivityCard(
    walk: Walk,
    //onClick: (Walk) -> Unit,
    onDelete: (Walk) -> Unit,
    onShare: (Walk) -> Unit
) {
    val currentUid = com.google.firebase.auth.FirebaseAuth.getInstance().currentUser?.uid

    Card(
        modifier = Modifier
            .fillMaxWidth(),
            //.clickable { onClick(walk) },
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.secondaryContainer
        )
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Row(
                horizontalArrangement = Arrangement.SpaceBetween,
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("👤 ${walk.username}", style = MaterialTheme.typography.titleMedium)
                if (walk.userId == currentUid) {
                    Row {
                        IconButton(onClick = { onDelete(walk) }) {
                            Icon(Icons.Default.Delete, contentDescription = "Usuń spacer")
                        }
                        IconButton(onClick = { onShare(walk) }) {
                            Icon(Icons.Default.Share, contentDescription = "Udostępnij spacer")
                        }
                    }
                }
            }

            Text("📅 ${walk.date}, ${walk.startTime}")
            Text("📏 Dystans: ${walk.distanceKm} km")
            Text("⏱️ Czas trwania: ${walk.durationMinutes} min")

            if (walk.pets.isNotEmpty()) {
                Text("🐾 Zwierzaki: ${walk.pets.joinToString(", ")}")
            }
            if (walk.walkedWith.isNotEmpty()) {
                Text("👥 Towarzysze: ${walk.walkedWith.joinToString(", ")}")
            }
        }
    }
}
