package com.ikbo0621.anitree.tree.builders

import android.content.Context
import android.graphics.Bitmap
import com.ikbo0621.anitree.tree.TreeView
import com.ikbo0621.anitree.tree.elements.*
import com.ikbo0621.anitree.tree.positioning.RPosition
import com.ikbo0621.anitree.tree.structures.TreeLayout
import java.lang.ref.WeakReference

open class TreeBuilder(
    protected val treeView: TreeView,
    protected val contextRef: WeakReference<Context>
) {
    protected var mainIcon: Circle? = null
    protected var subIcons = ArrayList<Circle>(3)
    protected val layout = TreeLayout(RPosition())

    fun addMainElement(bitmap: Bitmap, index: IntArray) {
        mainIcon = Icon(
            layout.mainIconPos,
            layout.mainIconRadius,
            bitmap,
        )
        mainIcon!!.index = index
    }

    fun addSubElement(bitmap: Bitmap, index: IntArray) {
        if (mainIcon == null)
            return

        if (subIcons.size >= 3)
            return

        subIcons.add(
            Icon(layout.subIconsPositions[subIcons.size], layout.subIconRadius, bitmap)
        )
        subIcons.last().index = index
    }

    protected open fun update() {
        if (mainIcon == null)
            return

        for (i in 0 until subIcons.size) {
            treeView.addElement(subIcons[i])
        }
        treeView.addElement(mainIcon!!)
    }

    fun invalidate() {
        treeView.clearElements()
        update()
        treeView.invalidate()
    }
}