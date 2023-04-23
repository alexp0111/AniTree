package com.ikbo0621.anitree

import android.graphics.Bitmap
import android.graphics.Point
import android.os.Bundle
import android.util.TypedValue
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.ikbo0621.anitree.data.DataController
import com.ikbo0621.anitree.databinding.ActivityMainBinding


class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private val bitmaps = ArrayList<Bitmap>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        DataController.init(
            applicationContext,
            Point(
                resources.displayMetrics.widthPixels,
                resources.displayMetrics.heightPixels
            )
        )

        setTextSize(binding.textView)
    }

    private fun setTextSize(textView: TextView) {
        textView.setTextSize(TypedValue.COMPLEX_UNIT_PX,
            DataController.getDimen("text_size") ?:
                resources.getDimension(R.dimen.text_size)
        )
    }
}