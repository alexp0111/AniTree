package com.ikbo0621.anitree.tree.positioning

import android.graphics.PointF
import kotlin.math.max

class RPosition(
    x: RValue = RValue(0f, RValue.Type.X),
    y: RValue = RValue(0f, RValue.Type.Y)
) : Cloneable {
    private var valuesX = ArrayList<RValue>()
    private var valuesY = ArrayList<RValue>()

    init {
        add(x, y)
    }

    constructor(x: ArrayList<RValue>, y: ArrayList<RValue>) : this(){
        set(x, y)
    }

    constructor(position: RPosition) : this(){
        set(position)
    }

    fun set(x: RValue, y: RValue) {
        valuesX.clear()
        valuesY.clear()
        add(x, y)
    }

    fun set(x: ArrayList<RValue>, y: ArrayList<RValue>) {
        valuesX = x.clone() as ArrayList<RValue>
        valuesY = y.clone() as ArrayList<RValue>
    }

    fun set(position: RPosition) {
        set(position.valuesX, position.valuesY)
    }

    fun add(x: RValue, y: RValue) {
        valuesX.add(x.clone())
        valuesY.add(y.clone())
    }

    fun add(offset: RPosition) {
        for (i in offset.valuesX)
            valuesX.add(i.clone())
        for (i in offset.valuesY)
            valuesY.add(i.clone())
    }

    fun add(x: ArrayList<RValue>, y: ArrayList<RValue>) {
        valuesX.addAll(x.clone() as ArrayList<RValue>)
        valuesY.addAll(y.clone() as ArrayList<RValue>)
    }

    fun getRelativeX() : ArrayList<RValue> {
        return valuesX
    }

    fun getRelativeY() : ArrayList<RValue> {
        return valuesY
    }

    fun getAbsolute(w: Int, h: Int) : PointF {
        val result = PointF(0f, 0f)
        for (i in 0 until max(valuesX.size, valuesY.size)) {
            result.offset(
                valuesX.getOrElse(i) { RValue() }.getAbsolute(w, h),
                valuesY.getOrElse(i) { RValue() }.getAbsolute(w, h)
            )
        }

        return result
    }

    override fun clone(): Any {
        val cloneX = ArrayList<RValue>()
        val cloneY = ArrayList<RValue>()
        for (i in 0 until max(valuesX.size, valuesY.size)) {
            cloneX.add(valuesX.getOrElse(i) { RValue() })
            cloneY.add(valuesY.getOrElse(i) { RValue() })
        }
        return RPosition(cloneX, cloneY)
    }
}