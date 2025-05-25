package com.example.dogwalk.ui.feed

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.example.dogwalk.data.Walk

@Composable
fun ActivityCard(
    walk: Walk,
    onClick: (Walk) -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick(walk) },
        colors = CardDefaults.cardColors(
            containerColor = Color(0xFFE0F7FA)
        )
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text("Walk with: ${walk.pets.joinToString(", ")}") // na razie na sztywno
            Text("${walk.date}, ${walk.startTime}")
            Text("Czas trwania: ${walk.durationMinutes} min")
            if (walk.walkedWith.isNotEmpty()) {
                Text("Z przyjaciółmi: ${walk.walkedWith.joinToString(", ")}")
            }

        }
    }
}