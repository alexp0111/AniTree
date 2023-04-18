package com.ikbo0621.anitree.viewModel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ikbo0621.anitree.model.repository.ParsingRepository
import com.ikbo0621.anitree.structure.Anime
import com.ikbo0621.anitree.util.UiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.channels.consumeEach
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

    private val channel = Channel<Job>(capacity = Channel.UNLIMITED).apply {
        viewModelScope.launch {
            consumeEach { it.join() }
        }
    }

    fun getAnimeWithTitle(
        animeTitle: String
    ) {
        _anim.value = UiState.Loading
        channel.trySend(
            viewModelScope.launch {
                repository.getAnimeWithName(
                    animeTitle = animeTitle,
                ) { _anim.value = it }
            }
        )
    }

    fun guessAnime(
        animeTitle: String
    ) {
        _guessedAnim.value = UiState.Loading
        channel.trySend(
            viewModelScope.launch {
                repository.guessAnime(
                    animeTitle = animeTitle,
                ) { _guessedAnim.value = it }
            }
        )
    }
}