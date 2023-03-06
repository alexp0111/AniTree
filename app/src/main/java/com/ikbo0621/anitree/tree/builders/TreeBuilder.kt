package com.ikbo0621.anitree.tree.builders

import android.content.Context
import android.graphics.Bitmap
import com.ikbo0621.anitree.tree.TreeView
import com.ikbo0621.anitree.tree.elements.*
import com.ikbo0621.anitree.tree.positioning.RValue.Type
import com.ikbo0621.anitree.tree.positioning.RPosition
import com.ikbo0621.anitree.tree.positioning.RValue
import java.lang.ref.WeakReference

open class TreeBuilder(
    protected val treeView: TreeView,
    protected val contextRef: WeakReference<Context>
) {
    protected var mainIcon: Circle? = null
    protected val mainIconRadius = RValue(0.1f, Type.Y, RValue(0.18f, Type.X))
    protected val mainIconPos = RPosition().apply {
        add(RValue(0.035f, Type.Y), RValue(0.05f, Type.Y))
        add(mainIconRadius, mainIconRadius)
    }

    protected var subIcons = ArrayList<Circle>(3)
    protected val subIconRadius = RValue(0.08f, Type.Y, RValue(0.16f, Type.X))
    protected val subIconsPositions = ArrayList<RPosition>(3).apply{
        add(
            RPosition(
                RValue(1.0f, Type.X), RValue(0.6f, Type.Y)
            ).apply {
                add(RValue(-0.10f, Type.Y), RValue(-0.12f, Type.Y))
            }
        )
        add(RPosition(last()).apply { add(RValue(), RValue(0.2f, Type.Y)) })
        add(RPosition(last()).apply { add(RValue(), RValue(0.2f, Type.Y)) })
    }

    fun addMainElement(bitmap: Bitmap, index: IntArray) {
        mainIcon = Icon(
            mainIconPos,
            mainIconRadius,
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
            Icon(subIconsPositions[subIcons.size], subIconRadius, bitmap)
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