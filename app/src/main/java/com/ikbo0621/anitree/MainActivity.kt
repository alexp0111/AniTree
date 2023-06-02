package com.ikbo0621.anitree

import android.graphics.Point
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.fragment.NavHostFragment
import com.ikbo0621.anitree.data.DimensController
import com.ikbo0621.anitree.data.Layout
import dagger.hilt.android.AndroidEntryPoint


/**
 * Main class.
 */

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        DimensController.init(
            applicationContext,
            Layout(),
            Point(
                resources.displayMetrics.widthPixels,
                resources.displayMetrics.heightPixels
            )
        )

        // Keep this code
        // Actual

        val navHostFragment =
            supportFragmentManager.findFragmentById(R.id.nav_host_fragment) as NavHostFragment
        val navController = navHostFragment.navController

        // Keep this code
    }
}