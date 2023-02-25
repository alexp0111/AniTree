package com.ikbo0621.anitree.tree.elements

import android.graphics.*
import android.graphics.Paint.Style
import com.ikbo0621.anitree.tree.positioning.RPosition
import com.ikbo0621.anitree.tree.positioning.RValue

class Curve(
    relativePath: Array<Line.LinePoint>,
    curveWidth: Float = 15f,
    curveColor: Int = Color.BLACK,
    cap: Paint.Cap = Paint.Cap.ROUND
) : TreeElement() {
    //override var relativePos = PointF(0.0f, 0.0f)
    override var relativePos = RPosition(RValue(0f, RValue.Type.X), RValue(0f, RValue.Type.Y))
    override var paint: Paint = Paint().apply {
        isAntiAlias = true
        style = Style.STROKE
        strokeWidth = curveWidth
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

    override fun correctPos(w: Int, h: Int) { // Пересчет всей линии?
        super.correctPos(w, h)

        //screenSize = Point(w.toFloat(), h.toFloat())
        line.correctPos(absolutePos, screenSize)
    }

    override fun setRPos(posX: Float, posY: Float) { // Пересчет всей линии?
        super.setRPos(posX, posY)

        line.correctPos(absolutePos, screenSize)
    }
}