package com.example.dogwalk.data

import com.google.firebase.firestore.FirebaseFirestore

object WalkRepository {
    private val db = FirebaseFirestore.getInstance()

    fun addWalk(walk: Walk, onSuccess: () -> Unit, onFailure: (Exception) -> Unit) {
        db.collection("walks")
            .add(walk)
            .addOnSuccessListener { onSuccess() }
            .addOnFailureListener { e -> onFailure(e) }
    }
}
