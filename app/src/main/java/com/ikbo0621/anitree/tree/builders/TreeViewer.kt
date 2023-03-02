package com.ikbo0621.anitree.tree.builders

import android.graphics.Color
import android.graphics.Typeface
import androidx.core.graphics.*
import com.ikbo0621.anitree.tree.TreeView
import com.ikbo0621.anitree.tree.elements.Circle
import com.ikbo0621.anitree.tree.elements.Text
import com.ikbo0621.anitree.tree.elements.TreeElement
import com.ikbo0621.anitree.tree.positioning.RPosition
import com.ikbo0621.anitree.tree.positioning.RValue
import com.ikbo0621.anitree.tree.structures.TreeData

open class TreeViewer(
    treeView: TreeView,
    protected var treeData: TreeData? = null,
    protected var font: Typeface? = null
) : TreeBuilder(treeView) {
    protected var currentElement = treeData

    init {
        if (treeData == null)
            addScheme()
        else
            this.toAnotherLayer(intArrayOf())
    }

    fun toPreviousLayer() {
        val currentIndex = getCurrentIndex() ?: return
        if (currentIndex.isEmpty())
            return
        val previousIndex = currentIndex.copyOf(currentIndex.size - 1)

        toAnotherLayer(previousIndex)
    }

    fun toNextLayer(nextElement: TreeElement?) {
        val nextIndex = nextElement?.index ?: return

        toAnotherLayer(nextIndex)
    }

    protected open fun toAnotherLayer(index: IntArray) {
        var resultLayer = treeData
        for (i in index) {
            resultLayer = resultLayer!!.tree?.getOrNull(i) ?: return
        }
        currentElement = resultLayer

        updateLayer()
    }

    protected fun updateLayer() {
        val layer = currentElement ?: return

        treeView.clearElements()
        subIcons.clear()

        addMainElement(layer.bitmap, layer.index)
        if (layer.tree != null) {
            for (i in layer.tree!!)
                addSubElement(i.bitmap, i.index)
        }
    }

    private fun getCurrentIndex() : IntArray? {
        return currentElement?.index
    }

    protected fun addScheme() {
        val schemeColor = Color.argb(69, mainColor.red, mainColor.green, mainColor.blue) // Just for test
        if (mainIcon == null) {
            mainIcon = Circle(mainIconPos, mainIconRadius, schemeColor)
            mainIcon!!.index = intArrayOf(0)
        }

        if (subIcons.size >= 3)
            return

        subIcons.add(Circle(subIconsPositions[subIcons.size], subIconRadius, schemeColor))
        addText()
    }

    protected fun addText() {
        /*
        val textPaint = Paint().apply {
            isAntiAlias = true
            style = Paint.Style.FILL
            textSize = 16F
        }
        canvas.drawText("TEST TEXT", 0F, 0F, textPaint)
         */
        if (font == null)
            return

        otherElements.add(Text(RPosition(RValue(0.5f, RValue.Type.X), RValue(0.5f, RValue.Type.Y)), "I HATE EVERYTHING ABOUT YOU", font=font))
    }
}