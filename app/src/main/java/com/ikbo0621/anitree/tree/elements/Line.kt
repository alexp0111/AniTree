package com.ikbo0621.anitree.tree.elements

import android.graphics.Path
import android.graphics.Point
import android.graphics.PointF
import com.ikbo0621.anitree.tree.positioning.RPosition

class Line(private val points: Array<LinePoint>) {
    val path = Path()

    fun correctPos(absolutePos: PointF, screenSize: Point) {
        path.reset()
        for (i in points.indices) {
            val point = points[i].pos.getAbsolute(screenSize.x, screenSize.y)

            if (i == 0) {
                path.moveTo(
                    absolutePos.x + point.x,
                    absolutePos.y + point.y
                )
                continue
            }

            if (points[i].isSmoothAngle) {
                if (i >= points.size - 1)
                    continue

                val nextPoint = points[i + 1].pos.getAbsolute(screenSize.x, screenSize.y)
                path.quadTo(
                    absolutePos.x + point.x,
                    absolutePos.y + point.y,
                    absolutePos.x + nextPoint.x,
                    absolutePos.y + nextPoint.y
                )
            } else {
                path.lineTo(
                    absolutePos.x + point.x,
                    absolutePos.y + point.y
                )
            }
        }
    }

    class LinePoint(val pos: RPosition, val isSmoothAngle: Boolean)
}