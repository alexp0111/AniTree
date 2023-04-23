package com.ikbo0621.anitree.model.implementation

import android.net.Uri
import android.util.Log
import androidx.core.net.toUri
import com.ikbo0621.anitree.model.repository.ParsingRepository
import com.ikbo0621.anitree.structure.Anime
import com.ikbo0621.anitree.util.ParserConstants
import com.ikbo0621.anitree.util.UiState
import com.ikbo0621.anitree.util.fitToExactRequest
import com.ikbo0621.anitree.util.fitToGuessRequest
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
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
                Log.d("Parser", ParserConstants.BASIC_URL + "anime/" + animeTitle)
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
                val releaseDate: String = sectionElement.select("span")[1].text().toString()
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

                Log.d("Parser", anim.toString())

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
    override suspend fun guessAnime(animeTitle: String, result: (UiState<Anime>, ArrayList<String>) -> Unit) {
        val titleForGuessSearch = animeTitle.fitToGuessRequest()
        val titleForExactSearch = animeTitle.fitToExactRequest()

        val anim: Anime = withContext(Dispatchers.IO) {
            Log.d("PARSER MODEL", titleForGuessSearch + " " + System.currentTimeMillis().toString())

            /**
             * Get html page
             * */
            try {
                val doc: Document = Jsoup
                    .connect(
                        ParserConstants.BASIC_URL
                                + "anime/all?sort=average&order=desc&name="
                                + titleForGuessSearch
                    )
                    .get()

                /**
                 * Get necessary elements by classes
                 * */
                val txtElement: Element =
                    doc.getElementsByAttributeValue("class", "cardDeck cardGrid")[0]

                /**
                 * Get info from elements in necessary type
                 * */
                val approvedAnimeTitle: String = txtElement
                    .getElementsByAttributeValue("class", "cardName")[0]
                    .text()
                    .toString().fitToExactRequest()

                /**
                 * Create Anime object
                 * */
                var anim = Anime()
                getAnimeWithName(approvedAnimeTitle) {
                    if (it is UiState.Success) {
                        anim = it.data
                    }
                }

                return@withContext anim
            } catch (e: java.lang.Exception) {

                /**
                 * If title not found -> try to exact search
                 * Caused by: guessing exact title do not return result
                 * Just a problem of parsed site
                 * */

                try {
                    var anim = Anime()
                    getAnimeWithName(titleForExactSearch) {
                        if (it is UiState.Success) {
                            anim = it.data
                        }
                    }
                    return@withContext anim
                } catch (e: java.lang.Exception){
                    return@withContext Anime()
                }
            }
        }

        if (anim.title == "-1") {
            result.invoke(
                UiState.Failure("Anime not found"), arrayListOf()
            )
        } else {
            result.invoke(
                UiState.Success(anim), arrayListOf("Mob psycho 100")
            )
        }
    }
}