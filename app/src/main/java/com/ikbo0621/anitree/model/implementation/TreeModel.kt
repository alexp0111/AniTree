package com.ikbo0621.anitree.model.implementation

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.ikbo0621.anitree.model.repository.TreeRepository
import com.ikbo0621.anitree.structure.Tree
import com.ikbo0621.anitree.util.FireStoreCollection
import com.ikbo0621.anitree.util.UiState

class TreeModel(
    val auth: FirebaseAuth,
    val database: FirebaseFirestore
) : TreeRepository {

    private val TAG: String = "TREE_MODEL"

    override fun updateTree(tree: Tree, result: (UiState<Tree>) -> Unit) {
        val id = database
            .collection(FireStoreCollection.TREE)
            .document(tree.children[0].toString())
            .collection(FireStoreCollection.INNER_PATH)
            .document()
            .id
        tree.id = id

        val document =
            database.collection(FireStoreCollection.TREE)
                .document(tree.children[0].toString())
                .collection(FireStoreCollection.INNER_PATH)
                .document(id)

        document
            .set(tree)
            .addOnSuccessListener {
                result.invoke(
                    UiState.Success(tree)
                )
            }
            .addOnFailureListener {
                result.invoke(
                    UiState.Failure(
                        it.localizedMessage
                    )
                )
            }
    }

    override fun like(tree: Tree, result: (UiState<Tree>) -> Unit) {
        TODO("Not yet implemented")
    }

    override fun dislike(tree: Tree, result: (UiState<Tree>) -> Unit) {
        TODO("Not yet implemented")
    }
}