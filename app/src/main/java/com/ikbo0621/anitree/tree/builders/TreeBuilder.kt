package com.ikbo0621.anitree.tree.builders

import android.content.Context
import android.graphics.*
import com.ikbo0621.anitree.tree.TreeView
import com.ikbo0621.anitree.tree.elements.*
import com.ikbo0621.anitree.tree.positioning.RPosition
import com.ikbo0621.anitree.tree.structures.TreeLayout
import java.lang.ref.WeakReference


open class TreeBuilder(
    protected val treeView: TreeView,
    context: Context
) {
    protected val contextRef = WeakReference(context)
    protected var mainIcon: Circle? = null
    protected var subIcons = ArrayList<Circle>(3)
    protected val layout = TreeLayout(RPosition())
    protected var mainIconIndex: Int? = null

    fun addMainElement(bitmap: Bitmap, index: IntArray) {
        mainIcon = Icon(
            layout.mainIconPos,
            layout.mainIconRadius,
            getDarkenBitmap(bitmap),
        )
        mainIcon!!.index = index
    }

    fun addSubElement(bitmap: Bitmap, index: IntArray) {
        if (mainIcon == null)
            return

        if (subIcons.size >= 3)
            return

        subIcons.add(
            Icon(layout.subIconsPositions[subIcons.size],
                layout.subIconRadius,
                getDarkenBitmap(bitmap)
            )
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
        mainIconIndex = treeView.elements.lastIndex
    }

    protected fun getDarkenBitmap(bitmap: Bitmap): Bitmap {
        val result = bitmap.copy(bitmap.config, true)
        val multiply = 179
        val canvas = Canvas(result)
        val paint = Paint()
        // Just dividing each color component
        val filter = LightingColorFilter(
            Color.rgb(multiply, multiply, multiply), 0
        )
        paint.colorFilter = filter
        canvas.drawBitmap(result, 0f, 0f, paint)
        return result
    }

    fun invalidate() {
        treeView.clearElements()
        update()
        treeView.invalidate()
    }
}