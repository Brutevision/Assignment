package com.dev.adhipassignment.activities

import android.graphics.Typeface
import android.os.Bundle
import android.text.SpannableString
import android.text.Spanned
import android.text.style.StyleSpan
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.dev.adhipassignment.R
import com.google.firebase.auth.FirebaseAuth
import kotlinx.android.synthetic.main.activity_home.*


class HomeActivity : AppCompatActivity() {
    lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)
        auth = FirebaseAuth.getInstance()
        val user = auth.currentUser

        //Log.d(TAG, user?.displayName.toString())
        if (user!=null){
            Toast.makeText(this, "Welcome ${user?.displayName}", Toast.LENGTH_LONG).show()
        }


        val mText = "A more mindful you"
        val mBSpannableString = SpannableString(mText)
        val mBold = StyleSpan(Typeface.BOLD)
        mBSpannableString.setSpan(mBold, 0, 18, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE)
        textView1.text = mBSpannableString
    }

    override fun onBackPressed() {
        finishAffinity()
    }
}