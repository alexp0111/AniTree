package com.ikbo0621.anitree.tree.elements

import android.graphics.*
import com.ikbo0621.anitree.tree.positioning.RPosition
import com.ikbo0621.anitree.tree.positioning.RValue

class Text(
    override var relativePos: RPosition,
    private var text: String,
    textColor: Int = Color.BLACK,
    font: Typeface? = null,
    renderType: Paint.Style? = Paint.Style.FILL,
    private val size: RValue = RValue(0.1F)
    ) : TreeElement() {
    override var paint = Paint().apply {
        isAntiAlias = true
        color = textColor
        style = renderType
        if (font != null)
            typeface = font
    }

    override fun draw(canvas: Canvas) {
        canvas.drawText(text, absolutePos.x, absolutePos.y, paint)
    }

    override fun isSelected(position: PointF): Boolean {
        return false
    }

    override fun correctPos(w: Int, h: Int) {
        super.correctPos(w, h)

        absolutePos = relativePos.getAbsolute(w, h)
        paint.textSize = size.getAbsolute(w, h)
    }
}