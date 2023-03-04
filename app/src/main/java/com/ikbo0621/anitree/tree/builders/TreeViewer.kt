package com.ikbo0621.anitree.tree.builders

import android.content.Context
import android.graphics.Typeface
import androidx.core.content.res.ResourcesCompat
import com.ikbo0621.anitree.R
import com.ikbo0621.anitree.tree.TreeView
import com.ikbo0621.anitree.tree.elements.Circle
import com.ikbo0621.anitree.tree.elements.Icon
import com.ikbo0621.anitree.tree.elements.Text
import com.ikbo0621.anitree.tree.elements.TreeElement
import com.ikbo0621.anitree.tree.positioning.RPosition
import com.ikbo0621.anitree.tree.positioning.RValue
import com.ikbo0621.anitree.tree.positioning.RValue.Type
import com.ikbo0621.anitree.tree.structures.TreeData
import java.lang.ref.WeakReference

open class TreeViewer(
    treeView: TreeView,
    contextRef: WeakReference<Context>,
    protected var treeData: TreeData? = null,
) : TreeBuilder(treeView, contextRef) {
    protected var currentElement = treeData

    private val mainTextPosition = RPosition(
        RValue(-0.05F,  Type.Y), RValue(-0.04F,  Type.Y)
    )
    private val mainTextSize = RValue(0.3F, Type.Y)

    private val subTextPositions = ArrayList<RPosition>(3).apply{
        add(RPosition(RValue(0.17f, Type.Y), RValue(0.518f, Type.Y)))
        add(RPosition(last()).apply { add(RValue(), RValue(0.2f, Type.Y)) })
        add(RPosition(last()).apply { add(RValue(), RValue(0.2f, Type.Y)) })
    }
    private val subTextSize = RValue(0.08F, Type.Y)
    private val subTextStrings = arrayOf("YOUR", "FAVORITE", "ANIME")

    private val additionalTextPositions = ArrayList<RPosition>(3).apply{
        add(RPosition(RValue(0.17f, Type.Y), RValue(0.549f, Type.Y)))
        add(RPosition(last()).apply { add(RValue(), RValue(0.2f, Type.Y)) })
        add(RPosition(last()).apply { add(RValue(), RValue(0.2f, Type.Y)) })
    }
    private val additionalTextSize = RValue(0.04F, Type.Y)
    private val additionalTextStrings = arrayOf("Help", "Others", "Choose")

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

        updateLayer()
    }

    protected fun updateLayer() {
        val layer = currentElement ?: return

        treeView.clearElements()
        subIcons.clear()

        addMainElement(layer.bitmap, layer.index)
        if (layer.tree != null) {
            for (i in layer.tree!!)
                addSubElement(i.bitmap, i.index)
        }
    }

    private fun getCurrentIndex() : IntArray? {
        return currentElement?.index
    }

    protected fun addScheme() {
        val context = contextRef.get() ?: return
        val schemeColor = context.resources.getColor(R.color.scheme_color, null)

        if (mainIcon == null) {
            mainIcon = Circle(mainIconPos, mainIconRadius, schemeColor)
            mainIcon!!.index = intArrayOf(0)
        }
        addText()

        if (subIcons.size >= 3)
            return

        subIcons.add(Circle(subIconsPositions[subIcons.size], subIconRadius, schemeColor))
    }

    private fun addMainText(context: Context, text: String, color: Int) {
        val font = Typeface.create(ResourcesCompat.getFont(context, R.font.intro), Typeface.BOLD)

        otherElements.add(
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

        otherElements.add(
            Text(subTextPositions[index], text, color, font, subTextSize)
        )
    }

    private fun addAdditionalText(context: Context, text: String, color: Int, index: Int) {
        val font = Typeface.create(
            ResourcesCompat.getFont(context, R.font.fira_sans), Typeface.BOLD
        )

        otherElements.add(
            Text(additionalTextPositions[index], text, color, font, additionalTextSize)
        )
    }

    private fun getMainText(context: Context, text: String, color: Int) : Text {
        val font = Typeface.create(ResourcesCompat.getFont(context, R.font.intro), Typeface.BOLD)

        return Text(mainTextPosition, text, color, font, mainTextSize, 90f)
    }
    protected fun addMainText() {
        val context = contextRef.get() ?: return
        val textColor = context.resources.getColor(R.color.text_color, null)

        if (currentElement == null)
            otherElements[0] = getMainText(context, "CHOOSE", textColor)
            //addMainText(context, "CHOOSE", textColor)
        else
            otherElements[0] = getMainText(context, currentElement!!.name, textColor)
            //addMainText(context, currentElement!!.name, textColor)
    }
    private fun addText() {
        otherElements.clear()
        val context = contextRef.get() ?: return
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
}