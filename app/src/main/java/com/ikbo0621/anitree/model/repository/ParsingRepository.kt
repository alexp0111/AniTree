package com.ikbo0621.anitree.model.repository

import android.content.Context
import android.graphics.Bitmap
import com.ikbo0621.anitree.structure.Anime
import com.ikbo0621.anitree.structure.Tree
import com.ikbo0621.anitree.util.UiState

/**
 * Repository, that holds operations connected to parsing data from internet
 * */
interface ParsingRepository {
    suspend fun getAnimeWithName(animeTitle: String, result: (UiState<Anime>) -> Unit)
    suspend fun guessAnime(animeTitle: String, result: (UiState<Anime>, ArrayList<String>) -> Unit)
    suspend fun getBitmapListForTree(tree: Tree, context: Context, result: (UiState<ArrayList<Bitmap>>) -> Unit)
}