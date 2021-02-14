package com.google.codelab.reminderwithgps.activity

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.MotionEvent
import android.view.inputmethod.InputMethodManager
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.google.codelab.reminderwithgps.R
import com.google.codelab.reminderwithgps.utils.ValidationUtils
import com.google.codelab.reminderwithgps.utils.showAlertDialog

class SignInActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sign_in)

        supportActionBar?.let {
            it.setTitle(R.string.sign_up)
            it.setDisplayHomeAsUpEnabled(true)
        }
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
                    val intent = Intent(this, MainActivity::class.java)
                    startActivity(intent)
                    finish()
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
