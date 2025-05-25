package com.example.dogwalk.data

data class Walk(
    val userId: String = "",
    val pets: List<String> = emptyList(),          // ← imiona twoich zwierząt
    val date: String = "",
    val startTime: String = "",
    val durationMinutes: Int = 0,
    val distanceKm: Double = 0.0,
    val walkedWith: List<String> = emptyList()     // ← imiona innych psów (znajomych)
)