package com.ikbo0621.anitree.viewModel

import androidx.fragment.app.viewModels
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.ikbo0621.anitree.model.repository.TreeRepository
import com.ikbo0621.anitree.structure.Tree
import com.ikbo0621.anitree.structure.User
import com.ikbo0621.anitree.util.UiState
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

/**
 * ViewModel for Tree
 * */
@HiltViewModel
class TreeViewModel @Inject constructor(
    val repository: TreeRepository
) : ViewModel() {

    private val TAG: String = "TREE_VIEW_MODEL"

    private val _tree = MutableLiveData<UiState<Tree>>()
    val tree: LiveData<UiState<Tree>>
        get() = _tree


    fun updateTree(
        tree: Tree
    ) {
        _tree.value = UiState.Loading
        repository.updateTree(
            tree = tree,
        ) { _tree.value = it }
    }
}