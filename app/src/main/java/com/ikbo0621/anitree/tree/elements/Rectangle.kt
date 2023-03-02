package com.ikbo0621.anitree.tree.elements

import android.graphics.*
import com.ikbo0621.anitree.tree.positioning.RPosition
import com.ikbo0621.anitree.tree.positioning.RRect
import com.ikbo0621.anitree.tree.positioning.RValue

class Rectangle(
    override var relativePos: RPosition,
    rectPoints: RRect,
    renderType: Paint.Style? = null,
    private val color: Int? = null, // not rendered if null
    private val width: RValue = RValue(0.05f)
    ) : TreeElement() {
    var rectPoints = rectPoints
        private set
    override var paint: Paint = Paint().apply {
        isAntiAlias = true
        style = renderType ?: Paint.Style.FILL
        color = this@Rectangle.color ?: Color.BLACK
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

        paint.strokeWidth = width.getAbsolute(w, h)
        absolutePos = relativePos.getAbsolute(w, h)
    }
}