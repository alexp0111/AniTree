package com.ikbo0621.anitree.tree.elements

import android.graphics.*
import com.ikbo0621.anitree.tree.positioning.RPosition
import com.ikbo0621.anitree.tree.positioning.RValue
import kotlin.math.pow

open class Circle(
    override var relativePos: RPosition,
    radius: RValue,
    circleColor: Int = Color.BLACK,
    renderType: Paint.Style? = Paint.Style.FILL,
    private val width: RValue = RValue(0.05f)
    ) : TreeElement() {
    var radius = radius
        protected set
    override var paint: Paint = Paint().apply {
        isAntiAlias = true
        color = circleColor
        style = renderType
    }
    private val path: Path = Path()

    override fun draw(canvas: Canvas) {
        canvas.drawPath(path, paint)
    }

    override fun isSelected(position: PointF) : Boolean {
        return (position.x - absolutePos.x).pow(2) +
                (position.y - absolutePos.y).pow(2) <=
                radius.getAbsolute(screenSize.x, screenSize.y).pow(2) //radius.pow(2)
    }

    override fun correctPos(w: Int, h: Int) {
        super.correctPos(w, h)

        absolutePos = relativePos.getAbsolute(w, h)
        paint.strokeWidth = width.getAbsolute(w, h)
        modifyPath()
    }

    override fun setRPos(posX: Float, posY: Float) {
        super.setRPos(posX, posY)
        modifyPath()
    }

    private fun modifyPath() {
        path.reset()
        path.addCircle(absolutePos.x, absolutePos.y, radius.getAbsolute(screenSize.x, screenSize.y), Path.Direction.CCW)
    }
}