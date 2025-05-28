package com.example.dogwalk.ui.settings

import androidx.compose.runtime.*
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.dogwalk.data.PetRepository
import com.example.dogwalk.data.FriendRepository
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await

data class Pet(val type: String = "", val breed: String = "", val name: String = "")
data class Friend(val uid: String, val username: String)

class ProfileViewModel : ViewModel() {

    var username by mutableStateOf("")
        private set

    var email by mutableStateOf("")
        private set

    var pets by mutableStateOf(listOf<Pet>())
        private set

    var friends by mutableStateOf(listOf<Friend>())
        private set

    private val db = Firebase.firestore
    private val user = FirebaseAuth.getInstance().currentUser

    fun loadUserData() {
        val uid = user?.uid ?: return

        viewModelScope.launch {
            email = user.email ?: ""

            val doc = db.collection("users").document(uid).get().await()
            username = doc.getString("username") ?: email.substringBefore("@")

            pets = PetRepository.getPets()
            friends = FriendRepository.getFriends()
        }
    }

    fun updateUsername(newName: String) {
        val uid = user?.uid ?: return

        viewModelScope.launch {
            db.collection("users").document(uid)
                .update("username", newName)
                .addOnSuccessListener {
                    username = newName
                }
        }
    }

    fun removePet(pet: Pet) {
        PetRepository.removePet(
            pet,
            onSuccess = { loadUserData() },
            onFailure = { /* TODO: obsługa błędu */ }
        )
    }

    fun removeFriend(uid: String) {
        FriendRepository.removeFriend(
            uid,
            onSuccess = { loadUserData() },
            onFailure = { /* TODO: obsługa błędu */ }
        )
    }
}
