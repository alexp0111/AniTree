package com.ikbo0621.anitree.tree.elements.buttons

import android.graphics.Color
import android.graphics.Paint
import com.ikbo0621.anitree.tree.elements.Circle
import com.ikbo0621.anitree.tree.positioning.RPosition
import com.ikbo0621.anitree.tree.positioning.RValue

class MainSchemeButton(
    relativePos: RPosition,
    radius: RValue,
    circleColor: Int = Color.BLACK,
    width: RValue = RValue(0.05f)
) : Circle(relativePos, radius, circleColor, Paint.Style.FILL, width)