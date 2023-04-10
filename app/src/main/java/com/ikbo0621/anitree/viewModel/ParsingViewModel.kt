package com.ikbo0621.anitree.viewModel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.ikbo0621.anitree.model.repository.ParsingRepository
import com.ikbo0621.anitree.structure.Anime
import com.ikbo0621.anitree.util.UiState
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class ParsingViewModel @Inject constructor(
    val repository: ParsingRepository
) : ViewModel() {
    private val _anim = MutableLiveData<UiState<Anime>>()
    val anim: LiveData<UiState<Anime>>
        get() = _anim

    fun getAnimeWithTitle(
        animeTitle: String
    ) {
        _anim.value = UiState.Loading
        repository.getAnimeWithName(
            animeTitle = animeTitle,
        ) { _anim.value = it }
    }
}