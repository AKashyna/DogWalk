package com.example.dogwalk


import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import com.example.dogwalk.data.WalkRepository
import com.google.android.gms.maps.model.LatLng
import com.example.dogwalk.data.Walk
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue


class WalkViewModel : ViewModel() {
    var currentRoute: List<LatLng> = emptyList()

    var walkList by mutableStateOf<List<Walk>>(emptyList())
        private set

    fun loadWalks() {
        WalkRepository.getWalks(
            onSuccess = { walks -> walkList = walks },
            onFailure = { /* obsłuż błąd */ }
        )
    }
}
