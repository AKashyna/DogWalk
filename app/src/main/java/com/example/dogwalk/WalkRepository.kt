package com.example.dogwalk.data

import android.util.Log
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.android.gms.tasks.Tasks

object WalkRepository {
    private val db = FirebaseFirestore.getInstance()

    fun addWalk(walk: Walk, onSuccess: () -> Unit, onFailure: (Exception) -> Unit) {
        val user = FirebaseAuth.getInstance().currentUser
        if (user == null) {
            onFailure(Exception("Nie zalogowano użytkownika"))
            return
        }

        db.collection("users").document(user.uid).get()
            .addOnSuccessListener { doc ->
                val username = doc.getString("username") ?: user.email?.substringBefore("@") ?: "Nieznajomy"
                val walkWithUser = walk.copy(
                    userId = user.uid,
                    username = username
                )

                db.collection("walks")
                    .add(walkWithUser)
                    .addOnSuccessListener { onSuccess() }
                    .addOnFailureListener { onFailure(it) }
            }
            .addOnFailureListener { onFailure(it) }
    }

    fun getWalks(
        onSuccess: (List<Walk>) -> Unit,
        onFailure: (Exception) -> Unit
    ) {
        val currentUser = FirebaseAuth.getInstance().currentUser ?: return
        val currentUid = currentUser.uid
        val db = FirebaseFirestore.getInstance()

        db.collection("users").document(currentUid).get()
            .addOnSuccessListener { userDoc ->
                val friends = userDoc.get("friends") as? List<String> ?: emptyList()
                val userIds = (friends + currentUid).distinct()

                // Firestore: .whereIn() max 10 elementów
                val limitedUserIds = userIds.take(10)

                db.collection("walks")
                    .whereIn("userId", limitedUserIds)
                    .orderBy("date")
                    .get()
                    .addOnSuccessListener { result ->
                        val walks = result.mapNotNull { doc ->
                            val walk = doc.toObject(Walk::class.java)
                            walk.copy(id = doc.id)
                        }
                        Log.d("WALK_FEED", "Pobrano spacery: ${walks.size}")
                        walks.forEach {
                            Log.d("WALK_FEED", "Spacer: ${it.date} – ${it.username} – ${it.userId}")
                        }
                        onSuccess(walks)
                    }
                    .addOnFailureListener { onFailure(it) }
            }
            .addOnFailureListener { onFailure(it) }
    }



    fun deleteWalk(walkId: String, onSuccess: () -> Unit, onFailure: (Exception) -> Unit) {
        db.collection("walks")
            .document(walkId)
            .delete()
            .addOnSuccessListener { onSuccess() }
            .addOnFailureListener { onFailure(it) }
    }
}
