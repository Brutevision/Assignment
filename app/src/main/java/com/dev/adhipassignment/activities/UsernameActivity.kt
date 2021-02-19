package com.dev.adhipassignment.activities

import android.content.Context
import android.content.Intent
import android.graphics.Typeface
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.text.SpannableString
import android.text.Spanned
import android.text.style.StyleSpan
import android.widget.Toast
import com.dev.adhipassignment.R
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.UserProfileChangeRequest
import kotlinx.android.synthetic.main.activity_home.*
import kotlinx.android.synthetic.main.activity_username.*

class UsernameActivity : AppCompatActivity() {
    lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_username)
        auth = FirebaseAuth.getInstance()

        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        val mText = "Hi, what do your\nfriends call you?"
        val mBSpannableString = SpannableString(mText)
        val mBold = StyleSpan(Typeface.BOLD)
        mBSpannableString.setSpan(mBold, 0, 34, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE)
        tvUsername.text = mBSpannableString

        /**---Continue---**/
        btUsername.setOnClickListener {
            updateProfile()
            saveDate()

            /** Slight delay in username update time */
            Handler().postDelayed({
                if (etUsername.text.isNotEmpty()) {
                    val homeIntent = Intent(this@UsernameActivity, HomeActivity::class.java)
                    startActivity(homeIntent)
                    finish()
                } else {
                    Toast.makeText(this@UsernameActivity, "Give us a name", Toast.LENGTH_SHORT)
                        .show()
                }
            }, 1500)
        }

        /**---Back---**/
        ivBack.setOnClickListener {
            finish()
        }
    }

    /**Funtion to update the username of google account user**/
    private fun updateProfile() {
        val user = auth.currentUser
        if (user != null) {
            auth.currentUser?.let {
                val username = etUsername.text.toString()
                val profileUpdates = UserProfileChangeRequest.Builder()
                    .setDisplayName(username)
                    .build()

                user.updateProfile(profileUpdates)
            }
        }
    }

    /**Funtion with shared pref to check whether username was given or not**/
    private fun saveDate() {
        val nickName = etUsername.text.toString()
        val sharedPreferences = getSharedPreferences("sharedPrefs", Context.MODE_PRIVATE)
        val editor = sharedPreferences.edit()
        editor.apply {
            putString("NICKNAME_KEY", nickName)
        }.apply()
    }
}