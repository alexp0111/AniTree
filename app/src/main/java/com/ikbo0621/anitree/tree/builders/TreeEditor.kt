package com.ikbo0621.anitree.tree.builders

import android.content.Context
import android.graphics.Bitmap
import com.ikbo0621.anitree.tree.TreeView
import com.ikbo0621.anitree.tree.elements.Icon
import com.ikbo0621.anitree.tree.structures.TreeData
import java.lang.ref.WeakReference

class TreeEditor(
    treeView: TreeView,
    contextRef: WeakReference<Context>,
) : TreeViewer(treeView, contextRef) {
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
                currentLayer.tree?.removeAt(subIcons.indexOf(i))
                break
            }
        }

        // Correct indices
        for (i in currentLayer.tree!!) {
            i.index = getIndex(currentLayer, currentLayer.tree!!.indexOf(i))
        }

        updateLayer()
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