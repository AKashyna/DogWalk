package com.example.dogwalk.data

import com.example.dogwalk.ui.settings.Friend
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.tasks.await

object FriendRepository {
    private val db = FirebaseFirestore.getInstance()
    private val user = FirebaseAuth.getInstance().currentUser

    suspend fun getFriends(): List<Friend> {
        val doc = db.collection("users").document(user!!.uid).get().await()
        val friendUids = doc.get("friends") as? List<String> ?: emptyList()
        val friends = mutableListOf<Friend>()

        for (uid in friendUids) {
            val friendDoc = db.collection("users").document(uid).get().await()
            val username = friendDoc.getString("username") ?: "Nieznajomy"
            friends.add(Friend(uid, username))

        }

        return friends
    }

    fun addFriend(uid: String, onSuccess: () -> Unit, onFailure: (Exception) -> Unit) {
        db.collection("users")
            .document(user!!.uid)
            .update("friends", FieldValue.arrayUnion(uid))
            .addOnSuccessListener { onSuccess() }
            .addOnFailureListener { onFailure(it) }
    }

    fun removeFriend(uid: String, onSuccess: () -> Unit, onFailure: (Exception) -> Unit) {
        db.collection("users")
            .document(user!!.uid)
            .update("friends", FieldValue.arrayRemove(uid))
            .addOnSuccessListener { onSuccess() }
            .addOnFailureListener { onFailure(it) }
    }

}
