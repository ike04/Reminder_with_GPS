package com.google.codelab.reminderwithgps.activity

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.MotionEvent
import android.view.inputmethod.InputMethodManager
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import com.google.codelab.reminderwithgps.R

class LoginActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        supportActionBar?.setTitle(R.string.login)

        val loginBtn = findViewById<Button>(R.id.loginBtn)
        val signInBtn = findViewById<Button>(R.id.signInBtn)

        loginBtn.setOnClickListener {
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
            finish()
        }

        signInBtn.setOnClickListener {
            val intent = Intent(this, SignInActivity::class.java)
            startActivity(intent)
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
