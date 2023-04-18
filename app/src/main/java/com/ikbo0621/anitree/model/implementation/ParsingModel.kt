package com.ikbo0621.anitree.model.implementation

import android.net.Uri
import android.util.Log
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
     * TODO: Check for necessity
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
                Log.d("Parser", "1")
                val descElement: Element =
                    doc.getElementsByAttributeValue("class", "pure-1 md-3-5")[0]
                Log.d("Parser", "2")
                val sectionElement: Element =
                    doc.getElementsByAttributeValue("class", "pure-g entryBar")[0]
                Log.d("Parser", "3")
                val imgElement: Element = doc.getElementsByAttributeValue("class", "mainEntry")[0]
                Log.d("Parser", "4")

                /**
                 * Get info from elements in necessary type
                 * */
                val approvedAnimeTitle: String = titleElement.text().toString()
                Log.d("Parser", "5")
                val description: String = descElement.select("p")[0].text().toString()
                Log.d("Parser", "6")
                val studio: String = sectionElement.select("a")[0].text().toString()
                Log.d("Parser", "7")
                val releaseDate: String = sectionElement.select("span")[1].text().toString()
                Log.d("Parser", "8")
                val imageURI: Uri = imgElement.html()
                    .substringAfter("src=\"")
                    .substringBefore("\">")
                    .toUri()
                Log.d("Parser", "9")

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
    override suspend fun guessAnime(animeTitle: String, result: (UiState<Anime>) -> Unit) {

        //TODO: Rework logic: get element (just name) -> go with url to anime page & get full info

        val anim: Anime = withContext(Dispatchers.IO) {

            /**
             * Get html page
             * */
            try {
                val doc: Document = Jsoup
                    .connect(
                        ParserConstants.BASIC_URL
                                + "anime/all?sort=average&order=desc&name="
                                + animeTitle
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
                    .toString()
                    .replace(" - ", "-")
                    .replace(" ", "-")
                    .replace("\'", "")
                    .replace(":", "")
                    .lowercase()

                /**
                 * Create Anime object
                 * */
                val anim = getAnimeWithName2(approvedAnimeTitle)

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
     * Internal foo for getting anime with exact title
     **/
    private suspend fun getAnimeWithName2(animeTitle: String): Anime {
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
                Log.d("Parser", "1")
                val descElement: Element =
                    doc.getElementsByAttributeValue("class", "pure-1 md-3-5")[0]
                Log.d("Parser", "2")
                val sectionElement: Element =
                    doc.getElementsByAttributeValue("class", "pure-g entryBar")[0]
                Log.d("Parser", "3")
                val imgElement: Element = doc.getElementsByAttributeValue("class", "mainEntry")[0]
                Log.d("Parser", "4")

                /**
                 * Get info from elements in necessary type
                 * */
                val approvedAnimeTitle: String = titleElement.text().toString()
                Log.d("Parser", "5")
                val description: String = descElement.select("p")[0].text().toString()
                Log.d("Parser", "6")
                val studio: String = sectionElement.select("a")[0].text().toString()
                Log.d("Parser", "7")
                val releaseDate: String = sectionElement.select("span")[1].text().toString()
                Log.d("Parser", "8")
                val imageURI: Uri = imgElement.html()
                    .substringAfter("src=\"")
                    .substringBefore("\">")
                    .toUri()
                Log.d("Parser", "9")

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
        return anim
    }
}