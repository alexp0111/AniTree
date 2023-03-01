package com.ikbo0621.anitree.tree.builders

import com.ikbo0621.anitree.tree.TreeView
import com.ikbo0621.anitree.tree.elements.Circle
import com.ikbo0621.anitree.tree.elements.TreeElement
import com.ikbo0621.anitree.tree.structures.TreeData

open class TreeViewer(treeView: TreeView, protected var treeData: TreeData? = null) : TreeBuilder(treeView) {
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

        treeView.clearElements()
        subIcons.clear()

        addMainElement(currentElement!!.bitmap, currentElement!!.index)
        if (currentElement!!.tree != null) {
            for (i in currentElement!!.tree!!)
                addSubElement(i.bitmap, i.index)
        }
    }

    private fun getCurrentIndex() : IntArray? {
        return currentElement?.index
    }

    protected fun addScheme() {
        if (mainIcon == null) {
            mainIcon = Circle(mainIconPos, mainIconRadius)
            mainIcon!!.index = intArrayOf(0)
        }

        /*
        val iconPos = when (subIcons.size) {
            0 -> subIconPos1
            1 -> subIconPos2
            2 -> subIconPos3
            else -> null
        } ?: return
         */
        if (subIcons.size >= 3)
            return

        subIcons.add(Circle(subIconsPositions[subIcons.size], subIconRadius))
    }
}