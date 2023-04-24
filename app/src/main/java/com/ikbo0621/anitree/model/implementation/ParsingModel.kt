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
            Log.d("Parser", ParserConstants.BASIC_URL + "anime/" + animeTitle)
            val doc: Document = Jsoup
                .connect(ParserConstants.BASIC_URL + "anime/" + animeTitle)
                .get()


            var approvedAnimeTitle = ""
            var description = "-1"
            var studio = "-1"
            var releaseDate = "-1"
            var imageURI = Uri.EMPTY

            // Title
            val titleElement: Element =
                doc.getElementsByAttributeValue("itemprop", "name")[0]
            approvedAnimeTitle = titleElement.text().toString()


            // Description
            try {
                val descElement: Element =
                    doc.getElementsByAttributeValue("class", "pure-1 md-3-5")[0]
                description = descElement.select("p")[0].text().toString()
            } catch (_: Exception) {
            }


            // Studio & Release date
            val sectionElement: Element =
                doc.getElementsByAttributeValue("class", "pure-g entryBar")[0]

            try {
                studio = sectionElement.select("a")[0].text().toString()
                releaseDate = sectionElement.select("span")[1].text().toString()
            } catch (_: Exception) {
            }


            // Image
            try {
                val imgElement: Element =
                    doc.getElementsByAttributeValue("class", "mainEntry")[0]
                imageURI = imgElement.html()
                    .substringAfter("src=\"")
                    .substringBefore("\">")
                    .toUri()
            } catch (_: Exception) {
            }

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
    override suspend fun guessAnime(
        animeTitle: String,
        result: (UiState<Anime>, ArrayList<String>) -> Unit
    ) {
        val titleForGuessSearch = animeTitle.fitToGuessRequest()
        val titleForExactSearch = animeTitle.fitToExactRequest()

        val animPair: Pair<Anime, ArrayList<String>> = withContext(Dispatchers.IO) {

            val guessList = arrayListOf<String>()

            Log.d("PARSER MODEL", titleForGuessSearch + " " + System.currentTimeMillis().toString())

            /**
             * Get html page
             * */
            val doc: Document = try {
                Jsoup
                    .connect(
                        ParserConstants.BASIC_URL
                                + "anime/all?sort=average&order=desc&name="
                                + titleForGuessSearch
                    )
                    .get()
            } catch (e: Exception){
                // Error 429: request limit
                Jsoup.parse("")
            }


            /**
             * Get necessary elements by classes
             * */
            val txtElement: Element
            try {
                txtElement =
                    doc.getElementsByAttributeValue("class", "cardDeck cardGrid")[0]
            } catch (e: Exception) {
                Log.d("PARSER", "NOT F")
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
                    return@withContext anim to arrayListOf<String>()
                } catch (e: java.lang.Exception) {
                    return@withContext Anime(title = "-1") to arrayListOf<String>()
                }
            }

            /**
             * Get info from elements in necessary type
             * */
            val approvedAnimeTitle: String = txtElement
                .getElementsByAttributeValue("class", "cardName")[0]
                .text()
                .toString().fitToExactRequest()

            for (i in 1..5) {
                try {
                    val titleForGuessList: String =
                        txtElement.getElementsByAttributeValue("class", "cardName")[i]
                            .text()
                            .toString()
                    guessList.add(titleForGuessList)
                } catch (_: java.lang.IndexOutOfBoundsException) {
                    // Number of guesses in site less that 5
                }
            }

            /**
             * Create Anime object
             * */
            var anim = Anime()
            getAnimeWithName(approvedAnimeTitle) {
                if (it is UiState.Success) {
                    anim = it.data
                }
            }

            return@withContext anim to guessList
        }

        if (animPair.first.title == "-1") {
            result.invoke(
                UiState.Failure("Anime not found"), arrayListOf()
            )
        } else {
            result.invoke(
                UiState.Success(animPair.first), animPair.second
            )
        }
    }
}