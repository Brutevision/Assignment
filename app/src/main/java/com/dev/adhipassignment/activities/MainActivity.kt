package com.dev.adhipassignment.activities

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import com.dev.adhipassignment.R
import com.google.firebase.auth.FirebaseAuth

class MainActivity : AppCompatActivity() {
    lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        auth = FirebaseAuth.getInstance()
        val user = auth.currentUser

        /**Shared pref to fetch username**/
        val sharedPreferences = getSharedPreferences("sharedPrefs", Context.MODE_PRIVATE)
        val usernameCheck = sharedPreferences.getString("NICKNAME_KEY", null) //Storing username for check

        /**
         * Conditions to start activities for different cases
         */
        Handler().postDelayed({
            if (user != null && usernameCheck!=null) {
                val homeIntent = Intent(this, HomeActivity::class.java)
                startActivity(homeIntent)
                finish()
            } else if (user != null) {
                val usernameIntent = Intent(this, UsernameActivity::class.java)
                startActivity(usernameIntent)
                finish()
            } else {
                val SignInIntent = Intent(this, SignInActivity::class.java)
                startActivity(SignInIntent)
                finish()
            }}, 2000)
    }
}