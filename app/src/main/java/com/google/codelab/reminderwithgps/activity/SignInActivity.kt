package com.google.codelab.reminderwithgps.activity

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.view.MotionEvent
import android.view.inputmethod.InputMethodManager
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.codelab.reminderwithgps.R
import com.google.codelab.reminderwithgps.utils.ValidationUtils
import com.google.codelab.reminderwithgps.utils.showAlertDialog
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase

class SignInActivity : AppCompatActivity() {
    private lateinit var auth: FirebaseAuth
    private val TAG = this::class.java.simpleName

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sign_in)

        supportActionBar?.let {
            it.setTitle(R.string.sign_up)
            it.setDisplayHomeAsUpEnabled(true)
        }

        // Initialize Firebase Auth
        auth = Firebase.auth
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_sign_in, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.sign_in_button -> {
                val email = findViewById<TextView>(R.id.signInEmailEditText).text.toString()
                val password = findViewById<TextView>(R.id.signInPasswordEditText).text.toString()
                val passwordConfirm =
                    findViewById<TextView>(R.id.signInPasswordConfirmEditText).text.toString()

                val errorMessage = ValidationUtils.checkSignUp(email, password, passwordConfirm)

                if (errorMessage == null) {
                    signUp(email, password)
                } else {
                    showAlertDialog(R.string.warning, errorMessage)
                }

                return true
            }
            android.R.id.home -> {
                finish()
                return true
            }
        }
        return false
    }

    private fun signUp(email: String, password: String) {
        auth.createUserWithEmailAndPassword(email, password)
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    // Sign in success, update UI with the signed-in user's information
                    Log.d(TAG, "createUserWithEmail:success")

                    val intent = Intent(this, MainActivity::class.java)
                    startActivity(intent)
                    finish()
                } else {
                    // If sign in fails, display a message to the user.
                    Log.w(TAG, "createUserWithEmail:failure", task.exception)
                    Toast.makeText(
                        baseContext, "Authentication failed.",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }
    }

    override fun onTouchEvent(event: MotionEvent?): Boolean {
        val imm: InputMethodManager =
            getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        val focus = currentFocus ?: return false

        imm.hideSoftInputFromWindow(
            focus.windowToken,
            InputMethodManager.HIDE_NOT_ALWAYS
        )
        return false
    }
}
