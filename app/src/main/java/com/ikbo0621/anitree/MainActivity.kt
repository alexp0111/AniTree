package com.ikbo0621.anitree

import android.graphics.Point
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.ikbo0621.anitree.data.DimensController
import com.ikbo0621.anitree.data.Layout
import com.ikbo0621.anitree.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        DimensController.init(
            applicationContext,
            Layout(),
            Point(
                resources.displayMetrics.widthPixels,
                resources.displayMetrics.heightPixels
            )
        )

        // To dimens.xml (just stubs)
//        DimensController.getDimen("text_size") // 358.8
//        DimensController.getDimen("small_text_size") // 179.4
//        val a = DimensController.getDimen("line_width") // 17.94

        DimensController.setDimen(binding.textView, "text_size", R.dimen.text_size)
        DimensController.setDimen(
            binding.textView2, "small_text_size", R.dimen.small_text_size
        )
        binding.stroke.layoutParams = binding.stroke.layoutParams.apply {
            height = DimensController.getDimen("line_width", R.dimen.line_width).toInt()
        }
    }
}