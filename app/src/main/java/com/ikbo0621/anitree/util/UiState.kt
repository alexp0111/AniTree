package com.ikbo0621.anitree.util


/**
 * Class that holds 3 possible states of UI
 *  1. If state is LOADING -> State contains nothing
 *  2. If state is SUCCESS -> State contains data
 *  3. If state is FAILURE -> State contains error message
 * */
sealed class UiState<out T> {
    object Loading: UiState<Nothing>()
    data class Success<out T>(val data: T): UiState<T>()
    data class Failure(val error: String?): UiState<Nothing>()
}