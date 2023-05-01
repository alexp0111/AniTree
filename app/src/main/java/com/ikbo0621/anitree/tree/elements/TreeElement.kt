package com.ikbo0621.anitree.tree.elements

import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.Point
import android.graphics.PointF
import com.ikbo0621.anitree.tree.positioning.RPosition
import com.ikbo0621.anitree.tree.positioning.RValue

abstract class TreeElement {
    protected abstract var relativePos: RPosition // Used to position elements during initialization
    protected var absolutePos = PointF(0.0F, 0.0F)
    var screenSize = Point(0, 0)
        protected set
    protected abstract var paint: Paint
    var alpha
        get() = paint.alpha
        set(value) {
            paint.alpha = value
        }
    var index: IntArray? = null
    var selectable = true

    abstract fun draw(canvas: Canvas)
    abstract fun isSelected(position: PointF) : Boolean

    open fun correctPos(w: Int, h: Int) {
        screenSize = Point(w, h)
    }

    fun getAbsPos() : PointF {
        return absolutePos
    }

    open fun setAbsPos(posX: Float, posY: Float) {
        absolutePos = PointF(posX, posY)
    }

    fun getRPos() : RPosition {
        return relativePos
    }

    open fun setRPos(posX: Float, posY: Float) {
        relativePos.set(RValue(posX, RValue.Type.X), RValue(posY, RValue.Type.X))
        absolutePos = relativePos.getAbsolute(screenSize.x, screenSize.y)
    }

    fun setRPos(relativePos: PointF) {
        setRPos(relativePos.x, relativePos.y)
    }

    override fun equals(other: Any?): Boolean {
        val comparedElement = other as? TreeElement ?: return false
        return absolutePos == comparedElement.absolutePos &&
                index.contentEquals(comparedElement.index)
    }

    override fun hashCode(): Int {
        var result = relativePos.hashCode()
        result = 31 * result + absolutePos.hashCode()
        result = 31 * result + screenSize.hashCode()
        result = 31 * result + paint.hashCode()
        result = 31 * result + (index?.contentHashCode() ?: 0)
        return result
    }
}