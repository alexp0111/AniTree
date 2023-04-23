package com.ikbo0621.anitree.data

import android.content.Context
import android.content.SharedPreferences
import android.graphics.Point
import com.ikbo0621.anitree.R
import com.ikbo0621.anitree.tree.positioning.RValue

object DataController {
    val dimens = hashMapOf(
        "text_size" to RValue(0.5f, RValue.Type.Y)
    )

    private const val DIMENS_STORAGE_NAME = "dimens"
    private lateinit var settings: SharedPreferences
    private lateinit var editor: SharedPreferences.Editor
    private val absoluteDimens = hashMapOf<String, Float>()
    private var isChanged = false

    fun init(context: Context, screenSize: Point) {
        settings = context.getSharedPreferences(DIMENS_STORAGE_NAME, Context.MODE_PRIVATE)
        editor = settings.edit()

        calculateAllDimens(screenSize)
        if (isChanged)
            saveAllDimens()
    }

    fun getDimen(key: String) : Float? {
        return absoluteDimens[key]
    }

    private fun calculateAllDimens(screenSize: Point) {
        for (i in dimens) {
            var absoluteDimen = loadFloat(i.key)

            if (absoluteDimen == null) {
                isChanged = true
                absoluteDimen = i.value.getAbsolute(screenSize)
            }

            absoluteDimens[i.key] = absoluteDimen
        }
    }

    private fun saveAllDimens() {
        for (i in absoluteDimens) {
            saveFloat(i.key, i.value)
        }
        editor.commit()
    }

    private fun loadFloat(key: String) : Float? {
        return try {
            val result = settings.getFloat(key, 0f)
            if (result == 0f) null else result
        } catch (e: java.lang.ClassCastException) {
            e.printStackTrace()
            null
        }
    }

    private fun saveFloat(key: String, dimen: Float) {
        editor.putFloat(key, dimen)
    }
}