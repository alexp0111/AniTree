package com.ikbo0621.anitree.structure

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.util.Log
import com.ikbo0621.anitree.tree.structures.TreeData
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.withContext
import java.io.IOException
import java.io.InputStream
import java.net.URL

private const val TAG = "TREE_CONVERTER"

object TreeConverter {

    /**
     * Responsible for converting the structure that we get from
     * the database into the structure displayed by the tree and vice versa
     */
    suspend fun convert(tree: Tree): TreeData? {

        // converting data
        val titles = tree.children
        val studios = tree.studios
        Log.d(TAG, System.currentTimeMillis().toString())
        val bitmaps = decodeBitmapFromURL(tree.urls)
        Log.d(TAG, System.currentTimeMillis().toString())

        // structure data
        if (titles[0] == null)
            return null

        val mainBitmap = bitmaps[0] ?: return null
        val result = TreeData(titles[0]!!, studios[0] ?: "", mainBitmap, IntArray(0))

        for (i in 0 until 3) {
            val index = i + 1
            val name = titles[index] ?: continue
            val bitmap = bitmaps[index] ?: continue
            val studio = studios[index] ?: ""

            result.addSubElement(name, studio, bitmap, intArrayOf(i))

            for (j in 0 until 3) {
                val subIndex = (1 + i) * 3 + (1 + j)
                val subName = titles[subIndex] ?: continue
                val subBitmap = bitmaps[subIndex] ?: continue
                val subStudio = studios[subIndex] ?: ""

                result.addSubElement(subName, subStudio, subBitmap, intArrayOf(i, j))
            }
        }

        return result
    }

    /**
     * Convert list of anime to Tree
     * */
    fun convert(id: String = "", list: List<Anime?>): Tree {
        val children: MutableList<String?> = arrayListOf()
        val studios: MutableList<String?> = arrayListOf()
        val urls: MutableList<String?> = arrayListOf()

        list.forEachIndexed { ind, anime ->
            if (anime == null) {
                children.add(ind, null)
                studios.add(ind, null)
                urls.add(ind, null)
            } else {
                children.add(ind, anime.title)
                studios.add(ind, anime.studio)
                urls.add(ind, anime.imageURI.toString())
            }
        }
        return Tree(
            id = id,
            children = children,
            studios = studios,
            urls = urls
        )
    }

    /**
     * Decode one url to the bitmap
     * */
    suspend fun decodeBitmapFromURL(url: String): Bitmap? {
        val bitmap = withContext(Dispatchers.IO) {
            var bitmap: Bitmap? = null

            val inputStream: InputStream
            try {
                inputStream = URL(url).openStream()
                bitmap = BitmapFactory.decodeStream(inputStream)
            } catch (e: IOException) {
                e.printStackTrace()
            }

            return@withContext bitmap
        }

        return bitmap
    }

    /**
     * Decode list of urls into list of bitmaps
     * */
    private suspend fun decodeBitmapFromURL(urls: List<String?>): ArrayList<Bitmap?> {
        val list = withContext(Dispatchers.IO) {
            val list = arrayListOf<Bitmap?>()

            urls.forEach {
                if (it != null) {
                    delay((400..700).random().toLong())
                    val inputStream: InputStream
                    try {
                        inputStream = URL(it).openStream()
                        list.add(BitmapFactory.decodeStream(inputStream))
                    } catch (e: IOException) {
                        e.printStackTrace()
                    }
                } else {
                    list.add(null)
                }
                Log.d(TAG, System.currentTimeMillis().toString())
            }

            return@withContext list
        }
        return list
    }
}