package com.ikbo0621.anitree.tree.elements

import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.PorterDuff
import android.graphics.PorterDuffXfermode
import android.graphics.Rect
import android.graphics.RectF
import com.ikbo0621.anitree.tree.positioning.RPosition
import com.ikbo0621.anitree.tree.positioning.RValue

class Icon(
    relativePos: RPosition,
    radius: RValue,
    bitmap: Bitmap
) : Circle(relativePos, radius) {
    private val cropBitmap = cropBitmapToCircle(bitmap)

    override fun draw(canvas: Canvas) {
        val absoluteRadius = radius.getAbsolute(screenSize.x, screenSize.y)

        val bitmapArea = RectF(
            absolutePos.x - absoluteRadius,
            absolutePos.y - absoluteRadius,
            absolutePos.x + absoluteRadius,
            absolutePos.y + absoluteRadius)
        canvas.drawBitmap(cropBitmap, null, bitmapArea, paint)
    }

    private fun cropBitmapToCircle(bitmap: Bitmap) : Bitmap {
        val dest = Bitmap.createBitmap(bitmap.width, bitmap.height, Bitmap.Config.ARGB_8888)
        val canvas = Canvas(dest)
        val paint = Paint()
        canvas.drawARGB(0, 0, 0, 0) // Fill with transparency
        canvas.drawCircle(
            bitmap.width * 0.5f,
            bitmap.height * 0.5f,
            bitmap.width.toFloat() * 0.5f,
            paint
        ) // Draw the area where the bitmap will be displayed

        paint.xfermode = PorterDuffXfermode(PorterDuff.Mode.SRC_IN)
        canvas.drawBitmap(bitmap, null, Rect(0, 0, bitmap.width, bitmap.height), paint)

        return dest
    }

    override fun correctPos(w: Int, h: Int) {
        super.correctPos(w, h)
        absolutePos = relativePos.getAbsolute(w, h)
    }
}