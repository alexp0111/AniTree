package com.ikbo0621.anitree.tree.positioning

import android.graphics.PointF
import android.graphics.RectF

data class RRect(val leftUpper: RPosition, val rightBottom: RPosition) : Cloneable{
    fun getAbsoluteRect(w: Int, h: Int, offset: PointF) : RectF {
        val absoluteLeftUpper = leftUpper.getAbsolute(w, h)
        val absoluteRightBottom = rightBottom.getAbsolute(w, h)
        return RectF(
            absoluteLeftUpper.x, absoluteLeftUpper.y,
            absoluteRightBottom.x, absoluteRightBottom.y
        ).apply {
            offset(offset.x, offset.y)
        }
    }

    fun add(offset: RPosition) {
        leftUpper.add(offset)
        rightBottom.add(offset)
    }

    public override fun clone(): Any {
        return RRect(RPosition(leftUpper), RPosition(rightBottom))
    }
}