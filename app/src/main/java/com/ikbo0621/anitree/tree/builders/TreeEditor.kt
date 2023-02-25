package com.ikbo0621.anitree.tree.builders

import android.graphics.Bitmap
import com.ikbo0621.anitree.tree.TreeView
import com.ikbo0621.anitree.tree.elements.Icon
import com.ikbo0621.anitree.tree.structures.TreeData

class TreeEditor(treeView: TreeView) : TreeViewer(treeView) {

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
}