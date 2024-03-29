package com.ikbo0621.anitree.viewModel

import android.content.Context
import android.graphics.Bitmap
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ikbo0621.anitree.model.repository.ParsingRepository
import com.ikbo0621.anitree.structure.Anime
import com.ikbo0621.anitree.structure.Tree
import com.ikbo0621.anitree.util.ParserConstants
import com.ikbo0621.anitree.util.UiState
import com.ikbo0621.anitree.util.fitToExactRequest
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import javax.inject.Inject

/**
 * ViewModel for parsing data
 * */
@HiltViewModel
class ParsingViewModel @Inject constructor(
    val repository: ParsingRepository
) : ViewModel() {

    private val TAG: String = "PARSING_VIEW_MODEL"

    private val _anim = MutableLiveData<UiState<Anime>>()
    val anim: LiveData<UiState<Anime>>
        get() = _anim

    private val _guessedAnim = MutableLiveData<UiState<Anime>>()
    val guessedAnim: LiveData<UiState<Anime>>
        get() = _guessedAnim

    private val _guessList = MutableLiveData<ArrayList<String>>()
    val guessList: LiveData<ArrayList<String>>
        get() = _guessList

    private val _bitmapList = MutableLiveData<UiState<ArrayList<Bitmap?>>?>()
    val bitmapList: MutableLiveData<UiState<ArrayList<Bitmap?>>?>
        get() = _bitmapList

    private var searchGuessJob: Job? = null
    private var searchExactJob: Job? = null
    private var getBitmapList: Job? = null

    fun getAnimeWithTitle(
        animeTitle: String
    ) {

        searchExactJob?.cancel()
        searchExactJob = viewModelScope.launch {
            delay(ParserConstants.SEARCH_INPUT_DELAY) // to not ddos server with each input of letter
            _guessedAnim.value = UiState.Loading
            repository.getAnimeWithName(
                animeTitle = animeTitle.fitToExactRequest(),
            ) { _guessedAnim.value = it }
        }
    }

    fun guessAnime(
        animeTitle: String
    ) {

        searchGuessJob?.cancel()
        searchGuessJob = viewModelScope.launch {
            delay(ParserConstants.SEARCH_INPUT_DELAY) // to not ddos server with each input of letter
            _guessedAnim.value = UiState.Loading
            repository.guessAnime(
                animeTitle = animeTitle,
            ) { uiState, strings ->
                _guessedAnim.value = uiState
                _guessList.value = strings
            }
        }
    }

    fun getBitmapListForTree(
        tree: Tree,
        context: Context
    ) {
        _bitmapList.value = UiState.Loading
        getBitmapList = viewModelScope.launch {
            repository.getBitmapListForTree(
                tree = tree,
                context = context
            ) { images ->
                _bitmapList.value = images
            }
        }
    }

    fun cancelSearch() {
        searchGuessJob?.cancel()
        _guessedAnim.value = UiState.Failure("Please, enter something")
    }

    fun clearAnimeValue() {
        searchGuessJob?.cancel()
        _guessedAnim.value = UiState.Failure("empty")
    }
}