package com.ikbo0621.anitree.tree.positioning

import android.graphics.Point
import kotlin.math.abs
import kotlin.math.max
import kotlin.math.min

class RValue(
    private var value: Float = 0f,
    private val type: Type = Type.X,
    var limit: RValue? = null
) : Cloneable {
    fun set(value: Float) {
        this.value = value
    }

    fun getRelative() : Float {
        return value
    }

    fun getType() : Type {
        return type
    }

    fun getAbsolute(w: Int, h: Int) : Float {
        val result = when (type) {
            Type.X -> {
                w * value
            }
            Type.Y -> {
                h * value
            }
            Type.BigSide -> {
                val bigSide = max(w, h)
                bigSide * value
            }
            Type.SmallSide -> {
                val smallSide = min(w, h)
                smallSide * value
            }
        }
        val absoluteLimit = limit?.getAbsolute(w, h) ?: return result

        return if (abs(result) > abs(absoluteLimit))
            absoluteLimit
        else
            result
    }

    fun getAbsolute(screenSize: Point) : Float {
        return getAbsolute(screenSize.x, screenSize.y)
    }

    operator fun unaryMinus(): RValue {
        val cloneLimit = if (limit == null)
            null
        else if (value > 0)
            -limit!!
        else
            limit

        return RValue(-value, type, cloneLimit)
    }
    public override fun clone(): RValue {
        return RValue(value, type, limit)
    }

    enum class Type {
        X, Y, BigSide, SmallSide
    }
}