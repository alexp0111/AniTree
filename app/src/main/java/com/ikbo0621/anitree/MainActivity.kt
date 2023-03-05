package com.ikbo0621.anitree

import android.graphics.Bitmap
import android.graphics.Canvas
import android.os.Bundle
import android.view.Window
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import com.ikbo0621.anitree.tree.TreeView
import com.ikbo0621.anitree.tree.builders.TreeBuilder
import com.ikbo0621.anitree.tree.builders.TreeEditor
import com.ikbo0621.anitree.tree.builders.TreeViewer
import com.ikbo0621.anitree.tree.elements.Icon
import com.ikbo0621.anitree.tree.elements.Rectangle
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
        treeView.setBackgroundColor(resources.getColor(R.color.background, null))

        // Form test bitmaps
        formBitmaps()

        // Work with tree
        // Test editor
        val tree = TreeData("Main anime", getRandomBitmap(), IntArray(0))
        tree.addSubElement("0Anime", getRandomBitmap(), intArrayOf(0))
        tree.addSubElement("1Anime", getRandomBitmap(), intArrayOf(1))

        val treeEditor = TreeEditor(treeView, WeakReference(this), tree)
        treeEditor.invalidate()

        treeView.setOnClickListener {
            val selectedElement =
                (it to treeView).second.selectedElement ?: return@setOnClickListener

            if (selectedElement is Rectangle && selectedElement.index != null) { // Back "button"
                treeEditor.toPreviousLayer()
            } else if (selectedElement is Icon) { // Sub element
                treeEditor.toNextLayer(selectedElement)
            } else { // Empty icon
                if (selectedElement.index != null) { // create main element
                    treeEditor.addMainElement("Main Anime", getRandomBitmap())
                } else {// Create sub element
                    val name = "${selectedElement.getAbsPos().y.toInt() / 100}Anime"
                    treeEditor.addSubElement(name, getRandomBitmap())
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