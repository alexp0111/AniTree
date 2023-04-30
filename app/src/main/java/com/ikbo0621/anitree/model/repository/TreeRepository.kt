package com.ikbo0621.anitree.model.repository

import com.ikbo0621.anitree.structure.Tree
import com.ikbo0621.anitree.util.UiState

interface TreeRepository {
    fun updateTree(tree: Tree, result: (UiState<Tree>) -> Unit)
    fun getTreesAccordingTo(animeTitle: String, result: (UiState<List<Tree>>) -> Unit)
    fun like(
        animeTitle: String,
        treeID: String,
        userID: String,
        state: Boolean,
        result: (UiState<Boolean>) -> Unit
    )

    fun checkIfCurrentUserIsLiker(
        animeTitle: String,
        treeID: String,
        userID: String,
        result: (UiState<Boolean>) -> Unit
    )
}