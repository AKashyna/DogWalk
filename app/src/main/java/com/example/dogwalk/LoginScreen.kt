package com.example.dogwalk.ui

import android.app.Activity
import android.content.Intent
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.navigation.NavController
import com.google.android.gms.auth.api.identity.BeginSignInRequest
import com.google.android.gms.auth.api.identity.Identity
import com.google.android.gms.auth.api.identity.SignInClient
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.GoogleAuthProvider
import androidx.activity.result.IntentSenderRequest
import android.util.Log
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase




@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LoginScreen(navController: NavController) {
    val context = LocalContext.current
    val activity = context as Activity
    val auth = FirebaseAuth.getInstance()
    val oneTapClient = Identity.getSignInClient(context)

    var loading by remember { mutableStateOf(false) }

    val launcher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.StartIntentSenderForResult()
    )
    { result ->
        val credential = oneTapClient.getSignInCredentialFromIntent(result.data)
        val idToken = credential.googleIdToken

        if (idToken != null) {
            val firebaseCredential = GoogleAuthProvider.getCredential(idToken, null)
            auth.signInWithCredential(firebaseCredential).addOnCompleteListener(activity) { task ->
                if (task.isSuccessful) {
                    navController.navigate("main") {
                        popUpTo("login") { inclusive = true }
                    }
                }
            }
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(title = { Text("Logowanie") })
        }
    ) { padding ->
        Surface(modifier = Modifier.padding(padding)) {
            Button(
                onClick = {
                    Log.d("LOGIN", "Kliknięto przycisk logowania")
                    loading = true
                    val signInRequest = BeginSignInRequest.builder()
                        .setGoogleIdTokenRequestOptions(
                            BeginSignInRequest.GoogleIdTokenRequestOptions.builder()
                                .setSupported(true)
                                .setServerClientId("55633790249-inb4tdvcfbc13baje4091ha12k97mj5k.apps.googleusercontent.com")
                                .setFilterByAuthorizedAccounts(false)
                                .build()
                        )
                        .build()

                    oneTapClient.beginSignIn(signInRequest)
                        .addOnSuccessListener { result ->
                            launcher.launch(IntentSenderRequest.Builder(result.pendingIntent).build())
                        }
                        .addOnFailureListener { e ->
                            loading = false
                            Log.e("LOGIN", "Błąd logowania: ${e.localizedMessage}")
                        }

                }
            ) {
                Text(if (loading) "Ładowanie..." else "Zaloguj się przez Google")
            }
        }
    }
}
