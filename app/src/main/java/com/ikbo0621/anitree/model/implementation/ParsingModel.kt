package com.ikbo0621.anitree.model.implementation

import android.net.Uri
import androidx.core.net.toUri
import com.ikbo0621.anitree.model.repository.ParsingRepository
import com.ikbo0621.anitree.structure.Anime
import com.ikbo0621.anitree.util.ParserConstants
import com.ikbo0621.anitree.util.UiState
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.jsoup.Jsoup
import org.jsoup.nodes.Document
import org.jsoup.nodes.Element

/**
 * Model that provides parsing logic
 * */
class ParsingModel() : ParsingRepository {

    /**
     * Get anime directly with argument
     * */
    override suspend fun getAnimeWithName(animeTitle: String, result: (UiState<Anime>) -> Unit) {

        val anim: Anime = withContext(Dispatchers.IO) {

            /**
             * Get html page
             * */
            try {
                val doc: Document = Jsoup
                    .connect(ParserConstants.BASIC_URL + "anime/" + animeTitle)
                    .get()

                /**
                 * Get necessary elements by classes
                 * */
                val titleElement: Element = doc.getElementsByAttributeValue("itemprop", "name")[0]
                val descElement: Element =
                    doc.getElementsByAttributeValue("class", "pure-1 md-3-5")[0]
                val sectionElement: Element =
                    doc.getElementsByAttributeValue("class", "pure-g entryBar")[0]
                val imgElement: Element = doc.getElementsByAttributeValue("class", "mainEntry")[0]

                /**
                 * Get info from elements in necessary type
                 * */
                val approvedAnimeTitle: String = titleElement.text().toString()
                val description: String = descElement.select("p")[0].text().toString()
                val studio: String = sectionElement.select("a")[0].text().toString()
                val releaseDate: String = sectionElement.select("a")[1].text().toString()
                val imageURI: Uri = imgElement.html()
                    .substringAfter("src=\"")
                    .substringBefore("\">")
                    .toUri()

                /**
                 * Create Anime object
                 * */
                val anim = Anime(
                    approvedAnimeTitle,
                    description,
                    studio,
                    releaseDate,
                    imageURI
                )

                return@withContext anim
            } catch (e: java.lang.Exception) {
                return@withContext Anime()
            }
        }

        if (anim.title == "-1") {
            result.invoke(
                UiState.Failure("Anime not found")
            )
        } else {
            result.invoke(
                UiState.Success(anim)
            )
        }
    }

    /**
     * Guess anime with argument
     * */
    override suspend fun guessAnime(animeTitle: String, result: (UiState<Anime>) -> Unit) {

        val anim: Anime = withContext(Dispatchers.IO) {

            /**
             * Get html page
             * */
            try {
                val doc: Document = Jsoup
                    .connect(
                        ParserConstants.BASIC_URL
                                + "anime/all?sort=status_1&order=desc&name="
                                + animeTitle
                    )
                    .get()

                /**
                 * Get necessary elements by classes
                 * */
                val txtElement: Element =
                    doc.getElementsByAttributeValue("class", "cardDeck cardGrid")[0]
                val imgElement: Element = doc.getElementsByAttributeValue("class", "card  ")[0]

                /**
                 * Get info from elements in necessary type
                 * */
                val approvedAnimeTitle: String = txtElement
                    .getElementsByAttributeValue("class", "cardName")[0]
                    .text()
                    .toString()
                val imageURI: Uri = imgElement.html()
                    .substringAfter("src=\"")
                    .substringBefore("\">")
                    .toUri()

                /**
                 * Create Anime object
                 * */
                val anim = Anime(
                    approvedAnimeTitle,
                    imageURI = imageURI
                )

                return@withContext anim
            } catch (e: java.lang.Exception) {
                return@withContext Anime()
            }
        }

        if (anim.title == "-1") {
            result.invoke(
                UiState.Failure("Anime not found")
            )
        } else {
            result.invoke(
                UiState.Success(anim)
            )
        }
    }
}