package com.ikbo0621.anitree.model.repository

import com.ikbo0621.anitree.structure.Anime
import com.ikbo0621.anitree.util.UiState

interface ParsingRepository {
    fun getAnimeWithName(animeTitle: String, result: (UiState<Anime>) -> Unit)
}