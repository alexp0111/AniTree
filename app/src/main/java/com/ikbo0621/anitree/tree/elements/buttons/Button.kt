package com.ikbo0621.anitree.tree.elements.buttons

import android.graphics.Canvas
import com.ikbo0621.anitree.tree.elements.Rectangle
import com.ikbo0621.anitree.tree.positioning.RPosition
import com.ikbo0621.anitree.tree.positioning.RRect

open class Button(
    relativePos: RPosition,
    rectPoints: RRect
) : Rectangle(relativePos, rectPoints) {
    override fun draw(canvas: Canvas) {
        return
    }
}