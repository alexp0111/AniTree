package com.ikbo0621.anitree.viewModel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.ikbo0621.anitree.model.repository.TreeRepository
import com.ikbo0621.anitree.structure.Tree
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

    private val _trees = MutableLiveData<UiState<List<Tree>>>()
    val trees: LiveData<UiState<List<Tree>>>
        get() = _trees

    private val _myTrees = MutableLiveData<UiState<List<Tree>>>()
    val myTrees: LiveData<UiState<List<Tree>>>
        get() = _myTrees

    private val _favTrees = MutableLiveData<UiState<List<Tree>>>()
    val favTrees: LiveData<UiState<List<Tree>>>
        get() = _favTrees

    private val _likeState = MutableLiveData<UiState<Boolean>>()
    val likeState: LiveData<UiState<Boolean>>
        get() = _likeState


    fun updateTree(
        tree: Tree
    ) {
        _tree.value = UiState.Loading
        repository.updateTree(
            tree = tree,
        ) { _tree.value = it }
    }

    fun getTreesAccordingTo(
        animeTitle: String
    ) {
        _trees.value = UiState.Loading
        repository.getTreesAccordingTo(
            animeTitle = animeTitle,
        ) { _trees.value = it }
    }

    fun getTreesFor(
        uCreatedTrees: List<String>
    ) {
        _myTrees.value = UiState.Loading
        repository.getTreesFor(
            createdTrees = uCreatedTrees,
        ) { _myTrees.value = it }
    }

    fun getFavTreesFor(
        uFavTrees: List<String>
    ) {
        _favTrees.value = UiState.Loading
        repository.getTreesFor(
            createdTrees = uFavTrees,
        ) { _favTrees.value = it }
    }

    fun like(
        animeTitle: String,
        treeID: String,
        userID: String,
    ) {
        _likeState.value = UiState.Loading
        repository.like(
            animeTitle = animeTitle,
            treeID = treeID,
            userID = userID,
        ) { _likeState.value = it }
    }

    fun checkIfCurrentUserIsLiker(
        animeTitle: String,
        treeID: String,
        userID: String
    ) {
        _likeState.value = UiState.Loading
        repository.checkIfCurrentUserIsLiker(
            animeTitle = animeTitle,
            treeID = treeID,
            userID = userID,
        ) { _likeState.value = it }
    }
}