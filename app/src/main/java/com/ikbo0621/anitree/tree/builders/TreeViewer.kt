package com.ikbo0621.anitree.tree.builders

import android.animation.ValueAnimator
import android.content.Context
import android.graphics.Paint
import android.graphics.Paint.Cap
import android.graphics.Point
import android.graphics.PointF
import android.graphics.Typeface
import android.view.animation.DecelerateInterpolator
import androidx.core.animation.doOnEnd
import androidx.core.content.res.ResourcesCompat
import com.ikbo0621.anitree.MainActivity
import com.ikbo0621.anitree.R
import com.ikbo0621.anitree.tree.TreeAnimator
import com.ikbo0621.anitree.tree.TreeView
import com.ikbo0621.anitree.tree.elements.*
import com.ikbo0621.anitree.tree.positioning.RPosition
import com.ikbo0621.anitree.tree.positioning.RValue
import com.ikbo0621.anitree.tree.positioning.RValue.Type
import com.ikbo0621.anitree.tree.structures.TreeData
import java.lang.ref.WeakReference
import kotlin.math.abs

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
    private val mainNameTextPosition = RPosition(mainFramePos).apply {
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

    // Preserving Elements to Optimize Rendering
    private var mainStudioText: Text? = null
    private var mainNameText: Text? = null
    private val subStudioTexts = arrayOf<Text?>(null, null, null)
    private val subNameTexts = arrayOf<Text?>(null, null, null)
    private val curves = arrayOf<Curve?>(null, null, null)

    protected var isAnimating = false

    init {
        if (treeData != null)
            this.toAnotherLayer(intArrayOf())
    }

    fun a(context: Context) {
        addBottomText(context)
        super.update()
        addCurves(context)
        addFrames(context)
        addUpperText(context)
    }

    override fun update() {
        val context = contextRef.get() ?: return

//        if (mainStudioText != null) {
//            val thread: Thread = object : Thread() {
//                val distance = -50f
//                val initPos = mainStudioText!!.getAbsPos()
//                var animatedPos = initPos.x
//                var duration = 3000
//                val pixelsPerFrame = 16 * distance / duration
//                val border = initPos.x + distance
//
//                override fun run() {
//                    try {
//                        while (animatedPos > border) {
//                            sleep(16)
//                            animatedPos += pixelsPerFrame
//                            //customView.position.set(animatedPos, initPos)
//                            //customView.postInvalidate()
//                            mainStudioText!!.setRPos(animatedPos, initPos.y)
//                            treeView.postInvalidate()
//                        }
//                        a(context)
//                    } catch (e: InterruptedException) {
//                        e.printStackTrace()
//                    }
//                }
//            }
//            thread.start()
//        } else {
//            a(context)
//        }

        addBottomText(context)
        super.update()
        addCurves(context)
        addFrames(context)
        addUpperText(context)
    }

    fun createThread(index: IntArray, distance: Float) {
        val context = contextRef.get() ?: return

        val thread: Thread = object : Thread() {
            val initPos = mainStudioText!!.getAbsPos()
            var animatedPos = initPos.x
            var duration = 100
            val pixelsPerFrame = 16 * distance / duration
            val border = initPos.x + distance

            override fun run() {
                isAnimating = true
                try {
                    while (abs(animatedPos) < abs(border)) {
                        sleep(16)
                        animatedPos += pixelsPerFrame
//                        mainStudioText!!.setAbsPos(animatedPos, initPos.y)
//                        treeView.postInvalidate()
                        (context as MainActivity).runOnUiThread {
                            mainStudioText!!.setAbsPos(animatedPos, initPos.y)
                            treeView.postInvalidate()
                        }
                    }


                    (context as MainActivity).runOnUiThread {
                        toAnotherLayer(index)
                        invalidate()
                    }

                    isAnimating = false
                    //update()
                    //treeView.postInvalidate()
                } catch (e: InterruptedException) {
                    e.printStackTrace()
                    isAnimating = false
                }
            }
        }
        thread.start()
    }

    fun startAnimation(index: IntArray, distance: Float) {
        val context = contextRef.get() ?: return
        val initPos = mainStudioText!!.getAbsPos()
        val border = initPos.x + distance
        isAnimating = true

        val animator = ValueAnimator().apply {
            setFloatValues(initPos.x, border)
            duration = 200
            interpolator = DecelerateInterpolator()
            addUpdateListener {
                (context as MainActivity).runOnUiThread {
                    mainStudioText!!.setAbsPos(animatedValue as Float, initPos.y)
                    treeView.postInvalidate()
                }
            }
            doOnEnd {
                (context as MainActivity).runOnUiThread {
                    toAnotherLayer(index)
                    invalidate()
                }

                isAnimating = false
            }
        }
        animator.start()
    }

    fun toPreviousLayer() {
        if (isAnimating)
            return

        val currentIndex = getCurrentIndex() ?: return
        if (currentIndex.isEmpty())
            return
        val previousIndex = currentIndex.copyOf(currentIndex.size - 1)

//        val thread: Thread = object : Thread() {
//            val distance = 500f
//            val initPos = mainStudioText!!.getAbsPos()
//            var animatedPos = initPos.x
//            var duration = 500
//            val pixelsPerFrame = 16 * distance / duration
//            val border = initPos.x + distance
//
//            override fun run() {
//                try {
//                    while (animatedPos < border) {
//                        sleep(16)
//                        animatedPos += pixelsPerFrame
//                        //customView.position.set(animatedPos, initPos)
//                        //customView.postInvalidate()
//                        mainStudioText!!.setAbsPos(animatedPos, initPos.y)
//                        treeView.postInvalidate()
//                    }
//
//
//                    (context as MainActivity).runOnUiThread {
//                        toAnotherLayer(previousIndex)
//                        invalidate()
//                    }
//
//                    //update()
//                    //treeView.postInvalidate()
//                } catch (e: InterruptedException) {
//                    e.printStackTrace()
//                }
//            }
//        }
//        thread.start()

        createThread(previousIndex, 500f)

        //toAnotherLayer(previousIndex)
    }

    fun toNextLayer(nextElement: TreeElement?) {
        if (isAnimating || nextElement == null || mainStudioText == null)
            return
        val nextIndex = nextElement.index ?: return

        //createThread(nextIndex, -500f)
        //startAnimation(nextIndex, -300f)
        //startAnimation(nextIndex, -mainStudioTextSize.getAbsolute(mainStudioText!!.screenSize) * 0.5f)

        val context = contextRef.get() ?: return
        isAnimating = true
        TreeAnimator(contextRef, treeView).apply {
            elements.add(mainStudioText)
            elements.add(mainNameText)
            elements.add(mainIcon)

            elements.addAll(subStudioTexts)
            elements.addAll(subNameTexts)
            elements.addAll(subIcons)

            elements.addAll(curves)

            setOnEndFunction {
                (context as MainActivity).runOnUiThread {
                    toAnotherLayer(nextIndex)
                    invalidate()
                }

                isAnimating = false
            }
//            startAnimation(PointF(
//                -mainStudioTextSize.getAbsolute(mainStudioText!!.screenSize) * 0.5f,
//                0f
//            ))
            startAnimation(PointF(
                mainIcon!!.getAbsPos().x - nextElement.getAbsPos().x,
                mainIcon!!.getAbsPos().y - nextElement.getAbsPos().y
            ))
        }

        //toAnotherLayer(nextIndex)
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
        val context = contextRef.get() ?: return

        addMainElement(layer.bitmap, layer.index)
        subIcons.clear()
        if (layer.tree != null) {
            for (i in layer.tree!!)
                addSubElement(i.bitmap, i.index)
//            for (i in curves.indices)
//                curves[i] = null
//            addCurves(context)
        }
    }

    private fun getCurrentIndex() : IntArray? {
        return currentElement?.index
    }

    private fun addMainStudioText(context: Context, text: String, color: Int) {
        if (mainStudioText == null) {
            val font =
                Typeface.create(ResourcesCompat.getFont(context, R.font.intro), Typeface.BOLD)
            mainStudioText = Text(
                mainStudioTextPosition,
                String(),
                color,
                font,
                mainStudioTextSize,
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
                Text(subStudioTextPositions[index], String(), color, font, subStudioTextSize)
        }
        subStudioTexts[index]!!.text = text

        treeView.addElement(subStudioTexts[index]!!)
    }

    private fun addSubNameText(context: Context, text: String, color: Int, index: Int) {
        if (subNameTexts[index] == null) {
            val font =
                Typeface.create(ResourcesCompat.getFont(context, R.font.fira_sans), Typeface.BOLD)
            subNameTexts[index] =
                Text(subNameTextPositions[index], String(), color, font, subNameTextSize)
        }
        subNameTexts[index]!!.text = text
        subNameTexts[index]!!.textColor = color

        treeView.addElement(subNameTexts[index]!!)
    }

    private fun addMainNameText(context: Context, text: String, color: Int) {
        if (mainNameText == null) {
            val font = ResourcesCompat.getFont(context, R.font.intro)
            mainNameText =
                Text(mainNameTextPosition, text, color, font, subStudioTextSize, 90f)
        }
        mainNameText!!.text = text

        treeView.addElement(mainNameText!!)
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

    private fun createCurveToSubIcon(context: Context, index: Int) {
        if (curves[index] == null) {
            val color = context.resources.getColor(R.color.elements_color, null)
            curves[index] = createCurveToSubIcon(mainFramePos, subFramePositions[index], color)
        }

        treeView.addElement(curves[index]!!)
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