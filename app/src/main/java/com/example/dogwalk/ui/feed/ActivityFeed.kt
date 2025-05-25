package com.example.dogwalk.ui.feed

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.dogwalk.data.Walk

@Composable
fun ActivityFeed(
    walks: List<Walk>,
    onClick: (Walk) -> Unit,
    onLoadMore: () -> Unit
) {
    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        contentPadding = PaddingValues(16.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        items(walks.take(15)) { walk ->
            ActivityCard(walk = walk, onClick = onClick)
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