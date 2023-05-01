package com.ikbo0621.anitree.tree.elements.buttons

import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.Path
import android.graphics.PointF
import android.graphics.RectF
import com.ikbo0621.anitree.tree.positioning.RPosition
import com.ikbo0621.anitree.tree.positioning.RRect
import com.ikbo0621.anitree.tree.positioning.RValue

class CrossButton(
    relativePos: RPosition,
    rectPoints: RRect,
    private val width: RValue,
    color: Int = Color.BLACK,
    cap: Paint.Cap = Paint.Cap.ROUND
) : Button(relativePos, rectPoints) {
    override var paint: Paint = Paint().apply {
        isAntiAlias = true
        style = Paint.Style.STROKE
        this.color = color
        strokeCap = cap
    }
    private val path = Path()

    override fun draw(canvas: Canvas) {
        canvas.drawPath(path, paint)
    }

    override fun correctPos(w: Int, h: Int) {
        super.correctPos(w, h)

        paint.strokeWidth = width.getAbsolute(w, h)

        val rect = calculateDrawableRect(w, h)
        path.reset()
        path.moveTo(rect.left, rect.top)
        path.lineTo(rect.right, rect.bottom)
        path.moveTo(rect.left, rect.bottom)
        path.lineTo(rect.right, rect.top)
    }

    override fun isSelected(position: PointF): Boolean {
        return selectable && calculateDrawableRect(
            screenSize.x, screenSize.y).contains(position.x, position.y
        )
    }

    private fun calculateDrawableRect(w: Int, h: Int) : RectF {
        val leftUpper = rectPoints.leftUpper.getAbsolute(w, h)
        val rightBottom = rectPoints.rightBottom.getAbsolute(w, h)
        val offset = PointF((rightBottom.x + paint.strokeWidth) * 0.5f, (rightBottom.y + paint.strokeWidth) * 0.5f)

        val correctedLeftUpper = leftUpper.apply {
            offset(absolutePos.x - offset.x, absolutePos.y - offset.y)
        }
        val correctedRightBottom = rightBottom.apply {
            offset(absolutePos.x - offset.x, absolutePos.y - offset.y)
        }

        return RectF(
            correctedLeftUpper.x,
            correctedLeftUpper.y,
            correctedRightBottom.x,
            correctedRightBottom.y
        )
    }
}