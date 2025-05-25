package com.example.dogwalk

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
import androidx.compose.foundation.Image
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.Alignment
import androidx.compose.ui.unit.dp
import androidx.compose.ui.graphics.Color
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.ui.unit.dp
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.draw.scale
import androidx.compose.ui.unit.sp
import androidx.compose.animation.core.*
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.foundation.Image
import androidx.compose.ui.graphics.graphicsLayer
import com.example.dogwalk.ui.login.PawTrail


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
                    val user = auth.currentUser
                    user?.let {
                        val db = Firebase.firestore
                        val userData = hashMapOf(
                            "email" to it.email,
                            "name" to it.displayName
                        )
                        db.collection("users")
                            .document(it.uid)
                            .set(userData)
                            .addOnSuccessListener {
                                Log.d("LOGIN", "Użytkownik zapisany w Firestore")
                            }
                            .addOnFailureListener { e ->
                                Log.e("LOGIN", "Błąd zapisu użytkownika: ${e.localizedMessage}")
                            }
                    }
                    navController.navigate("main") {
                        popUpTo("login") { inclusive = true }
                    }
                }

            }
        }
    }

    Scaffold(
        topBar = { }
    ) { padding ->
        Surface(
            modifier = Modifier
                .padding(padding)
                .fillMaxSize()
        ) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(padding)
            ) {
                PawTrail()

                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(24.dp),
                    verticalArrangement = Arrangement.Center,
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Image(
                        painter = painterResource(id = R.drawable.dw_logo),
                        contentDescription = "DogWalk Logo",
                        modifier = Modifier
                            .size(180.dp)
                            .padding(bottom = 32.dp)
                    )

                    Button(
                        onClick = {
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
                                .addOnSuccessListener {
                                    launcher.launch(
                                        IntentSenderRequest.Builder(it.pendingIntent).build()
                                    )
                                }
                                .addOnFailureListener { e ->
                                    loading = false
                                    Log.e("LOGIN", "Błąd logowania: ${e.localizedMessage}")
                                }
                        },
                        enabled = !loading
                    ) {
                        if (loading) {
                            CircularProgressIndicator(
                                color = MaterialTheme.colorScheme.onPrimary,
                                modifier = Modifier.size(20.dp),
                                strokeWidth = 2.dp
                            )
                            Spacer(Modifier.width(8.dp))
                            Text("Logowanie...")
                        } else {
                            Text("Zaloguj się przez Google")
                        }
                    }
                }
            }
        }
    }
}




