package com.ikbo0621.anitree

import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Typeface
import android.os.Bundle
import android.view.Window
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.core.content.res.ResourcesCompat
import com.ikbo0621.anitree.tree.TreeView
import com.ikbo0621.anitree.tree.builders.TreeEditor
import com.ikbo0621.anitree.tree.elements.Icon
import com.ikbo0621.anitree.tree.elements.Rectangle

class MainActivity : AppCompatActivity() {
    private val bitmaps = ArrayList<Bitmap>()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        this.supportRequestWindowFeature(Window.FEATURE_NO_TITLE) // Just for test
        setContentView(R.layout.activity_main)

        // Just for test
        val backgroundColor = Color.rgb(41, 41, 41)
        window.statusBarColor = backgroundColor
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)

        val treeView = findViewById<TreeView>(R.id.tree)
        treeView.setBackgroundColor(backgroundColor)

        // Form test bitmaps
        formBitmaps()

        // Work with tree
        val treeEditor = TreeEditor(treeView, ResourcesCompat.getFont(this, R.font.intro))
        treeEditor.invalidate()

        treeView.setOnClickListener {
            val selectedElement = (it to treeView).second.selectedElement ?: return@setOnClickListener

            if (selectedElement is Rectangle && selectedElement.index != null) { // back "button"
                treeEditor.toPreviousLayer()
            } else if (selectedElement is Icon) { // sub element
                treeEditor.toNextLayer(selectedElement)
            } else { // empty icon
                if (selectedElement!!.index != null) // create main element
                    treeEditor.addMainElement("_", getRandomBitmap())
                else // create sub element
                    treeEditor.addSubElement("01", getRandomBitmap())
            }

            treeEditor.invalidate()
        }

        treeView.setOnLongClickListener {
            val selectedElement = (it to treeView).second.selectedElement
            treeEditor.deleteElement(selectedElement!!.index)

            return@setOnLongClickListener true
        }
    }

    private fun formBitmaps() {
        val canvas = Canvas()
        bitmaps.add(
            Bitmap.createBitmap(190, 190, Bitmap.Config.ARGB_8888).apply {
                canvas.setBitmap(this)
                canvas.drawRGB(255, 0, 0)
            }
        )
        bitmaps.add(
            Bitmap.createBitmap(190, 190, Bitmap.Config.ARGB_8888).apply {
                canvas.setBitmap(this)
                canvas.drawRGB(0, 255, 0)
            }
        )
        bitmaps.add(
            Bitmap.createBitmap(190, 190, Bitmap.Config.ARGB_8888).apply {
                canvas.setBitmap(this)
                canvas.drawRGB(0, 0, 255)
            }
        )
    }

    private fun getRandomBitmap() : Bitmap {
        return bitmaps[(0..2).random()]
    }
}