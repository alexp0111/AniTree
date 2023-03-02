package com.ikbo0621.anitree.tree.builders

import android.graphics.Bitmap
import android.graphics.Typeface
import com.ikbo0621.anitree.tree.TreeView
import com.ikbo0621.anitree.tree.elements.Icon
import com.ikbo0621.anitree.tree.structures.TreeData

class TreeEditor(
    treeView: TreeView,
    font: Typeface? = null
) : TreeViewer(treeView, font=font) {
    override fun toAnotherLayer(index: IntArray) {
        super.toAnotherLayer(index)
        addScheme()
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
        if (subIcons.isNotEmpty() && subIcons.last() !is Icon) // If sub element is empty
            subIcons.removeAt(subIcons.lastIndex)

        currentElement!!.addSubElement(name, bitmap, index)
        super.addSubElement(bitmap, index)
        addScheme() // Add next empty icon
    }

    fun deleteElement(index: IntArray?) {
        val currentLayer = currentElement ?: return
        val elementIndex = index ?: return

        for (i in subIcons) {
            if (i.index.contentEquals(elementIndex)) {
                if (subIcons.last().index == null) // delete empty icon
                    subIcons.removeLast()
                currentLayer.tree?.removeAt(subIcons.indexOf(i))

                break
            }
        }

        updateLayer()

        // Correct indexes
        for (i in subIcons) {
            if (i.index != null) {
                i.index = getIndex(currentLayer, subIcons.indexOf(i))
                currentLayer.tree?.getOrNull(subIcons.indexOf(i))?.index = i.index!!
            }
        }
        addScheme()
        invalidate()
    }

    fun getTree() : TreeData? {
        return treeData
    }

    private fun getIndex(parent: TreeData) : IntArray {
        val subIndex = parent.tree?.size ?: 0

        val result = IntArray(parent.index.size + 1)

        for (i in parent.index.indices)
            result[i] = parent.index[i]
        result[result.lastIndex] = subIndex

        return result
    }

    private fun getIndex(parent: TreeData, additional: Int) : IntArray {
        val result = IntArray(parent.index.size + 1)

        for (i in parent.index.indices)
            result[i] = parent.index[i]
        result[result.lastIndex] = additional

        return result
    }
}