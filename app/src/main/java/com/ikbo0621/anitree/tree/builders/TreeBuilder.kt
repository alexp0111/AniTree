package com.ikbo0621.anitree.tree.builders

import android.graphics.Bitmap
import android.graphics.Color
import android.graphics.Paint
import com.ikbo0621.anitree.tree.TreeView
import com.ikbo0621.anitree.tree.elements.*
import com.ikbo0621.anitree.tree.positioning.RValue.Type
import com.ikbo0621.anitree.tree.positioning.RPosition
import com.ikbo0621.anitree.tree.positioning.RRect
import com.ikbo0621.anitree.tree.positioning.RValue

open class TreeBuilder(protected val treeView: TreeView) {
    protected var mainIcon: Circle? = null
    protected var subIcons = ArrayList<Circle>()

    protected val mainIconRadius = RValue(0.1f, Type.Y)
    protected val subIconRadius = RValue(0.08f, Type.Y)
    private val mainFramePos = RPosition(RValue(0.13f, Type.Y), RValue(0.13f, Type.Y))
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
    protected val mainIconPos = RPosition().apply {
        add(RValue(0.14f, Type.Y), RValue(0.12f, Type.Y))
    }
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
            Icon(
                subIconsPositions[subIcons.size],
                subIconRadius,
                bitmap
            )
        )
        subIcons.last().index = index
    }

    fun invalidate() {
        if (mainIcon == null)
            return

        treeView.clearElements()
        for (i in 0 until subIcons.size) {
            treeView.addElement(createCurveToSubIcon(mainFramePos, subFramePositions[i]))
            treeView.addElement(subIcons[i])
        }
        treeView.addElement(mainIcon!!)
        addBackField()
        addDesign()
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

    private fun addDesign() {
        treeView.addElement(
            Circle(
                mainFramePos,
                mainIconRadius,
                Color.LTGRAY,
                Paint.Style.STROKE,
                RValue(0.01f, Type.SmallSide)
            )
        )

        for (i in 0 until subIcons.size) {
            treeView.addElement(
                Circle(
                    subFramePositions[i],
                    subIconRadius,
                    Color.LTGRAY,
                    Paint.Style.STROKE,
                    RValue(0.01f, Type.SmallSide)
                )
            )
        }
    }

    private fun createCurveToSubIcon(mainPos: RPosition, subPos: RPosition) : Curve {
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
            RValue(0.01f, Type.SmallSide),
            Color.LTGRAY
        )
    }
}