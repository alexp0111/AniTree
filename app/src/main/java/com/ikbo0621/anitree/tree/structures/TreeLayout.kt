package com.ikbo0621.anitree.tree.structures

import com.ikbo0621.anitree.tree.positioning.RPosition
import com.ikbo0621.anitree.tree.positioning.RRect
import com.ikbo0621.anitree.tree.positioning.RValue

data class TreeLayout(val offset: RPosition) {
    // TreeBuilder
    val mainIconRadius = RValue(0.1f, RValue.Type.Y, RValue(0.18f, RValue.Type.X))
    val mainIconPos = RPosition(offset).apply {
        add(RValue(0.035f, RValue.Type.Y), RValue(0.05f, RValue.Type.Y))
        add(mainIconRadius, mainIconRadius)
    }

    val subIconRadius = RValue(0.08f, RValue.Type.Y, RValue(0.16f, RValue.Type.X))
    val subIconsPositions = ArrayList<RPosition>(3).apply{
        add(
            RPosition(offset).apply {
                add(RValue(1.0f, RValue.Type.X), RValue(0.6f, RValue.Type.Y))
                add(RValue(-0.10f, RValue.Type.Y), RValue(-0.12f, RValue.Type.Y))
            }
        )
        add(RPosition(last()).apply { add(RValue(), RValue(0.2f, RValue.Type.Y)) })
        add(RPosition(last()).apply { add(RValue(), RValue(0.2f, RValue.Type.Y)) })
    }

    // TreeViewer
    val mainStudioTextPosition = RPosition(
        RValue(-0.048F,  RValue.Type.Y), RValue(-0.04F,  RValue.Type.Y)
    )
    val mainStudioTextSize = RValue(0.3F, RValue.Type.Y)
    val mainFramePos = RPosition(mainIconPos).apply {
        add(RValue(-0.01f, RValue.Type.Y), RValue(0.01f, RValue.Type.Y))
    }
    val mainNameTextPosition = RPosition(mainFramePos).apply {
        add(-mainIconRadius, mainIconRadius)
    }

    val subStudioTextPositions = ArrayList<RPosition>(3).apply{
        add(RPosition(RValue(0.155f, RValue.Type.Y), RValue(0.518f, RValue.Type.Y)))
        add(RPosition(last()).apply { add(RValue(), RValue(0.2f, RValue.Type.Y)) })
        add(RPosition(last()).apply { add(RValue(), RValue(0.2f, RValue.Type.Y)) })
    }
    val subStudioTextSize = RValue(0.08F, RValue.Type.Y)
    val subFramePositions = ArrayList<RPosition>(3).apply{
        add(
            RPosition(subIconsPositions.first()).apply {
                add(RValue(-0.01f, RValue.Type.Y), RValue(+0.01f, RValue.Type.Y))
            }
        )
        add(RPosition(last()).apply { add(RValue(), RValue(0.2f, RValue.Type.Y)) })
        add(RPosition(last()).apply { add(RValue(), RValue(0.2f, RValue.Type.Y)) })
    }

    val subNameTextPositions = ArrayList<RPosition>(3).apply{
        add(RPosition(RValue(0.16f, RValue.Type.Y), RValue(0.547f, RValue.Type.Y)))
        add(RPosition(last()).apply { add(RValue(), RValue(0.2f, RValue.Type.Y)) })
        add(RPosition(last()).apply { add(RValue(), RValue(0.2f, RValue.Type.Y)) })
    }
    val subNameTextSize = RValue(0.04F, RValue.Type.Y)
    val lineWidth = RValue(0.003f, RValue.Type.Y)
    val subStudioTextStrings = arrayOf("YOUR", "FAVORITE", "ANIME")
    val subNameTextStrings = arrayOf("Help", "Others", "Choose")
    val backFieldRect = RRect(
        RPosition(RValue(0f, RValue.Type.X), RValue(0f, RValue.Type.Y)),
        RPosition(RValue(0.4f, RValue.Type.SmallSide), RValue(1f, RValue.Type.Y))
    )
    val authorIconRadius = RValue(
        subIconRadius.getRelative() * 0.5f,
        subIconRadius.getType(),
        RValue(
            subIconRadius.limit!!.getRelative() * 0.5f,
            subIconRadius.limit!!.getType()
        )
    )
    val authorIconPosition = RPosition().apply {
        set(
            subFramePositions[0].getRelativeX(),
            mainFramePos.getRelativeY()
        )
        add(subIconRadius, -mainIconRadius)
        add(-authorIconRadius, authorIconRadius)
    }

    val authorTextPosition = RPosition(authorIconPosition).apply {
        add(subIconRadius * -2f, authorIconRadius)
        add(authorIconRadius, RValue())
    }
    val authorButtonRect =  RRect(
        RPosition(authorTextPosition.getRelativeX(), ArrayList()),
        RPosition(arrayListOf(RValue(1f, RValue.Type.X)), mainIconPos.getRelativeY())
    )
    val likeButtonRect = RRect(
        RPosition(),
        RPosition(authorIconRadius * 2f, authorIconRadius * 2f)
    )
    val likeButtonPosition = RPosition().apply {
        set(
            mainFramePos.getRelativeX(),
            subFramePositions[2].getRelativeY()
        )
        add(-mainIconRadius, RValue())
    }
}