package com.ikbo0621.anitree.tree.builders

import android.content.Context
import android.graphics.Bitmap
import com.ikbo0621.anitree.R
import com.ikbo0621.anitree.tree.TreeView
import com.ikbo0621.anitree.tree.elements.Icon
import com.ikbo0621.anitree.tree.elements.buttons.CrossButton
import com.ikbo0621.anitree.tree.elements.buttons.MainSchemeButton
import com.ikbo0621.anitree.tree.elements.buttons.SchemeButton
import com.ikbo0621.anitree.tree.positioning.RPosition
import com.ikbo0621.anitree.tree.positioning.RRect
import com.ikbo0621.anitree.tree.structures.TreeData

class TreeEditor(
    treeView: TreeView,
    context: Context,
    treeData: TreeData,
) : TreeViewer(treeView, context, treeData) {
    // Preserving elements to optimize rendering
    private val crossButtons = arrayOf<CrossButton?>(null, null, null)
    private var isRemoval = false

    override fun update() {
        addSchemeButtons()
        super.update()
        addFramesAndCurvesToScheme()
        addCrossButtons()
    }

    override fun updateLayer() {
        addScheme()
        super.updateLayer()
    }

    override fun switchBottomButton() : Boolean {
        if (!isBottomButtonActivated) {
            isBottomButtonActivated = true
            invalidate()
        }
        return isBottomButtonActivated
    }

    fun addSubElement(name: String, studio: String, bitmap: Bitmap) {
        if (animator.isAnimating || isRemoval)
            return
        isRemoval = false
        isBottomButtonActivated = false

        val index = getIndex(currentElement)
        treeData.addSubElement(name, studio, bitmap, index)
        super.addSubElement(bitmap, index)
        invalidate()
    }

    fun deleteElement(index: IntArray?) {
        if (animator.isAnimating)
            return
        val currentLayer = currentElement
        val elementIndex = index ?: return
        if (elementIndex.contentEquals(currentElement.index)) // Do not remove the main element
            return
        isBottomButtonActivated = false

        for (i in subIcons) {
            if (i.index.contentEquals(elementIndex)) {
                currentLayer.tree?.removeAt(subIcons.indexOf(i))
                break
            }
        }

        // Correct indices
        for (i in currentLayer.tree!!) {
            i.index = getIndex(currentLayer, currentLayer.tree!!.indexOf(i))
        }

        updateLayer()
    }

    override fun toNextLayer(nextElement: Icon?) {
        if (isRemoval)
            return
        super.toNextLayer(nextElement)
    }

    override fun toPreviousLayer() {
        showCrossButtons(false)
        super.toPreviousLayer()
    }

    fun getTree() : List<String?> {
        val result = arrayListOf<String?>().apply {
            ensureCapacity(13)
            while (size < 13)
                add(null);
        }

        result[0] = treeData.name

        val a = currentElement

        for (i in 0 until 3) {
            result[1 + i] = treeData.tree?.getOrNull(i)?.name
            val subTree = treeData.tree?.getOrNull(i)?.tree
            for (j in 0 until 3) {
                result[(1 + i) * 3 + (1 + j)] = subTree?.getOrNull(j)?.name
            }
        }

        return result
    }

    fun showCrossButtons(show: Boolean) {
        if (animator.isAnimating)
            return

        isRemoval = show

        if (isRemoval)
            addCrossButtons()
        invalidate()
    }

    private fun addSchemeButtons() {
        val context = contextRef.get() ?: return
        val schemeColor = context.resources.getColor(R.color.scheme_color, null)
        val elementsColor = context.resources.getColor(R.color.elements_color, null)

        if (mainIcon == null) {
            treeView.addElement(MainSchemeButton(layout.mainIconPos, layout.mainIconRadius, schemeColor))
            addMainFrame(elementsColor)
        }

        val schemeIndex = subIcons.size
        if (schemeIndex >= 3)
            return

        treeView.addElement(
            SchemeButton(layout.subIconsPositions[schemeIndex], layout.subIconRadius, schemeColor)
        )
    }

    private fun addFramesAndCurvesToScheme() {
        val context = contextRef.get() ?: return
        val elementsColor = context.resources.getColor(R.color.elements_color, null)

        addMainFrame(elementsColor)

        val schemeIndex = subIcons.size
        if (schemeIndex >= 3)
            return

        createCurveToSubIcon(context, schemeIndex)
        addSubFrame(elementsColor, schemeIndex)
    }

    private fun addScheme() {
        val context = contextRef.get() ?: return
        val elementsColor = context.resources.getColor(R.color.elements_color, null)

        val schemeIndex = subIcons.size
        if (schemeIndex >= 3)
            return

        createCurveToSubIcon(context, schemeIndex)
        addSubFrame(elementsColor, schemeIndex)
    }

    // Returns the index of the new element
    private fun getIndex(parent: TreeData) : IntArray {
        val subIndex = parent.tree?.size ?: 0

        val result = IntArray(parent.index.size + 1)

        for (i in parent.index.indices)
            result[i] = parent.index[i]
        result[result.lastIndex] = subIndex

        return result
    }

    // Returns the index of the element at the given position
    private fun getIndex(parent: TreeData, position: Int) : IntArray {
        val result = IntArray(parent.index.size + 1)

        for (i in parent.index.indices)
            result[i] = parent.index[i]
        result[result.lastIndex] = position

        return result
    }

    private fun addCrossButtons() {
        if (!isRemoval)
            return
        val context = contextRef.get() ?: return

        for (i in 0 until (currentElement.tree?.size ?: 0)) {
            addCrossButton(context, i)
        }
    }

    private fun addCrossButton(context: Context, index: Int) {
        if (crossButtons[index] == null) {
            val color = context.resources.getColor(R.color.elements_color, null)
            crossButtons[index] = CrossButton(
                layout.subFramePositions[index],
                RRect(
                    RPosition(),
                    RPosition(layout.subIconRadius, layout.subIconRadius)
                ),
                layout.lineWidth,
                color
            )
        }
        crossButtons[index]!!.index = currentElement.tree!![index].index

        treeView.addElement(crossButtons[index]!!)
    }
}