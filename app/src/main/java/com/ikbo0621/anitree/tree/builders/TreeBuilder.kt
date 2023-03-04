package com.ikbo0621.anitree.tree.builders

import android.content.Context
import android.graphics.Bitmap
import android.graphics.Paint
import com.ikbo0621.anitree.R
import com.ikbo0621.anitree.tree.TreeView
import com.ikbo0621.anitree.tree.elements.*
import com.ikbo0621.anitree.tree.positioning.RValue.Type
import com.ikbo0621.anitree.tree.positioning.RPosition
import com.ikbo0621.anitree.tree.positioning.RRect
import com.ikbo0621.anitree.tree.positioning.RValue
import java.lang.ref.WeakReference

open class TreeBuilder(
    protected val treeView: TreeView,
    protected val contextRef: WeakReference<Context>
) {
    protected var otherElements = ArrayList<TreeElement>()

    protected var mainIcon: Circle? = null
    protected val mainIconPos = RPosition().apply {
        add(RValue(0.14f, Type.Y), RValue(0.15f, Type.Y))
    }
    protected val mainIconRadius = RValue(0.1f, Type.Y)
    private val mainFramePos = RPosition(RValue(0.13f, Type.Y), RValue(0.16f, Type.Y))

    protected var subIcons = ArrayList<Circle>()
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
    protected val subIconRadius = RValue(0.08f, Type.Y)
    private val subFramePositions = ArrayList<RPosition>(3).apply{
        add(
            RPosition(
                RValue(1.0f, Type.X), RValue(0.6f, Type.Y)
            ).apply {
                add(RValue(-0.11f, Type.Y), RValue(-0.11f, Type.Y))
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

    fun invalidate() {
        if (mainIcon == null)
            return

        val context = contextRef.get() ?: return
        val elementsColor = context.resources.getColor(R.color.elements_color, null)
        treeView.clearElements()

        for (i in otherElements)
            treeView.addElement(i)

        for (i in 0 until subIcons.size) {
            treeView.addElement(
                createCurveToSubIcon(mainFramePos, subFramePositions[i], elementsColor)
            )
            treeView.addElement(subIcons[i])
        }
        treeView.addElement(mainIcon!!)
        addBackField()
        addDesign(elementsColor)
        treeView.invalidate()
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

    private fun addCircle(position: RPosition, radius: RValue, color: Int) {
        treeView.addElement(
                Circle(position, radius, color, Paint.Style.STROKE, RValue(0.003f, Type.Y)
            )
        )
    }

    private fun addDesign(color: Int) {
        addCircle(mainFramePos, mainIconRadius, color)

        for (i in 0 until subIcons.size) {
            addCircle(subFramePositions[i], subIconRadius, color)
        }
    }

    private fun createCurveToSubIcon(mainPos: RPosition, subPos: RPosition, color: Int) : Curve {
        val startPos = RPosition(mainFramePos).apply {
            add(RValue(), RValue(mainIconRadius.getRelative(), mainIconRadius.getType()))
        }
        val cornerPos = RPosition(
            mainPos.getRelativeX(), subPos.getRelativeY()
        )
        val endPos = RPosition(subPos).apply {
            add(RValue(-subIconRadius.getRelative(), subIconRadius.getType()), RValue())
        }
        val upperAnchorPoint = RPosition(cornerPos)
        upperAnchorPoint.add(
            RValue(), RValue(-subIconRadius.getRelative(), subIconRadius.getType())
        )
        val downAnchorPoint = RPosition(cornerPos)
        downAnchorPoint.add(RValue(subIconRadius.getRelative(), subIconRadius.getType()), RValue())

        return Curve(
            arrayOf(
                Line.LinePoint(startPos, false),
                Line.LinePoint(upperAnchorPoint, false),
                Line.LinePoint(cornerPos, true),
                Line.LinePoint(downAnchorPoint, false),
                Line.LinePoint(endPos, false)
            ),
            RValue(0.003f, Type.Y),
            color
        )
    }
}