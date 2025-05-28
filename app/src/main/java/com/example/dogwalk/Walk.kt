package com.example.dogwalk.data

data class Walk(
    val id: String = "",
    val userId: String = "",
    val username: String = "",
    val pets: List<String> = emptyList(),
    val date: String = "",
    val startTime: String = "",
    val durationMinutes: Int = 0,
    val distanceKm: Double = 0.0,
    val walkedWith: List<String> = emptyList()
)
