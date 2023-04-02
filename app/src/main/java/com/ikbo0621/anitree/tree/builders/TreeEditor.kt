package com.ikbo0621.anitree.tree.builders

import android.content.Context
import android.graphics.Bitmap
import com.ikbo0621.anitree.R
import com.ikbo0621.anitree.tree.TreeView
import com.ikbo0621.anitree.tree.elements.Curve
import com.ikbo0621.anitree.tree.elements.buttons.CrossButton
import com.ikbo0621.anitree.tree.elements.buttons.MainSchemeButton
import com.ikbo0621.anitree.tree.elements.buttons.SchemeButton
import com.ikbo0621.anitree.tree.positioning.RPosition
import com.ikbo0621.anitree.tree.positioning.RRect
import com.ikbo0621.anitree.tree.positioning.RValue
import com.ikbo0621.anitree.tree.structures.TreeData
import java.lang.ref.WeakReference

class TreeEditor(
    treeView: TreeView,
    contextRef: WeakReference<Context>,
    treeData: TreeData,
) : TreeViewer(treeView, contextRef, treeData) {
    private val crossButtons = arrayOf<CrossButton?>(null, null, null)

    override fun update() {
        addSchemeButtons()
        super.update()
        //addCrossButtons(contextRef.get()!!)
        addFramesAndCurvesToScheme()

//        treeView.addElement(
//            CrossButton(
//                RPosition(RValue(0.5f, RValue.Type.X), RValue(0.5f, RValue.Type.Y)),
//                RRect(
//                    RPosition(RValue(0.1f, RValue.Type.X), RValue(0.1f, RValue.Type.Y)),
//                    RPosition(RValue(0.9f, RValue.Type.X), RValue(0.9f, RValue.Type.Y))
//                ),
//                RValue(0.1f)
//            )
//        )
    }

    override fun updateLayer() {
        addScheme()
        super.updateLayer()
    }

    fun addSubElement(name: String, studio: String, bitmap: Bitmap) {
        if (animator.isAnimating)
            return

        val index = getIndex(currentElement)
        currentElement.addSubElement(name, studio, bitmap, index)
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

    fun getTree() : TreeData {
        return treeData
    }

    fun showCrossButtons() {
        val context = contextRef.get() ?: return

        for (i in 0 until subIcons.size) {
            createCrossButton(context, i)
        }
        treeView.invalidate()
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
        addSubFrame(schemeIndex, elementsColor)
    }

    private fun addScheme() {
        val context = contextRef.get() ?: return
        val schemeColor = context.resources.getColor(R.color.scheme_color, null)
        val elementsColor = context.resources.getColor(R.color.elements_color, null)

//        if (mainIcon == null) {
//            treeView.addElement(MainSchemeButton(layout.mainIconPos, layout.mainIconRadius, schemeColor))
//            addMainFrame(elementsColor)
//        }

        val schemeIndex = subIcons.size
        if (schemeIndex >= 3)
            return

//        treeView.addElement(
//            SchemeButton(layout.subIconsPositions[schemeIndex], layout.subIconRadius, schemeColor)
//        )

        createCurveToSubIcon(context, schemeIndex)
        addSubFrame(schemeIndex, elementsColor)
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

    private fun createCrossButton(context: Context, index: Int) {
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

        treeView.addElement(crossButtons[index]!!)
    }
}