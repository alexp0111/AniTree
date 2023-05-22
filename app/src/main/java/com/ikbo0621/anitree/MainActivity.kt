package com.ikbo0621.anitree

import android.graphics.*
import android.os.Bundle
import android.view.Window
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.core.content.ContextCompat
import com.ikbo0621.anitree.tree.TreeView
import com.ikbo0621.anitree.tree.builders.TreeEditor
import com.ikbo0621.anitree.tree.elements.Icon
import com.ikbo0621.anitree.tree.elements.VectorIcon
import com.ikbo0621.anitree.tree.elements.buttons.Button
import com.ikbo0621.anitree.tree.elements.buttons.CrossButton
import com.ikbo0621.anitree.tree.elements.buttons.SchemeButton
import com.ikbo0621.anitree.tree.structures.TreeData


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

        val treeEditor = TreeEditor(treeView, this, tree)
        treeEditor.setBottomIcon(
            ContextCompat.getDrawable(this, R.drawable.save)!!,
            ContextCompat.getDrawable(this, R.drawable.confirmation)!!
        )
        treeEditor.invalidate()

        treeView.setOnClickListener {
            val selectedElement = (it to treeView).second.selectedElement
            if (selectedElement == null) {
                treeEditor.showCrossButtons(false)
                return@setOnClickListener
            }

            when(selectedElement) {
                is Icon -> treeEditor.toNextLayer(selectedElement)
                is SchemeButton -> {
                    val name = "${selectedElement.getAbsPos().y.toInt() / 100}Anime"
                    treeEditor.addSubElement(name, "RandStudio", getRandomBitmap())
                }
                is CrossButton -> {
                    treeEditor.deleteElement(selectedElement.index)
                }
                is VectorIcon -> {
                    Toast.makeText(
                        applicationContext,
                        "Is saved: ${treeEditor.switchBottomButton()}",
                        Toast.LENGTH_SHORT
                    ).show()
                }
                is Button -> {
                    if (selectedElement.index != null) // author button
                        Toast.makeText(applicationContext, "Author", Toast.LENGTH_SHORT).show()
                    else
                        treeEditor.toPreviousLayer()
                }
                else -> treeEditor.showCrossButtons(false)
            }
        }

        treeView.setOnLongClickListener {
            treeEditor.showCrossButtons(true)

            return@setOnLongClickListener true
        }

        // Test viewer
//        val tree = TreeData("Main anime", "MainStudio", getRandomBitmap(), IntArray(0))
//        tree.addSubElement("0Anime", "Studio0", getRandomBitmap(), intArrayOf(0))
//        tree.addSubElement("1Anime", "Studio1", getRandomBitmap(), intArrayOf(1))
//        tree.addSubElement("2Anime", "Studio2", getRandomBitmap(), intArrayOf(1, 0))
//        tree.addSubElement("3Anime", "Studio3", getRandomBitmap(), intArrayOf(1, 1))
//        tree.addSubElement("2Anime", "Studio2", getRandomBitmap(), intArrayOf(2))
//        tree.addSubElement("3Anime", "Studio3", getRandomBitmap(), intArrayOf(3))
//
//        val treeViewer = TreeViewer(treeView, this, tree)
//        treeViewer.setAuthor(getRandomBitmap(), "animebit13")
//        treeViewer.setBottomIcon(
//            ContextCompat.getDrawable(this, R.drawable.like)!!,
//            ContextCompat.getDrawable(this, R.drawable.like_activated)!!
//        )
//        treeViewer.invalidate()
//
//        treeView.setOnClickListener {
//            val selectedElement =
//                (it to treeView).second.selectedElement ?: return@setOnClickListener
//
//            when(selectedElement) {
//                is VectorIcon -> {
//                    Toast.makeText(
//                        applicationContext,
//                        "Is liked: ${treeViewer.switchBottomButton()}",
//                        Toast.LENGTH_SHORT
//                    ).show()
//                }
//                is Icon -> treeViewer.toNextLayer(selectedElement)
//                is Button -> treeViewer.toPreviousLayer()
//            }
//        }

        // Test builder
//        val treeBuilder = TreeBuilder(treeView, WeakReference(this))
//        treeBuilder.addMainElement(getRandomBitmap(), IntArray(0))
//        treeBuilder.addSubElement(getRandomBitmap(), intArrayOf(0))
//        treeBuilder.addSubElement(getRandomBitmap(), intArrayOf(1))
//        treeBuilder.addSubElement(getRandomBitmap(), intArrayOf(2))
//        treeBuilder.addSubElement(getRandomBitmap(), intArrayOf(3))
//        treeBuilder.invalidate()
    }

    private fun getDarkenBitmap(bitmap: Bitmap): Bitmap {
        val multiply = (0.7f * 255).toInt()
        val canvas = Canvas(bitmap)
        val paint = Paint()
        // Just dividing each color component
        val filter = LightingColorFilter(
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
                Bitmap.createBitmap(190, 250, Bitmap.Config.ARGB_8888).apply {
                    canvas.setBitmap(this)
                    canvas.drawRGB(255, 0, 0)
                }
            )
        )
        bitmaps.add(
            getDarkenBitmap(
                Bitmap.createBitmap(190, 250, Bitmap.Config.ARGB_8888).apply {
                    canvas.setBitmap(this)
                    canvas.drawRGB(0, 255, 0)
                }
            )
        )
        bitmaps.add(
            getDarkenBitmap(
                Bitmap.createBitmap(190, 250, Bitmap.Config.ARGB_8888).apply {
                    canvas.setBitmap(this)
                    canvas.drawRGB(0, 0, 255)
                }
            )
        )
    }

    private fun getRandomBitmap() : Bitmap {
        val result = bitmaps[(0..2).random()]
        val canvas = Canvas(result)
        canvas.drawRect(0.0f, 0.0f, 95.0f, 125.0f, Paint())
        return bitmaps[(0..2).random()]
    }
}