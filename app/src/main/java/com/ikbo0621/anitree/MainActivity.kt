package com.ikbo0621.anitree

import android.graphics.*
import android.os.Bundle
import android.view.Window
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import com.ikbo0621.anitree.tree.TreeView
import com.ikbo0621.anitree.tree.builders.TreeEditor
import com.ikbo0621.anitree.tree.elements.Icon
import com.ikbo0621.anitree.tree.elements.buttons.Button
import com.ikbo0621.anitree.tree.elements.buttons.MainSchemeButton
import com.ikbo0621.anitree.tree.elements.buttons.SchemeButton
import com.ikbo0621.anitree.tree.structures.TreeData
import java.lang.ref.WeakReference


class MainActivity : AppCompatActivity() {
    private val bitmaps = ArrayList<Bitmap>()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        this.supportRequestWindowFeature(Window.FEATURE_NO_TITLE) // Just for test
        setContentView(R.layout.activity_main)

        // Just for test
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)

        val treeView = findViewById<TreeView>(R.id.tree)
        //treeView.setBackgroundColor(resources.getColor(R.color.background, null))

        // Form test bitmaps
        formBitmaps()

        // Work with tree
        // Test editor
        val tree = TreeData("ERASED", "A-1Pictures", getRandomBitmap(), IntArray(0))
        tree.addSubElement("OVERLORD", "Madhouse", getRandomBitmap(), intArrayOf(0))
        tree.addSubElement("STEINS;GATE", "WHITEFOX", getRandomBitmap(), intArrayOf(1))

        val treeEditor = TreeEditor(treeView, WeakReference(this), tree)
        treeEditor.invalidate()

        treeView.setOnClickListener {
            val selectedElement =
                (it to treeView).second.selectedElement ?: return@setOnClickListener

            when(selectedElement) {
                is Icon -> treeEditor.toNextLayer(selectedElement)
                is SchemeButton -> {
                    val name = "${selectedElement.getAbsPos().y.toInt() / 100}Anime"
                    treeEditor.addSubElement(name, "RandStudio", getRandomBitmap())
                }
                is Button -> treeEditor.toPreviousLayer()
                is MainSchemeButton -> {
                    treeEditor.addMainElement(
                        "Main Anime", "RandStudio", getRandomBitmap()
                    )
                }
            }

            treeEditor.invalidate()
        }

        treeView.setOnLongClickListener {
            val selectedElement =
                (it to treeView).second.selectedElement ?: return@setOnLongClickListener false
            treeEditor.deleteElement(selectedElement.index)

            treeEditor.invalidate()
            return@setOnLongClickListener true
        }

        /*
        // Test viewer
        val tree = TreeData("Main anime", getRandomBitmap(), IntArray(0))
        tree.addSubElement("0Anime", getRandomBitmap(), intArrayOf(0))
        tree.addSubElement("1Anime", getRandomBitmap(), intArrayOf(1))
        tree.addSubElement("2Anime", getRandomBitmap(), intArrayOf(2))
        tree.addSubElement("3Anime", getRandomBitmap(), intArrayOf(3))

        val treeEditor = TreeViewer(treeView, WeakReference(this), tree)
        treeEditor.invalidate()
         */

        /*
        // Test builder
        val treeBuilder = TreeBuilder(treeView, WeakReference(this))
        treeBuilder.addMainElement(getRandomBitmap(), IntArray(0))
        treeBuilder.addSubElement(getRandomBitmap(), intArrayOf(0))
        treeBuilder.addSubElement(getRandomBitmap(), intArrayOf(1))
        treeBuilder.addSubElement(getRandomBitmap(), intArrayOf(2))
        treeBuilder.addSubElement(getRandomBitmap(), intArrayOf(3))
        treeBuilder.invalidate()
         */
    }

    private fun getDarkenBitmap(bitmap: Bitmap): Bitmap {
        val multiply = (0.7f * 255).toInt()
        val canvas = Canvas(bitmap)
        val paint = Paint()
        // Just dividing each color component
        val filter: ColorFilter = LightingColorFilter(
            Color.rgb(multiply, multiply, multiply), 0
        )
        paint.colorFilter = filter
        canvas.drawBitmap(bitmap, 0f, 0f, paint)
        return bitmap
    }
    private fun formBitmaps() {
        val canvas = Canvas()

        bitmaps.add(
            getDarkenBitmap(
                Bitmap.createBitmap(190, 190, Bitmap.Config.ARGB_8888).apply {
                    canvas.setBitmap(this)
                    canvas.drawRGB(255, 0, 0)
                }
            )
        )
        bitmaps.add(
            getDarkenBitmap(
                Bitmap.createBitmap(190, 190, Bitmap.Config.ARGB_8888).apply {
                    canvas.setBitmap(this)
                    canvas.drawRGB(0, 255, 0)
                }
            )
        )
        bitmaps.add(
            getDarkenBitmap(
                Bitmap.createBitmap(190, 190, Bitmap.Config.ARGB_8888).apply {
                    canvas.setBitmap(this)
                    canvas.drawRGB(0, 0, 255)
                }
            )
        )
    }

    private fun getRandomBitmap() : Bitmap {
        return bitmaps[(0..2).random()]
    }
}