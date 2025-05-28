package com.example.dogwalk.data

import com.example.dogwalk.ui.settings.Pet
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.SetOptions
import kotlinx.coroutines.tasks.await

object PetRepository {
    private val db = FirebaseFirestore.getInstance()
    private val user = FirebaseAuth.getInstance().currentUser

    suspend fun getPets(): List<Pet> {
        val doc = db.collection("users").document(user!!.uid).get().await()
        val petsRaw = doc.get("pets") as? List<Map<String, Any>> ?: emptyList()
        return petsRaw.map {
            Pet(
                type = it["type"]?.toString().orEmpty(),
                breed = it["breed"]?.toString().orEmpty(),
                name = it["name"]?.toString().orEmpty()
            )
        }
    }

    fun addPet(pet: Pet, onSuccess: () -> Unit, onFailure: (Exception) -> Unit) {
        db.collection("users")
            .document(user!!.uid)
            .set(
                mapOf("pets" to FieldValue.arrayUnion(pet)),
                SetOptions.merge()
            )
            .addOnSuccessListener { onSuccess() }
            .addOnFailureListener { onFailure(it) }
    }

    fun removePet(pet: Pet, onSuccess: () -> Unit, onFailure: (Exception) -> Unit) {
        db.collection("users")
            .document(user!!.uid)
            .update("pets", FieldValue.arrayRemove(pet))
            .addOnSuccessListener { onSuccess() }
            .addOnFailureListener { onFailure(it) }
    }

}
