package com.ikbo0621.anitree.tree.elements

import android.graphics.*
import com.ikbo0621.anitree.tree.positioning.RPosition
import com.ikbo0621.anitree.tree.positioning.RValue

class Text(
    override var relativePos: RPosition,
    var text: String,
    textColor: Int = Color.BLACK,
    font: Typeface? = null,
    private val size: RValue = RValue(0.1F),
    private val rotationAngle: Float? = null,
    renderType: Paint.Style? = Paint.Style.FILL,
) : TreeElement() {
    var textColor = textColor
        set(value) {
            field = value
            paint.color = field
        }
    override var paint = Paint().apply {
        isAntiAlias = true
        color = textColor
        style = renderType
        if (font != null)
            typeface = font
    }

    override fun draw(canvas: Canvas) {
        if (rotationAngle == null) {
            canvas.drawText(text, absolutePos.x, absolutePos.y, paint)
        } else {
            canvas.save()
            canvas.translate(absolutePos.x, absolutePos.y)
            canvas.rotate(rotationAngle)
            canvas.drawText(text, 0f, 0f, paint)
            canvas.restore()
        }
    }

    override fun isSelected(position: PointF): Boolean {
        return false
    }

    override fun correctPos(w: Int, h: Int) {
        super.correctPos(w, h)

        absolutePos = relativePos.getAbsolute(w, h)
        paint.textSize = size.getAbsolute(w, h)
        val px = size.getAbsolute(w, h)
        // px = dp * (dpi / 160)
        val dp = px / (420 / 160)

    }
}