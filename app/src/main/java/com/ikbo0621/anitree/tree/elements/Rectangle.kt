package com.ikbo0621.anitree.tree.elements

import android.graphics.*
import com.ikbo0621.anitree.tree.positioning.RPosition

class Rectangle(
    override var relativePos: RPosition,
    rectPoints: RectR,
    private val renderType: Paint.Style? = null,
    private val color: Int? = null, // not rendered if null
    private val strokeWidth: Float? = null
    ) : TreeElement() {
    var rectPoints = rectPoints
        private set
    override var paint: Paint = Paint().apply {
        isAntiAlias = true
        style = renderType ?: Paint.Style.FILL
        color = this@Rectangle.color ?: Color.BLACK
        strokeWidth = this@Rectangle.strokeWidth ?: 1f
    }

    override fun draw(canvas: Canvas) {
        if (color == null)
            return
        canvas.drawRect(rectPoints.getAbsoluteRect(screenSize.x, screenSize.y, absolutePos), paint)
    }

    override fun isSelected(position: PointF): Boolean {
        val absoluteRect = rectPoints.getAbsoluteRect(screenSize.x, screenSize.y, absolutePos)
        return absoluteRect.contains(position.x, position.y)
    }

    override fun correctPos(w: Int, h: Int) {
        super.correctPos(w, h)

        absolutePos = relativePos.getAbsolute(w, h)
    }

    data class RectR(val leftUpper: RPosition, val rightBottom: RPosition) {
        fun getAbsoluteRect(w: Int, h: Int, offset: PointF) : RectF {
            val absoluteLeftUpper = leftUpper.getAbsolute(w, h)
            val absoluteRightBottom = rightBottom.getAbsolute(w, h)
            return RectF(
                absoluteLeftUpper.x, absoluteLeftUpper.y,
                absoluteRightBottom.x, absoluteRightBottom.y
            ).apply {
                offset(offset.x, offset.y)
            }
        }
    }
}