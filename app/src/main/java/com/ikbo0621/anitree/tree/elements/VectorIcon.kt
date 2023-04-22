package com.ikbo0621.anitree.tree.elements

import android.graphics.*
import android.graphics.drawable.Drawable
import androidx.core.graphics.BlendModeColorFilterCompat
import androidx.core.graphics.BlendModeCompat
import com.ikbo0621.anitree.tree.positioning.RPosition
import com.ikbo0621.anitree.tree.positioning.RRect

class VectorIcon(
    relativePos: RPosition,
    rectPoints: RRect,
    drawable: Drawable,
    private val color: Int? = null
) : Rectangle(relativePos, rectPoints) {
    private var bitmap = getVectorBitmap(drawable)
    override var paint: Paint = Paint().apply {
        isAntiAlias = true
    }

    override fun draw(canvas: Canvas) {
        if (color == null)
            return
        //canvas.drawRect(rectPoints.getAbsoluteRect(screenSize.x, screenSize.y, absolutePos), paint)
        canvas.drawBitmap(
            bitmap,
            null,
            rectPoints.getAbsoluteRect(screenSize.x, screenSize.y, absolutePos),
            paint
        )
    }

    override fun correctPos(w: Int, h: Int) {
        super.correctPos(w, h)

        absolutePos = relativePos.getAbsolute(w, h)
    }

    private fun getVectorBitmap(drawable: Drawable): Bitmap {
        val result = Bitmap.createBitmap(
            drawable.intrinsicWidth, // default width and height
            drawable.intrinsicHeight,
            Bitmap.Config.ARGB_8888
        )

        val canvas = Canvas(result)
        drawable.setBounds(0, 0, canvas.width, canvas.height)
        //drawable.colorFilter = BlendModeColorFilter(color, BlendMode.SRC)
        drawable.colorFilter = BlendModeColorFilterCompat.createBlendModeColorFilterCompat(color!!, BlendModeCompat.SRC_ATOP)
        drawable.draw(canvas)

        return result
    }
}