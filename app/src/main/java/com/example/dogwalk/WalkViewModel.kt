package com.example.dogwalk

import androidx.lifecycle.ViewModel
import androidx.compose.runtime.*
import com.example.dogwalk.data.Walk
import com.example.dogwalk.data.WalkRepository
import com.google.android.gms.maps.model.LatLng
import android.util.Log

class WalkViewModel : ViewModel() {
    var currentRoute: List<LatLng> = emptyList()

    var walkList by mutableStateOf<List<Walk>>(emptyList())
        private set

    fun loadWalks() {
        WalkRepository.getWalks(
            onSuccess = { walks ->
                Log.d("WALK_VM", "Ustawiam spacerów: ${walks.size}")
                walkList = walks
            },
            onFailure = { e ->
                Log.e("WALK_VM", "Błąd ładowania spacerów", e)
            }
        )
    }

    fun deleteWalk(walk: Walk) {
        WalkRepository.deleteWalk(
            walkId = walk.id,
            onSuccess = { loadWalks() },
            onFailure = { e ->
                Log.e("WALK_VM", "Błąd usuwania spaceru", e)
            }
        )
    }
}
