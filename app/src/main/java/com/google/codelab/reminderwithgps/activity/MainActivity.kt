package com.google.codelab.reminderwithgps.activity

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.codelab.reminderwithgps.fragment.MapFragment
import com.google.codelab.reminderwithgps.R
import com.google.codelab.reminderwithgps.fragment.RemindListFragment

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
                R.id.navi_remind_list -> {
                    supportFragmentManager
                        .beginTransaction()
                        .replace(R.id.activity_main, RemindListFragment())
                        .commit()
                    true
                }
                R.id.navi_map -> {
                    supportFragmentManager
                        .beginTransaction()
                        .replace(R.id.activity_main, MapFragment())
                        .commit()
                    true
                }
                else -> false
            }
        }
}
