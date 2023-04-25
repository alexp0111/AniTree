package com.ikbo0621.anitree.data

import android.content.Context
import android.content.SharedPreferences
import android.graphics.Point
import android.util.TypedValue
import android.widget.TextView
import java.lang.ref.WeakReference

object DimensController {
    private lateinit var contextRef: WeakReference<Context>
    private lateinit var layout: RelativeValuesLayout

    private const val DIMENS_STORAGE_NAME = "dimens"
    private lateinit var settings: SharedPreferences
    private lateinit var editor: SharedPreferences.Editor
    private val absoluteDimens = hashMapOf<String, Float>()
    private var isChanged = false

    fun init(context: Context, layout: RelativeValuesLayout, screenSize: Point) {
        this.layout = layout
        contextRef = WeakReference(context)
        settings = context.getSharedPreferences(DIMENS_STORAGE_NAME, Context.MODE_PRIVATE)
        editor = settings.edit()

        calculateAllDimens(screenSize)
        if (isChanged)
            saveAllDimens()
    }

    fun setDimen(textView: TextView, dimenKey: String, alternativeDimenId: Int) {
        val context = contextRef.get() ?: return

        textView.setTextSize(
            TypedValue.COMPLEX_UNIT_PX,
            getDimen(dimenKey) ?: context.resources.getDimension(alternativeDimenId)
        )
    }

    fun getDimen(dimenKey: String) : Float? {
        return absoluteDimens[dimenKey]
    }

    fun getDimen(dimenKey: String, alternativeDimenId: Int) : Float {
        val context = contextRef.get() ?: return 0f

        return absoluteDimens[dimenKey] ?: context.resources.getDimension(alternativeDimenId)
    }

    private fun calculateAllDimens(screenSize: Point) {
        for (i in layout.getValues()) {
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