package com.ikbo0621.anitree.model.implementation

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.ikbo0621.anitree.model.repository.TreeRepository
import com.ikbo0621.anitree.structure.Tree
import com.ikbo0621.anitree.util.UiState

class TreeModel(
    val auth: FirebaseAuth,
    val database: FirebaseFirestore
): TreeRepository {
    override fun updateTree(tree: Tree, result: (UiState<Tree>) -> Unit) {
        TODO("Not yet implemented")
    }

    override fun like(tree: Tree, result: (UiState<Tree>) -> Unit) {
        TODO("Not yet implemented")
    }

    override fun dislike(tree: Tree, result: (UiState<Tree>) -> Unit) {
        TODO("Not yet implemented")
    }
}