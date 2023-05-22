package com.ikbo0621.anitree.tree.elements

import android.graphics.*
import com.ikbo0621.anitree.tree.positioning.RPosition
import com.ikbo0621.anitree.tree.positioning.RValue

class Icon(
    relativePos: RPosition,
    radius: RValue,
    bitmap: Bitmap
) : Circle(relativePos, radius) {
    private var resultBitmap = bitmap

    override fun draw(canvas: Canvas) {
        val absoluteRadius = radius.getAbsolute(screenSize.x, screenSize.y)

        val bitmapArea = RectF(
            absolutePos.x - absoluteRadius,
            absolutePos.y - absoluteRadius,
            absolutePos.x + absoluteRadius,
            absolutePos.y + absoluteRadius)
        canvas.drawBitmap(resultBitmap, null, bitmapArea, paint)
    }

    override fun correctPos(w: Int, h: Int) {
        super.correctPos(w, h)
        absolutePos = relativePos.getAbsolute(w, h)
        resultBitmap = cropBitmapToCircle(resultBitmap, radius.getAbsolute(w, h))
    }

    private fun cropBitmapToCircle(bitmap: Bitmap, absoluteRadius: Float) : Bitmap {
        val scaledBitmap = scaleBitmapToSquare(bitmap, absoluteRadius)
        val dest = Bitmap.createBitmap(scaledBitmap.width, scaledBitmap.height, Bitmap.Config.ARGB_8888)

        val canvas = Canvas(dest)
        val paint = Paint().apply {
            isAntiAlias = true
        }
        canvas.drawARGB(0, 0, 0, 0) // Fill with transparency
        canvas.drawCircle(
            scaledBitmap.width * 0.5f,
            scaledBitmap.height * 0.5f,
            scaledBitmap.width.toFloat() * 0.5f,
            paint
        ) // Draw the area where the bitmap will be displayed

        paint.xfermode = PorterDuffXfermode(PorterDuff.Mode.SRC_IN)
        canvas.drawBitmap(scaledBitmap, null, Rect(0, 0, scaledBitmap.width, scaledBitmap.height), paint)

        return dest
    }

    // The size of the bitmap must be equal to the size of the icon on the screen
    private fun scaleBitmapToSquare(bitmap: Bitmap, absoluteRadius: Float) : Bitmap {
        val matrix = Matrix()
        val factor = (2f * absoluteRadius) / bitmap.width
        matrix.postScale(factor, factor)

        return Bitmap.createBitmap(
            bitmap,
            0,
            (bitmap.height - bitmap.width) / 2,
            bitmap.width,
            bitmap.width,
            matrix,
            true
        )
    }
}