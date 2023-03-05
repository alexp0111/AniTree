package com.ikbo0621.anitree.tree.builders

import android.content.Context
import android.graphics.Bitmap
import com.ikbo0621.anitree.R
import com.ikbo0621.anitree.tree.TreeView
import com.ikbo0621.anitree.tree.elements.Circle
import com.ikbo0621.anitree.tree.elements.Rectangle
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

    fun addMainElement(name: String, bitmap: Bitmap) {
        if (treeData == null) {
            treeData = TreeData(name, bitmap, IntArray(0))
            currentElement = treeData
        }

        super.addMainElement(bitmap, currentElement!!.index)
    }

    fun addSubElement(name: String, bitmap: Bitmap) {
        if (currentElement == null)
            return

        val index = getIndex(currentElement!!)
        currentElement!!.addSubElement(name, bitmap, index)
        super.addSubElement(bitmap, index)
    }

    fun deleteElement(index: IntArray?) {
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
            treeView.addElement(
                Circle(mainIconPos, mainIconRadius, schemeColor).apply {
                    index = intArrayOf(0)
                }
            )
            addMainFrame(elementsColor)
        }

        val schemeIndex = subIcons.size
        if (schemeIndex >= 3)
            return

        treeView.addElement(Circle(subIconsPositions[schemeIndex], subIconRadius, schemeColor))

        treeView.addElement(
            createCurveToSubIcon(mainFramePos, subFramePositions[schemeIndex], elementsColor)
        )
        addSubFrame(schemeIndex, elementsColor)
    }

    private fun addBackField() {
        val rect = RRect(
            RPosition(RValue(0f, Type.X), RValue(0f, Type.Y)),
            RPosition(RValue(0.2f, Type.SmallSide), RValue(1f, Type.Y))
        )
        treeView.addElement(Rectangle(RPosition(RValue(), RValue()), rect).apply {
            index = intArrayOf(0)
        })
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