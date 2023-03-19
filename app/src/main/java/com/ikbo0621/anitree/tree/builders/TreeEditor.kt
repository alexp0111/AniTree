package com.ikbo0621.anitree.tree.builders

import android.content.Context
import android.graphics.Bitmap
import android.graphics.Paint
import com.ikbo0621.anitree.R
import com.ikbo0621.anitree.tree.TreeView
import com.ikbo0621.anitree.tree.elements.buttons.Button
import com.ikbo0621.anitree.tree.elements.buttons.MainSchemeButton
import com.ikbo0621.anitree.tree.elements.buttons.SchemeButton
import com.ikbo0621.anitree.tree.positioning.RPosition
import com.ikbo0621.anitree.tree.positioning.RRect
import com.ikbo0621.anitree.tree.positioning.RValue
import com.ikbo0621.anitree.tree.positioning.RValue.Type
import com.ikbo0621.anitree.tree.structures.TreeData
import java.lang.ref.WeakReference

class TreeEditor(
    treeView: TreeView,
    contextRef: WeakReference<Context>,
    treeData: TreeData? = null,
) : TreeViewer(treeView, contextRef, treeData) {
    override fun update() {
        super.update()
        addScheme()
        addBackField()
    }

    override fun updateLayer() {
        addScheme()
        super.updateLayer()
    }

    fun addMainElement(name: String, studio: String, bitmap: Bitmap) {
        if (isAnimating)
            return
        if (treeData == null) {
            treeData = TreeData(name, studio, bitmap, IntArray(0))
            currentElement = treeData
        }

        super.addMainElement(bitmap, currentElement!!.index)
        invalidate()
    }

    fun addSubElement(name: String, studio: String, bitmap: Bitmap) {
        if (isAnimating)
            return
        if (currentElement == null)
            return

        val index = getIndex(currentElement!!)
        currentElement!!.addSubElement(name, studio, bitmap, index)
        super.addSubElement(bitmap, index)
        invalidate()
    }

    fun deleteElement(index: IntArray?) {
        if (isAnimating)
            return
        val currentLayer = currentElement ?: return
        val elementIndex = index ?: return

        for (i in subIcons) {
            if (i.index.contentEquals(elementIndex)) {
                currentLayer.tree?.removeAt(subIcons.indexOf(i))
                break
            }
        }

        // Correct indices
        for (i in currentLayer.tree!!) {
            i.index = getIndex(currentLayer, currentLayer.tree!!.indexOf(i))
        }

        updateLayer()
    }

    fun getTree() : TreeData? {
        return treeData
    }

    private fun addScheme() {
        val context = contextRef.get() ?: return
        val schemeColor = context.resources.getColor(R.color.scheme_color, null)
        val elementsColor = context.resources.getColor(R.color.elements_color, null)

        if (mainIcon == null) {
            treeView.addElement(MainSchemeButton(mainIconPos, mainIconRadius, schemeColor))
            addMainFrame(elementsColor)
        }

        val schemeIndex = subIcons.size
        if (schemeIndex >= 3)
            return

        treeView.addElement(
            SchemeButton(subIconsPositions[schemeIndex], subIconRadius, schemeColor)
        )
        // Adjusting the position so that the line does not
        // paint over the frame of the main element
        treeView.addElement(
            createCurveToSubIcon(
                RPosition(mainFramePos).apply { add(RValue(), lineWidth) },
                subFramePositions[schemeIndex],
                elementsColor,
                Paint.Cap.SQUARE
            )
        )
        addSubFrame(schemeIndex, elementsColor)
    }

    private fun addBackField() {
        val rect = RRect(
            RPosition(RValue(0f, Type.X), RValue(0f, Type.Y)),
            RPosition(RValue(0.3f, Type.SmallSide), RValue(1f, Type.Y))
        )
        treeView.addElement(Button(RPosition(RValue(), RValue()), rect))
    }

    // Returns the index of the new element
    private fun getIndex(parent: TreeData) : IntArray {
        val subIndex = parent.tree?.size ?: 0

        val result = IntArray(parent.index.size + 1)

        for (i in parent.index.indices)
            result[i] = parent.index[i]
        result[result.lastIndex] = subIndex

        return result
    }

    // Returns the index of the element at the given position
    private fun getIndex(parent: TreeData, position: Int) : IntArray {
        val result = IntArray(parent.index.size + 1)

        for (i in parent.index.indices)
            result[i] = parent.index[i]
        result[result.lastIndex] = position

        return result
    }
}