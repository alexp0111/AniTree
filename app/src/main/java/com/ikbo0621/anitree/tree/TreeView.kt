package com.ikbo0621.anitree.tree

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.*
import android.util.AttributeSet
import android.view.GestureDetector
import android.view.GestureDetector.SimpleOnGestureListener
import android.view.HapticFeedbackConstants
import android.view.MotionEvent
import android.view.View
import android.widget.Scroller
import com.ikbo0621.anitree.tree.elements.TreeElement


class TreeView(context: Context, attrs: AttributeSet) : View(context, attrs) {
    var elements: ArrayList<TreeElement> = ArrayList()
        private set
    var screenSize: Point? = null
        private set
    var selectedElement: TreeElement? = null
        private set
    private var currentPos = PointF(0f, 0f)
    private var canvasMatrix = Matrix()
    private var scroller = Scroller(context)
    private val gestureDetector = GestureDetector(context, object : SimpleOnGestureListener() {
        override fun onDown(e: MotionEvent): Boolean {
            for (it in elements.iterator()) {
                if (it.isSelected(PointF(e.x - currentPos.x, e.y - currentPos.y))) {
                    selectedElement = it
                    break
                }
            }
            return true
        }

        override fun onLongPress(e: MotionEvent) {
            performHapticFeedback(HapticFeedbackConstants.LONG_PRESS)
            performLongClick()
        }

        override fun onSingleTapUp(e: MotionEvent): Boolean {
            performClick()
            return true
        }
    })

    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        screenSize = Point(w, h)

        // Account for padding
        for (it in elements.iterator()) {
            it.correctPos(w, h)
        }
    }

    override fun onDraw(canvas: Canvas) {
        canvasMatrix.apply {
            reset()
            postTranslate(scroller.currX.toFloat(), scroller.currY.toFloat())
        }
        canvas.setMatrix(canvasMatrix)

        for (it in elements.iterator()) {
            it.draw(canvas)
        }
    }

    fun clearElements() = elements.clear()

    fun addElement(element: TreeElement) {
        if (screenSize != null)
            element.correctPos(screenSize!!.x, screenSize!!.y)
        elements.add(element)
    }

    fun deleteElement(index: Int) {
        if (index < 0 || index >= elements.size)
            return
        elements.removeAt(index)
    }

    @SuppressLint("ClickableViewAccessibility")
    override fun onTouchEvent(event: MotionEvent): Boolean {
        return gestureDetector.onTouchEvent(event)
    }
}
