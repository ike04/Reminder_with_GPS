package com.google.codelab.reminderwithgps

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.bottomnavigation.BottomNavigationView

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        supportFragmentManager
            .beginTransaction()
            .replace(R.id.activity_main, RemindListFragment())
            .commit()

        val menuBottomNavigationBar: BottomNavigationView = findViewById(R.id.bottom_navigation)
        menuBottomNavigationBar.setOnNavigationItemSelectedListener(onNavigationItemSelectedListener)

    }

    private val onNavigationItemSelectedListener: BottomNavigationView.OnNavigationItemSelectedListener =
        BottomNavigationView.OnNavigationItemSelectedListener { item ->
            when (item.itemId) {
                R.id.navi_book_list -> {
                    supportFragmentManager
                        .beginTransaction()
                        .replace(R.id.activity_main, RemindListFragment())
                        .commit()
                    true
                }
                R.id.navi_setting -> {

                    true
                }
                else -> false
            }
        }
}
