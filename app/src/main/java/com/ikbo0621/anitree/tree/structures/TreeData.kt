package com.ikbo0621.anitree.tree.structures

import android.graphics.Bitmap

data class TreeData(
    val name: String,
    val bitmap: Bitmap,
    var index: IntArray,
    var tree: ArrayList<TreeData>? = null
) {
    private fun createTree(name: String, bitmap: Bitmap, index: IntArray) {
        tree = ArrayList(3)
        tree!!.add(TreeData(name, bitmap, index))
    }

    fun addSubElement(name: String, bitmap: Bitmap, index: IntArray) {
        if (tree == null) {
            createTree(name, bitmap, index)
        } else if (tree!!.size < 3) {
            tree!!.add(TreeData(name, bitmap, index))
        }
    }
}