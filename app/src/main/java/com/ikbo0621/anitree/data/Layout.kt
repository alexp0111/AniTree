package com.ikbo0621.anitree.data

import com.ikbo0621.anitree.tree.positioning.RValue

class Layout : RelativeValuesLayout() {
    override val dimens = hashMapOf(
        "text_size" to RValue(0.2f, RValue.Type.Y),
        "small_text_size" to RValue(0.1f, RValue.Type.Y),
        "line_width" to RValue(0.01f, RValue.Type.Y)
    )
}