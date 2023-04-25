package com.ikbo0621.anitree.data

import com.ikbo0621.anitree.tree.positioning.RValue

abstract class RelativeValuesLayout {
    protected open val dimens = hashMapOf<String, RValue>()

    fun getValues() : HashMap<String, RValue> {
        return dimens
    }
}