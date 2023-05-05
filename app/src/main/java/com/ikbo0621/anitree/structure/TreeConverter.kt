package com.ikbo0621.anitree.structure

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import com.ikbo0621.anitree.tree.structures.TreeData
import java.io.IOException
import java.net.URL


object TreeConverter {
    /**
     * Responsible for converting the structure that we get from
     * the database into the structure displayed by the tree and vice versa
     */
    fun convert(tree: Tree) : TreeData? {

        if (tree.children[0] == null)
            return null

        val mainBitmap = decodeBitmapFromURL(tree.urls[0]!!) ?: return null
        val result = TreeData(tree.children[0]!!, "---", mainBitmap, IntArray(0))

        for (i in 0 until 3) {
            val index = i + 1
            val name = tree.children[index] ?: continue
            val bitmap = decodeBitmapFromURL(tree.urls[index]) ?: continue
            result.addSubElement(name, "---", bitmap, intArrayOf(i))

            for (j in 0 until 3) {
                val subIndex = (1 + i) * 3 + (1 + j)
                val subName = tree.children[subIndex] ?: continue
                val subBitmap = decodeBitmapFromURL(tree.urls[subIndex]) ?: continue
                result.addSubElement(subName, "---", subBitmap, intArrayOf(i, j))
            }
        }

        return result
    }

    fun convert(treeData: TreeData) : Tree? {
        TODO()
    }

    private fun decodeBitmapFromURL(url: String?) : Bitmap? {
        if (url == null)
            return null

        return try {
            val image = BitmapFactory.decodeStream(
                URL(url).openConnection().getInputStream()
            )
            image
        } catch (e: IOException) {
            e.printStackTrace()
            null
        }
    }
}