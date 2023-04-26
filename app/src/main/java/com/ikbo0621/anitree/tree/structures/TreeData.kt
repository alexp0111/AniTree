package com.ikbo0621.anitree.tree.structures

import android.graphics.Bitmap
import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class TreeData(
    val name: String,
    val studio: String,
    val bitmap: Bitmap,
    var index: IntArray,
    var tree: ArrayList<TreeData>? = null
) : Parcelable {
    private fun createTree(name: String, studio: String, bitmap: Bitmap, index: IntArray) {
        tree = ArrayList(3)
        tree!!.add(TreeData(name, studio, bitmap, index))
    }

    fun addSubElement(name: String, studio: String, bitmap: Bitmap, index: IntArray) {
        val parent = getSubElementByIndex(index.copyOfRange(0, index.lastIndex)) ?: return
        val parentTree = parent.tree
        
        if (parentTree == null) {
            parent.createTree(name, studio, bitmap, index)
        } else if (parentTree.size < 3) {
            parentTree.add(TreeData(name, studio, bitmap, index))
        }
    }

    private fun getSubElementByIndex(index: IntArray) : TreeData? {
        var result = this
        for (i in index) {
            result = result.tree?.getOrNull(i) ?: return null
        }

        return result
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as TreeData

        if (name != other.name) return false
        if (studio != other.studio) return false
        if (bitmap != other.bitmap) return false
        if (!index.contentEquals(other.index)) return false
        if (tree != other.tree) return false

        return true
    }

    override fun hashCode(): Int {
        var result = name.hashCode()
        result = 31 * result + studio.hashCode()
        result = 31 * result + bitmap.hashCode()
        result = 31 * result + index.contentHashCode()
        result = 31 * result + (tree?.hashCode() ?: 0)
        return result
    }
}