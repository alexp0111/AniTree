package com.ikbo0621.anitree.tree.builders

import android.content.Context
import android.graphics.Paint
import android.graphics.Typeface
import androidx.core.content.res.ResourcesCompat
import com.ikbo0621.anitree.R
import com.ikbo0621.anitree.tree.TreeView
import com.ikbo0621.anitree.tree.elements.*
import com.ikbo0621.anitree.tree.positioning.RPosition
import com.ikbo0621.anitree.tree.positioning.RValue
import com.ikbo0621.anitree.tree.positioning.RValue.Type
import com.ikbo0621.anitree.tree.structures.TreeData
import java.lang.ref.WeakReference

open class TreeViewer(
    treeView: TreeView,
    contextRef: WeakReference<Context>,
    protected var treeData: TreeData? = null
) : TreeBuilder(treeView, contextRef) {
    protected var currentElement = treeData

    private val mainTextPosition = RPosition(
        RValue(-0.05F,  Type.Y), RValue(-0.04F,  Type.Y)
    )
    private val mainTextSize = RValue(0.3F, Type.Y)
    protected val mainFramePos = RPosition(RValue(0.13f, Type.Y), RValue(0.16f, Type.Y))

    private val subTextPositions = ArrayList<RPosition>(3).apply{
        add(RPosition(RValue(0.17f, Type.Y), RValue(0.518f, Type.Y)))
        add(RPosition(last()).apply { add(RValue(), RValue(0.2f, Type.Y)) })
        add(RPosition(last()).apply { add(RValue(), RValue(0.2f, Type.Y)) })
    }
    private val subTextSize = RValue(0.08F, Type.Y)
    private val subTextStrings = arrayOf("YOUR", "FAVORITE", "ANIME")
    protected val subFramePositions = ArrayList<RPosition>(3).apply{
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

    private val additionalTextPositions = ArrayList<RPosition>(3).apply{
        add(RPosition(RValue(0.17f, Type.Y), RValue(0.549f, Type.Y)))
        add(RPosition(last()).apply { add(RValue(), RValue(0.2f, Type.Y)) })
        add(RPosition(last()).apply { add(RValue(), RValue(0.2f, Type.Y)) })
    }
    private val additionalTextSize = RValue(0.04F, Type.Y)
    private val additionalTextStrings = arrayOf("Help", "Others", "Choose")

    init {
        if (treeData != null)
            this.toAnotherLayer(intArrayOf())
    }

    override fun update() {
        val context = contextRef.get() ?: return
        addText(context)
        super.update()
        addFrames(context)
        addCurves(context)
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

    private fun toAnotherLayer(index: IntArray) {
        var resultLayer = treeData
        for (i in index) {
            resultLayer = resultLayer!!.tree?.getOrNull(i) ?: return
        }
        currentElement = resultLayer

        updateLayer()
    }

    protected open fun updateLayer() {
        val layer = currentElement ?: return

        addMainElement(layer.bitmap, layer.index)
        subIcons.clear()
        if (layer.tree != null) {
            for (i in layer.tree!!)
                addSubElement(i.bitmap, i.index)
        }
    }

    private fun getCurrentIndex() : IntArray? {
        return currentElement?.index
    }

    private fun addMainText(context: Context, text: String, color: Int) {
        val font = Typeface.create(ResourcesCompat.getFont(context, R.font.intro), Typeface.BOLD)

        treeView.addElement(
            Text(
                mainTextPosition,
                text,
                color,
                font,
                mainTextSize,
                90f
            )
        )
    }

    private fun addSubText(context: Context, text: String, color: Int, index: Int) {
        val font = ResourcesCompat.getFont(context, R.font.intro)

        treeView.addElement(
            Text(subTextPositions[index], text, color, font, subTextSize)
        )
    }

    private fun addAdditionalText(context: Context, text: String, color: Int, index: Int) {
        val font = Typeface.create(
            ResourcesCompat.getFont(context, R.font.fira_sans), Typeface.BOLD
        )

        treeView.addElement(
            Text(additionalTextPositions[index], text, color, font, additionalTextSize)
        )
    }

    private fun addText(context: Context) {
        val textColor = context.resources.getColor(R.color.text_color, null)

        if (currentElement == null)
            addMainText(context, "CHOOSE", textColor)
        else
            addMainText(context, currentElement!!.name, textColor)

        val border = currentElement?.tree?.size ?: 0
        for (i in 0 until border) {
            addSubText(context, currentElement!!.tree!![i].name, textColor, i)
            addAdditionalText(context, "Anime studio", textColor, i)
        }

        for (i in border until 3) {
            addSubText(context, subTextStrings[i], textColor, i)
            addAdditionalText(context, additionalTextStrings[i], textColor, i)
        }
    }

    protected fun addMainFrame(color: Int) {
        treeView.addElement(
            Circle(
                mainFramePos,
                mainIconRadius,
                color,
                Paint.Style.STROKE, RValue(0.003f, Type.Y)
            )
        )
    }

    protected fun addSubFrame(index: Int, color: Int) {
        treeView.addElement(
            Circle(
                subFramePositions[index],
                subIconRadius,
                color,
                Paint.Style.STROKE,
                RValue(0.003f, Type.Y)
            )
        )
    }

    private fun addFrames(context: Context) {
        if (mainIcon == null)
            return

        val color = context.resources.getColor(R.color.elements_color, null)

        addMainFrame(color)

        for (i in 0 until subIcons.size) {
            addSubFrame(i, color)
        }
    }

    private fun addCurves(context: Context) {
        val color = context.resources.getColor(R.color.elements_color, null)

        for (i in 0 until subIcons.size) {
            treeView.addElement(
                createCurveToSubIcon(mainFramePos, subFramePositions[i], color)
            )
        }
    }

    protected fun createCurveToSubIcon(mainPos: RPosition, subPos: RPosition, color: Int) : Curve {
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