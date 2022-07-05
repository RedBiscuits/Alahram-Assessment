package com.datastructures.pairingtask.ui

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.datastructures.pairingtask.R

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val loginFragment = LoginFragment()
        supportFragmentManager.beginTransaction().apply {
            replace(R.id.flFragments , loginFragment)
            commit()
        }

    }
}