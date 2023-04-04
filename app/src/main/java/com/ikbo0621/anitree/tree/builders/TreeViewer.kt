package com.ikbo0621.anitree.tree.builders

import android.content.Context
import android.graphics.Paint
import android.graphics.Paint.Cap
import android.graphics.PointF
import android.graphics.Typeface
import androidx.core.content.res.ResourcesCompat
import com.ikbo0621.anitree.R
import com.ikbo0621.anitree.animators.TreeAnimator
import com.ikbo0621.anitree.tree.TreeView
import com.ikbo0621.anitree.tree.elements.*
import com.ikbo0621.anitree.tree.elements.buttons.Button
import com.ikbo0621.anitree.tree.positioning.RPosition
import com.ikbo0621.anitree.tree.positioning.RRect
import com.ikbo0621.anitree.tree.positioning.RValue
import com.ikbo0621.anitree.tree.positioning.RValue.Type
import com.ikbo0621.anitree.tree.structures.TreeData
import java.lang.ref.WeakReference

open class TreeViewer(
    treeView: TreeView,
    contextRef: WeakReference<Context>,
    protected var treeData: TreeData
) : TreeBuilder(treeView, contextRef) {
    protected var currentElement = treeData
    protected val animator = JumpAnimator()
//    private val subStudioTextStrings = arrayOf("YOUR", "FAVORITE", "ANIME")
//    private val subNameTextStrings = arrayOf("Help", "Others", "Choose")

    // Preserving elements to optimize rendering
    private var mainStudioText: Text? = null
    private var mainNameText: Text? = null
    private val subStudioTexts = arrayOf<Text?>(null, null, null)
    private val subNameTexts = arrayOf<Text?>(null, null, null)
    private val curves = arrayOf<Curve?>(null, null, null)

    init {
        this.toAnotherLayer(intArrayOf())
    }

    override fun update() {
        val context = contextRef.get() ?: return

        addBottomText(context)
        super.update()
        addCurves(context)
        addFrames(context)
        addUpperText(context)
        addBackField()
    }

    open fun toPreviousLayer() {
        if (animator.isAnimating)
            return

        val currentIndex = getCurrentIndex()
        if (currentIndex.isEmpty())
            return
        val nextIndex = currentIndex.copyOf(currentIndex.size - 1)
        val screenSize = treeView.screenSize ?: return
        val nextPosition = layout.subFramePositions[getCurrentIndex().last()].
            getAbsolute(screenSize.x, screenSize.y)

        animator.startAnimation(
            nextIndex,
            PointF(
                nextPosition.x - mainIcon!!.getAbsPos().x,
                nextPosition.y - mainIcon!!.getAbsPos().y
            )
        )
    }

    open fun toNextLayer(nextElement: Icon?) {
        if (animator.isAnimating || nextElement == null || mainStudioText == null)
            return
        val nextIndex = nextElement.index ?: return

        animator.startAnimation(
            nextIndex,
            PointF(
                mainIcon!!.getAbsPos().x - nextElement.getAbsPos().x,
                mainIcon!!.getAbsPos().y - nextElement.getAbsPos().y
            )
        )
    }

    private fun toAnotherLayer(index: IntArray) {
        var resultLayer = treeData
        for (i in index) {
            resultLayer = resultLayer.tree?.getOrNull(i) ?: return
        }
        currentElement = resultLayer

        updateLayer()
    }

    protected open fun updateLayer() {
        val layer = currentElement

        addMainElement(layer.bitmap, layer.index)
        subIcons.clear()
        if (layer.tree != null) {
            for (i in layer.tree!!)
                addSubElement(i.bitmap, i.index)
        }
        invalidate()
    }

    private fun getCurrentIndex() : IntArray {
        return currentElement.index
    }

    private fun addMainStudioText(context: Context, text: String, color: Int) {
        if (mainStudioText == null) {
            val font =
                Typeface.create(ResourcesCompat.getFont(context, R.font.intro), Typeface.BOLD)
            mainStudioText = Text(
                layout.mainStudioTextPosition,
                String(),
                color,
                font,
                layout.mainStudioTextSize,
                90f
            )
        }
        mainStudioText!!.text = text

        treeView.addElement(mainStudioText!!)
    }

    private fun addSubStudioText(context: Context, text: String, color: Int, index: Int) {
        if (subStudioTexts[index] == null) {
            val font = ResourcesCompat.getFont(context, R.font.intro)
            subStudioTexts[index] =
                Text(layout.subStudioTextPositions[index], String(), color, font, layout.subStudioTextSize)
        }
        subStudioTexts[index]!!.text = text

        treeView.addElement(subStudioTexts[index]!!)
    }

    private fun addSubNameText(context: Context, text: String, color: Int, index: Int) {
        if (subNameTexts[index] == null) {
            val font =
                Typeface.create(ResourcesCompat.getFont(context, R.font.fira_sans), Typeface.BOLD)
            subNameTexts[index] =
                Text(layout.subNameTextPositions[index], String(), color, font, layout.subNameTextSize)
        }
        subNameTexts[index]!!.text = text
        subNameTexts[index]!!.textColor = color

        treeView.addElement(subNameTexts[index]!!)
    }

    private fun addMainNameText(context: Context, text: String, color: Int) {
        if (mainNameText == null) {
            val font = ResourcesCompat.getFont(context, R.font.intro)
            mainNameText =
                Text(layout.mainNameTextPosition, text, color, font, layout.subStudioTextSize, 90f)
        }
        mainNameText!!.text = text

        treeView.addElement(mainNameText!!)
    }

    private fun addBottomText(context: Context) {
        val textColor = context.resources.getColor(R.color.text_color, null)

        addMainStudioText(context, currentElement.studio, textColor)

        val border = currentElement.tree?.size ?: 0
        for (i in 0 until border) {
            addSubStudioText(context, currentElement.tree!![i].studio, textColor, i)
        }

        for (i in border until 3) {
            addSubStudioText(context, layout.subStudioTextStrings[i], textColor, i)
            addSubNameText(context, layout.subNameTextStrings[i], textColor, i)
        }
    }

    private fun addUpperText(context: Context) {
        val textColor = context.resources.getColor(R.color.elements_color, null)

        addMainNameText(context, currentElement.name, textColor)

        val border = currentElement.tree?.size ?: 0
        for (i in 0 until border) {
            addSubNameText(context, currentElement.tree!![i].name, textColor, i)
        }
    }

    protected fun addMainFrame(color: Int) {
        treeView.addElement(
            Circle(
                layout.mainFramePos,
                layout.mainIconRadius,
                color,
                Paint.Style.STROKE,
                layout.lineWidth
            ).apply { selectable = false }
        )
    }

    protected fun addSubFrame(index: Int, color: Int) {
        treeView.addElement(
            Circle(
                layout.subFramePositions[index],
                layout.subIconRadius,
                color,
                Paint.Style.STROKE,
                layout.lineWidth
            ).apply { selectable = false }
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
        for (i in 0 until subIcons.size) {
            createCurveToSubIcon(context, i)
        }
    }

    protected fun createCurveToSubIcon(context: Context, index: Int) {
        if (curves[index] == null) {
            val color = context.resources.getColor(R.color.elements_color, null)
            curves[index] = createCurveToSubIcon(layout.mainFramePos, layout.subFramePositions[index], color)
        }

        treeView.addElement(curves[index]!!)
    }

    private fun addBackField() {
        val rect = RRect(
            RPosition(RValue(0f, Type.X), RValue(0f, Type.Y)),
            RPosition(RValue(0.4f, Type.SmallSide), RValue(1f, Type.Y))
        )
        treeView.addElement(Button(RPosition(RValue(), RValue()), rect))
    }

    private fun createCurveToSubIcon(
        mainPos: RPosition,
        subPos: RPosition,
        color: Int,
        cap: Cap = Cap.ROUND
    ) : Curve {
        val startPos = RPosition(mainPos).apply {
            add(RValue(), layout.mainIconRadius)
        }
        val cornerPos = RPosition(
            mainPos.getRelativeX(), subPos.getRelativeY()
        )
        val upperAnchorPoint = RPosition(cornerPos).apply {
            add(RValue(), -layout.subIconRadius)
        }
        val downAnchorPoint = RPosition(cornerPos).apply {
            add(layout.subIconRadius, RValue())
        }
        val endPos = RPosition(subPos).apply {
            add(-layout.subIconRadius, RValue())
        }

        return Curve(
            arrayOf(
                Line.LinePoint(startPos, false),
                Line.LinePoint(upperAnchorPoint, false),
                Line.LinePoint(cornerPos, true),
                Line.LinePoint(downAnchorPoint, false),
                Line.LinePoint(endPos, false)
            ),
            layout.lineWidth,
            color,
            cap=cap
        )
    }

    protected inner class JumpAnimator : TreeAnimator(contextRef, treeView) {
        var isAnimating = false
            private set
        private var index = IntArray(0)

        init {
            setOnEndFunction {
                toAnotherLayer(index)

                TreeAnimator(contextRef, treeView).apply {
                    invalidate()
                    duration = 400
                    addElement(treeView.elements, AdditionalEffect.FADEIN)

                    setOnEndFunction {
                        invalidate()
                        isAnimating = false
                    }
                    startAnimation(PointF())
                }
            }
        }

        private fun setElements() {
            elements.clear()
            elements.ensureCapacity(treeView.elements.size)

            for (i in treeView.elements) {
                if (i is Text || i is Icon) {
                    addElement(i, AdditionalEffect.FADEOUT)
                } else {
                    addElement(i, AdditionalEffect.FADEOUT, MovementType.NONE)
                }
            }
        }

        fun startAnimation(index: IntArray, dPos: PointF) {
            isAnimating = true
            animator.setElements()
            this.index = index
            super.startAnimation(dPos)
        }
    }
}