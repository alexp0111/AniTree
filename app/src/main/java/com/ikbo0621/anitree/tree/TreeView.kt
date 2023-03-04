package com.ikbo0621.anitree.tree

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Canvas
import android.graphics.Matrix
import android.graphics.Point
import android.graphics.PointF
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.View
import android.widget.Scroller
import com.ikbo0621.anitree.tree.elements.TreeElement

class TreeView(context: Context, attrs: AttributeSet) : View(context, attrs) {
    private var elements: ArrayList<TreeElement> = ArrayList()
    private var currentPos = PointF(0f, 0f)
    private var canvasMatrix = Matrix()
    var selectedElement: TreeElement? = null
        private set
    private var scroller = Scroller(context)
    private var screenSize: Point? = null
    private var clickCounter = 0L

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

    fun getElement(index: Int) : TreeElement? = elements.getOrNull(index)

    fun clearElements() = elements.clear()

    fun getNumberOfElements() : Int = elements.size

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
        when (event.actionMasked) {
            MotionEvent.ACTION_DOWN -> {
                clickCounter = event.eventTime
            }
            MotionEvent.ACTION_UP -> {
                val elapsedTime = event.eventTime - clickCounter

                for (it in elements.iterator()) {
                    if (
                        it.isSelected(PointF(event.x - currentPos.x, event.y - currentPos.y))
                    ) {
                        selectedElement = it
                        break
                    }
                }

                if (elapsedTime > 400) {
                    performLongClick()
                } else {
                    performClick()
                }

                selectedElement = null
            }
        }
        return true
    }
}
