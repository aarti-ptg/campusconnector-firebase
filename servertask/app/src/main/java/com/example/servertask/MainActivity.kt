package com.example.servertask

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.servertask.R
import com.google.firebase.auth.FirebaseAuth

class MainActivity : AppCompatActivity() {

    // Instance of FirebaseAuth
    private lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_signup)

        // Initialize Firebase Auth
        auth = FirebaseAuth.getInstance()
    }

    // Function to create a new user with email and password
    private fun createUser(email: String, password: String) {
        auth.createUserWithEmailAndPassword(email, password)
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    // Sign in success, update UI with the signed-in user's information
                    val user = auth.currentUser
                    // You can now create a user record in your Realtime Database if needed
                } else {
                    // If sign in fails, display a message to the user.
                    // ...
                }
            }
    }

    // Function to sign in a user with email and password
    private fun signInUser(email: String, password: String) {
        auth.signInWithEmailAndPassword(email, password)
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    // Sign in success, update UI with the signed-in user's information
                    val user = auth.currentUser
                    // User is now logged in and you can redirect them to your app's main page or perform other actions
                } else {
                    // If sign in fails, display a message to the user.
                    // ...
                }
            }
    }
}
