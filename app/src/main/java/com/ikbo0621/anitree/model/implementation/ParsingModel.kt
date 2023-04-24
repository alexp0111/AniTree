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
            try {
                /** Get html page */
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
                    // There is no description on site
                }


                // Studio & Release date
                val sectionElement: Element =
                    doc.getElementsByAttributeValue("class", "pure-g entryBar")[0]

                try {
                    studio = sectionElement.select("a")[0].text().toString()
                    releaseDate = sectionElement.select("span")[1].text().toString()
                } catch (_: Exception) {
                    // There is no studio or release date on site
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
                    // There is no image on site
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
            } catch (e: Exception){
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
    override suspend fun guessAnime(
        animeTitle: String,
        result: (UiState<Anime>, ArrayList<String>) -> Unit
    ) {
        val titleForGuessSearch = animeTitle.fitToGuessRequest()
        val titleForExactSearch = animeTitle.fitToExactRequest()

        val animPair: Pair<Anime, ArrayList<String>> = withContext(Dispatchers.IO) {

            val guessList = arrayListOf<String>()
            var approvedAnimeTitle = titleForExactSearch

            Log.d("PARSER MODEL", titleForGuessSearch + " " + System.currentTimeMillis().toString())
            Log.d(
                "PARSER MODEL", ParserConstants.BASIC_URL
                        + "anime/all?sort=average&order=desc&name="
                        + titleForGuessSearch
            )


            try {
                /** Get html page */
                val doc = Jsoup
                    .connect(
                        ParserConstants.BASIC_URL
                                + "anime/all?sort=average&order=desc&name="
                                + titleForGuessSearch
                    )
                    .get()


                /** Get necessary elements by classes */
                val txtElement =
                    doc.getElementsByAttributeValue("class", "cardDeck cardGrid")[0]


                /** Get info from elements in necessary type */
                approvedAnimeTitle = txtElement
                    .getElementsByAttributeValue("class", "cardName")[0]
                    .text()
                    .toString().fitToExactRequest()


                /** Get guess list of anime */
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
            } catch (_: java.lang.Exception) {
                // -> Error in url
                // -> Anime name is exactly matches
            }

            /** Create Anime object */
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