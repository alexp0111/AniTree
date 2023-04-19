package com.ikbo0621.anitree

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.ikbo0621.anitree.testUI.LogInFragment
import dagger.hilt.android.AndroidEntryPoint


/**
 * Main class.
 */

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        supportFragmentManager.beginTransaction()
            .replace(R.id.fragment_container_view, LogInFragment())
            .commitAllowingStateLoss()
    }
}