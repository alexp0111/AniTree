package com.ikbo0621.anitree.viewModel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ikbo0621.anitree.model.repository.ParsingRepository
import com.ikbo0621.anitree.structure.Anime
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

    private var searchGuessJob: Job? = null
    private var searchExactJob: Job? = null

    fun getAnimeWithTitle(
        animeTitle: String
    ) {
        _guessedAnim.value = UiState.Loading
        searchExactJob?.cancel()
        searchExactJob = viewModelScope.launch {
            delay(ParserConstants.SEARCH_INPUT_DELAY) // to not ddos server with each input of letter
            repository.getAnimeWithName(
                animeTitle = animeTitle.fitToExactRequest(),
            ) { _guessedAnim.value = it }
        }
    }

    fun guessAnime(
        animeTitle: String
    ) {
        _guessedAnim.value = UiState.Loading
        searchGuessJob?.cancel()
        searchGuessJob = viewModelScope.launch {
            delay(ParserConstants.SEARCH_INPUT_DELAY) // to not ddos server with each input of letter
            repository.guessAnime(
                animeTitle = animeTitle,
            ) { uiState, strings ->
                _guessedAnim.value = uiState
                _guessList.value = strings
            }
        }
    }

    fun cancelSearch(){
        searchGuessJob?.cancel()
        _guessedAnim.value = UiState.Failure("Please, enter something")
    }
}