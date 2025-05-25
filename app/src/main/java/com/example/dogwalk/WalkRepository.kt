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

    fun getWalks(
        onSuccess: (List<Walk>) -> Unit,
        onFailure: (Exception) -> Unit
    ) {
        db.collection("walks")
            .orderBy("date") // jeśli chcesz sortowanie
            .limit(50)
            .get()
            .addOnSuccessListener { result ->
                val walks = result.documents.mapNotNull { it.toObject(Walk::class.java) }
                onSuccess(walks)
            }
            .addOnFailureListener { onFailure(it) }
    }

}
