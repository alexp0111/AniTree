package com.ikbo0621.anitree.tree.elements

import android.graphics.*
import android.graphics.Paint.Style
import com.ikbo0621.anitree.tree.positioning.RPosition
import com.ikbo0621.anitree.tree.positioning.RValue

class Curve(
    relativePath: Array<Line.LinePoint>,
    private val width: RValue,
    curveColor: Int = Color.BLACK,
    cap: Paint.Cap = Paint.Cap.ROUND
) : TreeElement() {
    override var relativePos = RPosition(RValue(0f, RValue.Type.X), RValue(0f, RValue.Type.Y))
    override var paint: Paint = Paint().apply {
        isAntiAlias = true
        style = Style.STROKE
        color = curveColor
        strokeCap = cap
    }
    private var line: Line = Line(relativePath)

    override fun draw(canvas: Canvas) {
        canvas.drawPath(line.path, paint)
    }

    override fun isSelected(position: PointF) : Boolean {
        return false
    }

    override fun correctPos(w: Int, h: Int) {
        super.correctPos(w, h)

        paint.strokeWidth = width.getAbsolute(w, h)
        line.correctPos(absolutePos, screenSize)
    }

    override fun setRPos(posX: Float, posY: Float) {
        super.setRPos(posX, posY)

        line.correctPos(absolutePos, screenSize)
    }
}