package com.ikbo0621.anitree.model.implementation

import android.net.Uri
import com.ikbo0621.anitree.model.repository.ParsingRepository
import com.ikbo0621.anitree.structure.Anime
import com.ikbo0621.anitree.util.UiState

class ParsingModel() : ParsingRepository {
    override fun getAnimeWithName(animeTitle: String, result: (UiState<Anime>) -> Unit) {
        val anim = Anime(
            animeTitle,
            "Namana",
            "Studio",
            "00.00.0000",
            Uri.EMPTY
        )
        result.invoke(
            UiState.Success(anim)
        )
    }
}