package com.ikbo0621.anitree

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.ikbo0621.anitree.testUI.LogInFragment


/**
 * Main class.
 */

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        supportFragmentManager.beginTransaction()
            .replace(R.id.fragment_container_view, LogInFragment())
            .commitAllowingStateLoss()

        //TODO:
        // 1. Check for SESSION logic
        // 2. DI realisation
        // 3. Dagger annotations
        // 4. UserModel methods realisation
    }
}