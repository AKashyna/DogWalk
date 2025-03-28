package com.example.dogwalk.data

data class Walk(
    val userId: String = "",
    val date: String = "",
    val startTime: String = "",
    val durationMinutes: Int = 0,
    val distanceKm: Double = 0.0,
    val walkedWith: String = ""
)