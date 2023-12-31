package com.example.servertask.Consumer

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.servertask.R
import com.example.servertask.Start.LoginActivity
import com.example.servertask.UpdateViewInterface
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase


class ConsumerActivity : AppCompatActivity(), UpdateViewInterface {
    // Initialize Firebase Auth here, inside the class scope
    private lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_consumer)

        // Initialize Firebase Auth
        auth = FirebaseAuth.getInstance()

        val addButton: Button = findViewById(R.id.addNameButton)
        addButton.setOnClickListener {
            addName()
        }
    }

    private fun addName() {
        val nameEditText: EditText = findViewById(R.id.nameEditText)
        val enteredName = nameEditText.text.toString().trim()

        val ageEditText: EditText = findViewById(R.id.ageEditText)
        val enteredAge = ageEditText.text.toString().trim()

        val emailEditText: EditText = findViewById(R.id.emailEditText)
        val enteredEmail = emailEditText.text.toString().trim()

        val usernameEditText: EditText = findViewById(R.id.usernameEditText)
        val enteredUsername = usernameEditText.text.toString().trim()

        val passwordEditText: EditText = findViewById(R.id.passwordEditText)
        val enteredPassword = passwordEditText.text.toString().trim()

        // Validate username
        if (enteredUsername.isEmpty()) {
            Toast.makeText(this, "Please enter a username", Toast.LENGTH_SHORT).show()
            return
        }

        // Validate password
        if (enteredPassword.isEmpty()) {
            Toast.makeText(this, "Please enter a password", Toast.LENGTH_SHORT).show()
            return
        }


        // Validate name
        if (enteredName.isEmpty()) {
            Toast.makeText(this, "Please enter a name", Toast.LENGTH_SHORT).show()
            return
        }

        // Validate age
        if (enteredAge.isEmpty() || !enteredAge.matches("\\d+".toRegex())) {
            Toast.makeText(this, "Please enter a valid age", Toast.LENGTH_SHORT).show()
            return
        }

        // Validate email
        if (enteredEmail.isEmpty() || !android.util.Patterns.EMAIL_ADDRESS.matcher(enteredEmail)
                .matches()
        ) {
            Toast.makeText(this, "Please enter a valid email", Toast.LENGTH_SHORT).show()
            return
        }

        val database = FirebaseDatabase.getInstance()
        val consumersRef = database.getReference("Consumers")

        // Create a unique key for each new consumer
        val consumerKey = consumersRef.push().key
        if (consumerKey == null) {
            Log.w("ConsumerActivity", "Couldn't get push key for consumers")
            return
        }

        auth.createUserWithEmailAndPassword(enteredEmail, enteredPassword)
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    // User creation successful, now add additional info to the database
                    val userId = task.result?.user?.uid ?: return@addOnCompleteListener
                    addConsumerInfoToDatabase(
                        userId,
                        enteredName,
                        enteredAge,
                        enteredEmail,
                        enteredUsername,
                        enteredPassword
                    )
                } else {
                    // If sign up fails, display a message to the user
                    val exception = task.exception
                    Toast.makeText(
                        this,
                        "Sign up failed: ${exception?.localizedMessage}",
                        Toast.LENGTH_SHORT
                    ).show()
                    Log.e("ConsumerActivity", "Sign up failed", exception)
                }
            }

    }

    private fun addConsumerInfoToDatabase(
        userId: String,
        name: String,
        age: String,
        email: String,
        username: String,
        password: String
    ) {
        val database = FirebaseDatabase.getInstance()
        val consumersRef = database.getReference("Consumers")

        val consumerData = HashMap<String, Any>().apply {
            put("name", name)
            put("age", age.toInt())
            put("email", email)
            put("username", username)
            put("password", password)
        }

        consumersRef.child(userId).setValue(consumerData)
            .addOnSuccessListener {
                Log.d("ConsumerActivity", "Consumer info added successfully")
                clearInputFields()
                startActivity(Intent(this, LoginActivity::class.java))
            }
            .addOnFailureListener {
                Log.w("ConsumerActivity", "Failed to add consumer info", it)
                Toast.makeText(this, "Failed to add consumer info: ${it.message}", Toast.LENGTH_SHORT).show()
            }
    }

    private fun clearInputFields() {
        // Clear the input fields
        findViewById<EditText>(R.id.nameEditText).text.clear()
        findViewById<EditText>(R.id.ageEditText).text.clear()
        findViewById<EditText>(R.id.emailEditText).text.clear()
        findViewById<EditText>(R.id.usernameEditText).text.clear()
        findViewById<EditText>(R.id.passwordEditText).text.clear()
    }

    override fun updateView(s: String) {
        Log.w("MA", "Inside updateView, s is $s")
        val tv: TextView = findViewById(R.id.tv)
        tv.text = s

        
    }
    override fun runOnUi(runnable: Runnable) {
        runOnUiThread(runnable)
    }


    companion object{

        var address: String = "null"
        var BusinessReceivedOrders: List<String> = listOf()
        var ConsumersSentOrders: List<String> = listOf()
        var businessUsername: String = "null"
        var averageRating: Double? = 0.0
        var businessId: String = "null"
        var consumerUsername: String =  "null"
        var consumerid: String =  "null"

        var URL_PHP_GET : String  = "https://cmsc436-2301.cs.umd.edu/testGet.php?name=Aarti&age=21"
        var URL_PHP_POST_CONSUMER : String  = "https://499aitikira.cs.umd.edu/cgi-bin/add_name.php"
        var URL_PHP_POST_BUSINESS : String  = "https://499aitikira.cs.umd.edu/cgi-bin/addBusiness.php"
        var isBusinessSelected: Boolean = false
        var URL_PHP_GET_BUSINESS : String  = "https://499aitikira.cs.umd.edu/cgi-bin/searchBus.php"
        var URL_PHP_VALIDATE: String = "https://499aitikira.cs.umd.edu/cgi-bin/validate.php"
    }

    /*Look into ListView: present in list form
     */
}