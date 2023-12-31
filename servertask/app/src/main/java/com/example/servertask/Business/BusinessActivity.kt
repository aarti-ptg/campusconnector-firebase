package com.example.servertask.Business

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.servertask.R
import com.example.servertask.Start.LoginActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase

class BusinessActivity : AppCompatActivity() {

    private lateinit var auth: FirebaseAuth  // Initialize Firebase Auth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_business)

        auth = FirebaseAuth.getInstance()  // Initialize Firebase Auth

        val addButton: Button = findViewById(R.id.addNameButton)
        addButton.setOnClickListener {
            addBusiness()
        }
    }

    private fun addBusiness(){
        val nameEditText: EditText = findViewById(R.id.nameEditText)
        val enteredName = nameEditText.text.toString().trim()

        val phoneEditText: EditText = findViewById(R.id.phoneEditText)
        val enteredPhone = phoneEditText.text.toString().trim()

        val emailEditText: EditText = findViewById(R.id.emailEditText)
        val enteredEmail = emailEditText.text.toString().trim()

        val usernameEditText: EditText = findViewById(R.id.usernameEditText)
        val enteredusername = usernameEditText.text.toString().trim()

        val passwordEditText: EditText = findViewById(R.id.passwordEditText)
        val enteredpassword = passwordEditText.text.toString().trim()

        val AddressEditText: EditText = findViewById(R.id.AddressEditText)
        val enteredAddress = AddressEditText.text.toString().trim()


        // Validate name
        if (enteredName.isEmpty()) {
            Toast.makeText(this, "Please enter a name", Toast.LENGTH_SHORT).show()
            return
        }

        // Validate phone
        if (enteredPhone.isEmpty() || !enteredPhone.matches("\\d+".toRegex())) {
            Toast.makeText(this, "Please enter a valid phone number", Toast.LENGTH_SHORT).show()
            return
        }

        // Validate email
        if (enteredEmail.isEmpty() || !android.util.Patterns.EMAIL_ADDRESS.matcher(enteredEmail).matches()) {
            Toast.makeText(this, "Please enter a valid email", Toast.LENGTH_SHORT).show()
            return
        }

        // Validate address
        if (enteredAddress.isEmpty()) {
            Toast.makeText(this, "Please enter an address", Toast.LENGTH_SHORT).show()
            return
        }


        auth.createUserWithEmailAndPassword(enteredEmail, enteredpassword)
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    // Registration successful, now add business information to the database
                    val businessId = task.result?.user?.uid ?: return@addOnCompleteListener
                    addBusinessInfoToDatabase(businessId, enteredName, enteredPhone, enteredEmail, enteredusername, enteredAddress)
                } else {
                    // If registration fails, display a message to the user
                    task.exception?.message?.let {
                        Toast.makeText(this, "Registration failed: $it", Toast.LENGTH_SHORT).show()
                    }
                }
            }
    }

    private fun addBusinessInfoToDatabase(businessId: String, name: String, phone: String, email: String, username: String, address: String) {
        val database = FirebaseDatabase.getInstance()
        val businessRef = database.getReference("Businesses")

        val businessData = HashMap<String, Any>().apply {
            put("name", name)
            put("phone_number", phone)
            put("email", email)
            put("username", username)
            put("address", address)
        }

        businessRef.child(businessId).setValue(businessData)
            .addOnSuccessListener {
                Log.d("BusinessActivity", "Business info added successfully")
                startActivity(Intent(this, LoginActivity::class.java))
            }
            .addOnFailureListener {
                Toast.makeText(this, "Failed to add business info: ${it.message}", Toast.LENGTH_SHORT).show()
            }
    }



}
