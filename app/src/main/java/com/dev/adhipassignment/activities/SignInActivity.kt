package com.dev.adhipassignment.activities

import android.content.Intent
import android.graphics.Typeface
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.SpannableString
import android.text.Spanned
import android.text.style.StyleSpan
import android.widget.Toast
import com.dev.adhipassignment.R
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.GoogleAuthProvider
import kotlinx.android.synthetic.main.activity_sign_in.*
import kotlinx.android.synthetic.main.activity_username.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext
import java.lang.Exception

const val REQUEST_CODE_SIGN_IN = 0

class SignInActivity : AppCompatActivity() {
    lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sign_in)

        auth = FirebaseAuth.getInstance()

        /**---Google---**/
        btSignIn.setOnClickListener {
            val options = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.webclient_id))
                .requestEmail()
                .build()

            //google popUp with id's
            val signInClient = GoogleSignIn.getClient(this, options)
            signInClient.signInIntent.also {
                startActivityForResult(it, REQUEST_CODE_SIGN_IN)
            }
        }

        /**---Guest---**/
        btGuest.setOnClickListener {
            val signInIntent = Intent(this@SignInActivity, UsernameActivity::class.java)
            startActivity(signInIntent)
        }

        val mText = "Make meditation a\n     habit you love"
        val mBSpannableString = SpannableString(mText)
        val mBold = StyleSpan(Typeface.BOLD)
        mBSpannableString.setSpan(mBold, 0, 37, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE)
        tvSignIn.text = mBSpannableString

    }

    private fun googleAuthForFirebase(account: GoogleSignInAccount) {
        var credentials = GoogleAuthProvider.getCredential(account.idToken, null)
        CoroutineScope(Dispatchers.IO).launch {
            try {
                //signing In with these credentials
                auth.signInWithCredential(credentials).await()
                withContext(Dispatchers.Main) {
                    val signInIntent = Intent(this@SignInActivity, UsernameActivity::class.java)
                    startActivity(signInIntent)
                }
            } catch (e: Exception) {
                withContext(Dispatchers.Main) {
                    Toast.makeText(this@SignInActivity, e.message, Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == REQUEST_CODE_SIGN_IN) {
            try {
                //this is the selected account from popUp
                val account =
                    GoogleSignIn.getSignedInAccountFromIntent(data).result
                /**currently this account is just a google account and not connected to our firebase yet.*/

                account?.let {
                    googleAuthForFirebase(it) // this fun will connect the account with this firebase project.
                }
            } catch (e: Exception) {
                Toast.makeText(this@SignInActivity, "Account not selected", Toast.LENGTH_SHORT)
                    .show()
            }
        }
    }
}