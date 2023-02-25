package com.ikbo0621.anitree.tree.builders

import android.graphics.Bitmap
import com.ikbo0621.anitree.tree.TreeView
import com.ikbo0621.anitree.tree.elements.*
import com.ikbo0621.anitree.tree.positioning.RValue.Type
import com.ikbo0621.anitree.tree.positioning.RPosition
import com.ikbo0621.anitree.tree.positioning.RValue

open class TreeBuilder(protected val treeView: TreeView) {
    protected var mainIcon: Circle? = null
    protected var subIcons = ArrayList<Circle>()

    protected val mainIconPos = RPosition(
        RValue(0.2f, Type.SmallSide), RValue(0.2f, Type.SmallSide)
    )
    protected val subIconPos1 = RPosition(
        RValue(1.0f, Type.X), RValue(0.6f, Type.Y)
    ).apply {
        add(RValue(-0.2f, Type.SmallSide), RValue(-0.2f, Type.SmallSide))
    }
    protected val subIconPos2 = RPosition(
        RValue(1.0f, Type.X), RValue(0.8f, Type.Y)
    ).apply {
        add(RValue(-0.2f, Type.SmallSide), RValue(-0.2f, Type.SmallSide))
    }
    protected val subIconPos3 = RPosition(
        RValue(1.0f, Type.X), RValue(1.0f, Type.Y)
    ).apply {
        add(RValue(-0.2f, Type.SmallSide), RValue(-0.2f, Type.SmallSide))
    }

    fun addMainElement(bitmap: Bitmap, index: IntArray) {
        mainIcon = Icon(
            mainIconPos,
            RValue(0.1f, Type.Y),
            bitmap,
        )
        mainIcon!!.index = index
    }
    fun addSubElement(bitmap: Bitmap, index: IntArray) {
        if (mainIcon == null)
            return

        val iconPos = when (subIcons.size) {
            0 -> subIconPos1
            1 -> subIconPos2
            2 -> subIconPos3
            else -> null
        } ?: return

        subIcons.add(
            Icon(
                iconPos,
                RValue(0.07f, Type.Y),
                bitmap
            )
        )
        subIcons.last().index = index
    }

    fun invalidate() {
        if (mainIcon == null)
            return

        treeView.clearElements()
        for (i in subIcons) {
            treeView.addElement(createCurveToSubIcon(mainIcon!!, i))
            treeView.addElement(i)
        }
        treeView.addElement(mainIcon!!)
        addBackField()
        treeView.invalidate()
    }

    private fun addBackField() {
        val rect = Rectangle.RectR(
            RPosition(RValue(0f, Type.X), RValue(0f, Type.Y)),
            RPosition(RValue(0.2f, Type.SmallSide), RValue(1f, Type.Y))
        )
        treeView.addElement(Rectangle(RPosition(RValue(), RValue()), rect).apply {
            index = intArrayOf(0)
        })
    }

    private fun createCurveToSubIcon(mainIcon: Circle, subIcon: Circle) : Curve {
        val cornerPos = RPosition(
            mainIcon.getRPos().getRelativeX(), subIcon.getRPos().getRelativeY()
        )
        val delta = subIcon.radius
        val upperAnchorPoint = RPosition(cornerPos)
        upperAnchorPoint.add(RValue(), RValue(-delta.getRelative(), delta.getType()))
        val downAnchorPoint = RPosition(cornerPos)
        downAnchorPoint.add(RValue(delta.getRelative(), delta.getType()), RValue())

        return Curve(
            arrayOf(
                Line.LinePoint(mainIconPos, false),
                Line.LinePoint(upperAnchorPoint, false),
                Line.LinePoint(cornerPos, true),
                Line.LinePoint(downAnchorPoint, false),
                Line.LinePoint(subIcon.getRPos(), false)
            )
        )
    }
}