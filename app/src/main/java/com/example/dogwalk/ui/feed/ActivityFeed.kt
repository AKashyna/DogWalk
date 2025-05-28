package com.example.dogwalk.ui.feed

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.dogwalk.data.Walk
import com.google.firebase.auth.FirebaseAuth

@Composable
fun ActivityFeed(
    walks: List<Walk>,
    onClick: (Walk) -> Unit,
    onDelete: (Walk) -> Unit,
    onShare: (Walk) -> Unit,
    onLoadMore: () -> Unit,
    modifier: Modifier = Modifier
) {
    val currentUid = FirebaseAuth.getInstance().currentUser?.uid

    LazyColumn(
        modifier = modifier.fillMaxSize(),
        contentPadding = PaddingValues(16.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        items(walks) { walk ->
            ActivityCard(
                walk = walk,
                //onClick = onClick,
                onDelete = onDelete,
                onShare = onShare
            )

        }
        if (walks.isEmpty()) {
            item {
                Text("Brak spacerów do wyświetlenia.")
            }
        }


        item {
            Button(
                onClick = onLoadMore,
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Załaduj więcej")
            }
        }
    }
}
