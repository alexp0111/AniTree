package com.ikbo0621.anitree.viewModel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ikbo0621.anitree.model.repository.ParsingRepository
import com.ikbo0621.anitree.structure.Anime
import com.ikbo0621.anitree.util.UiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import javax.inject.Inject

/**
 * ViewModel for parsing data
 * */
@HiltViewModel
class ParsingViewModel @Inject constructor(
    val repository: ParsingRepository
) : ViewModel() {
    private val _anim = MutableLiveData<UiState<Anime>>()
    val anim: LiveData<UiState<Anime>>
        get() = _anim

    private val _guessedAnim = MutableLiveData<UiState<Anime>>()
    val guessedAnim: LiveData<UiState<Anime>>
        get() = _guessedAnim

    private var searchGuessJob: Job? = null
    private var searchExactJob: Job? = null

    fun getAnimeWithTitle(
        animeTitle: String
    ) {
        _anim.value = UiState.Loading
        searchExactJob?.cancel()
        searchExactJob = viewModelScope.launch {
            repository.getAnimeWithName(
                animeTitle = animeTitle,
            ) { _anim.value = it }
        }
    }

    fun guessAnime(
        animeTitle: String
    ) {
        _guessedAnim.value = UiState.Loading
        searchGuessJob?.cancel()
        searchGuessJob = viewModelScope.launch {
            repository.guessAnime(
                animeTitle = animeTitle,
            ) { _guessedAnim.value = it }
        }
    }
}