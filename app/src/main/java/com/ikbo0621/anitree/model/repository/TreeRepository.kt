package com.ikbo0621.anitree.model.repository

import com.ikbo0621.anitree.structure.Tree
import com.ikbo0621.anitree.util.UiState

interface TreeRepository {
    fun updateTree(tree: Tree, result: (UiState<Tree>) -> Unit)
    fun like(tree: Tree, result: (UiState<Tree>) -> Unit)
    fun dislike(tree: Tree, result: (UiState<Tree>) -> Unit)
}