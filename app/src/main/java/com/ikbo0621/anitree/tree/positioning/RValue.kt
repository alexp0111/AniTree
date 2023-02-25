package com.ikbo0621.anitree.tree.positioning

import kotlin.math.max
import kotlin.math.min

class RValue(
    private var value: Float = 0f,
    private val type: Type = Type.X
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
        return when (type) {
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
    }

    public override fun clone(): Any {
        return RValue(value, type)
    }

    enum class Type {
        X, Y, BigSide, SmallSide
    }
}