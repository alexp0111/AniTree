package com.ikbo0621.anitree.tree.builders

import android.content.Context
import android.graphics.Paint
import android.graphics.Paint.Cap
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

    private val mainStudioTextPosition = RPosition(
        RValue(-0.048F,  Type.Y), RValue(-0.04F,  Type.Y)
    )
    private val mainStudioTextSize = RValue(0.3F, Type.Y)
    protected val mainFramePos = RPosition(mainIconPos).apply {
        add(RValue(-0.01f, Type.Y), RValue(0.01f, Type.Y))
    }
    private val mainNameText = RPosition(mainFramePos).apply {
        add(-mainIconRadius, mainIconRadius)
    }

    private val subStudioTextPositions = ArrayList<RPosition>(3).apply{
        add(RPosition(RValue(0.16f, Type.Y), RValue(0.518f, Type.Y)))
        add(RPosition(last()).apply { add(RValue(), RValue(0.2f, Type.Y)) })
        add(RPosition(last()).apply { add(RValue(), RValue(0.2f, Type.Y)) })
    }
    private val subStudioTextSize = RValue(0.08F, Type.Y)
    private val subStudioTextStrings = arrayOf("YOUR", "FAVORITE", "ANIME")
    protected val subFramePositions = ArrayList<RPosition>(3).apply{
        add(
            RPosition(subIconsPositions.first()).apply {
                add(RValue(-0.01f, Type.Y), RValue(+0.01f, Type.Y))
            }
        )
        add(RPosition(last()).apply { add(RValue(), RValue(0.2f, Type.Y)) })
        add(RPosition(last()).apply { add(RValue(), RValue(0.2f, Type.Y)) })
    }

    private val subNameTextPositions = ArrayList<RPosition>(3).apply{
        add(RPosition(RValue(0.17f, Type.Y), RValue(0.546f, Type.Y)))
        add(RPosition(last()).apply { add(RValue(), RValue(0.2f, Type.Y)) })
        add(RPosition(last()).apply { add(RValue(), RValue(0.2f, Type.Y)) })
    }
    private val subNameTextSize = RValue(0.04F, Type.Y)
    private val subNameTextStrings = arrayOf("Help", "Others", "Choose")
    protected val lineWidth = RValue(0.003f, Type.Y)

    init {
        if (treeData != null)
            this.toAnotherLayer(intArrayOf())
    }

    override fun update() {
        val context = contextRef.get() ?: return
        addBottomText(context)
        super.update()
        addCurves(context)
        addFrames(context)
        addUpperText(context)
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

    private fun addMainStudioText(context: Context, text: String, color: Int) {
        val font = Typeface.create(ResourcesCompat.getFont(context, R.font.intro), Typeface.BOLD)

        treeView.addElement(
            Text(
                mainStudioTextPosition,
                text,
                color,
                font,
                mainStudioTextSize,
                90f
            )
        )
    }

    private fun addSubStudioText(context: Context, text: String, color: Int, index: Int) {
        val font = ResourcesCompat.getFont(context, R.font.intro)

        treeView.addElement(
            Text(subStudioTextPositions[index], text, color, font, subStudioTextSize)
        )
    }

    private fun addSubNameText(context: Context, text: String, color: Int, index: Int) {
        val font = Typeface.create(
            ResourcesCompat.getFont(context, R.font.fira_sans), Typeface.BOLD
        )

        treeView.addElement(
            Text(subNameTextPositions[index], text, color, font, subNameTextSize)
        )
    }

    private fun addMainNameText(context: Context, text: String, color: Int) {
        val font = ResourcesCompat.getFont(context, R.font.intro)

        treeView.addElement(
            Text(mainNameText, text, color, font, subStudioTextSize, 90f)
        )
    }

    private fun addBottomText(context: Context) {
        val textColor = context.resources.getColor(R.color.text_color, null)

        if (currentElement == null)
            addMainStudioText(context, "CHOOSE", textColor)
        else
            addMainStudioText(context, currentElement!!.studio, textColor)

        val border = currentElement?.tree?.size ?: 0
        for (i in 0 until border) {
            addSubStudioText(context, currentElement!!.tree!![i].studio, textColor, i)
        }

        for (i in border until 3) {
            addSubStudioText(context, subStudioTextStrings[i], textColor, i)
            addSubNameText(context, subNameTextStrings[i], textColor, i)
        }
    }

    private fun addUpperText(context: Context) {
        val textColor = context.resources.getColor(R.color.elements_color, null)

        if (currentElement != null)
            addMainNameText(context, currentElement!!.name, textColor)

        val border = currentElement?.tree?.size ?: 0
        for (i in 0 until border) {
            addSubNameText(context, currentElement!!.tree!![i].name, textColor, i)
        }
    }

    protected fun addMainFrame(color: Int) {
        treeView.addElement(
            Circle(
                mainFramePos,
                mainIconRadius,
                color,
                Paint.Style.STROKE,
                lineWidth
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
                lineWidth
            )
        )
    }

    private fun addFrames(context: Context) {
        if (mainIcon == null)
            return

        val mainColor = context.resources.getColor(R.color.highlight, null)
        val subColor = context.resources.getColor(R.color.elements_color, null)

        addMainFrame(mainColor)

        for (i in 0 until subIcons.size) {
            addSubFrame(i, subColor)
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

    protected fun createCurveToSubIcon(
        mainPos: RPosition,
        subPos: RPosition,
        color: Int,
        cap: Cap = Cap.ROUND
    ) : Curve {
        val startPos = RPosition(mainPos).apply {
            add(RValue(), mainIconRadius)
        }
        val cornerPos = RPosition(
            mainPos.getRelativeX(), subPos.getRelativeY()
        )
        val upperAnchorPoint = RPosition(cornerPos).apply {
            add(RValue(), -subIconRadius)
        }
        val downAnchorPoint = RPosition(cornerPos).apply {
            add(subIconRadius, RValue())
        }
        val endPos = RPosition(subPos).apply {
            add(-subIconRadius, RValue())
        }

        return Curve(
            arrayOf(
                Line.LinePoint(startPos, false),
                Line.LinePoint(upperAnchorPoint, false),
                Line.LinePoint(cornerPos, true),
                Line.LinePoint(downAnchorPoint, false),
                Line.LinePoint(endPos, false)
            ),
            lineWidth,
            color,
            cap=cap
        )
    }
}