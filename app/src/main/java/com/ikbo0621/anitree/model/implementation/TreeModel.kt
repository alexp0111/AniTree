package com.ikbo0621.anitree.model.implementation

import android.os.Bundle
import android.util.Log
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
        val id: String

        if (tree.id.isEmpty()) {
            id = database
                .collection(FireStoreCollection.TREE)
                .document(tree.children[0].toString())
                .collection(FireStoreCollection.INNER_PATH)
                .document()
                .id
            tree.id = id
        } else {
            id = tree.id
        }

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

    override fun getTreesAccordingTo(animeTitle: String, result: (UiState<List<Tree>>) -> Unit) {
        database
            .collection(FireStoreCollection.TREE)
            .document(animeTitle)
            .collection(FireStoreCollection.INNER_PATH)
            .get()
            .addOnCompleteListener {
                if (it.isSuccessful) {
                    val trees = arrayListOf<Tree>()
                    it.result.documents.forEach { thisTree ->
                        thisTree.toObject(Tree::class.java).apply {
                            if (this != null) {
                                trees.add(this)
                                Log.d(TAG + "myPair", this.urls[0].toString())
                            }
                        }
                    }
                    result.invoke(UiState.Success(trees))
                } else {
                    result.invoke(
                        UiState.Failure("Task is not successful")
                    )
                }
            }
            .addOnFailureListener {
                result.invoke(
                    UiState.Failure("Something went wrong")
                )
            }
    }

    override fun getTreesFor(createdTrees: List<String>, result: (UiState<List<Tree>>) -> Unit) {
        database.collectionGroup("trees")
            .get()
            .addOnCompleteListener {
                if (it.isSuccessful) {
                    val trees = arrayListOf<Tree>()
                    it.result.documents.forEach { thisTree ->
                        thisTree.toObject(Tree::class.java).apply {
                            if (this != null && createdTrees.contains(this.id)) {
                                trees.add(this)
                                Log.d(TAG + "myPair", this.urls[0].toString())
                            }
                        }
                    }
                    result.invoke(UiState.Success(trees))
                } else {
                    result.invoke(
                        UiState.Failure("Task is not successful")
                    )
                }
            }
            .addOnFailureListener {
                result.invoke(
                    UiState.Failure("Something went wrong")
                )
            }
    }

    override fun like(
        animeTitle: String,
        treeID: String,
        userID: String,
        result: (UiState<Boolean>) -> Unit
    ) {
        database
            .collection(FireStoreCollection.TREE)
            .document(animeTitle)
            .collection(FireStoreCollection.INNER_PATH)
            .document(treeID)
            .get()
            .addOnCompleteListener {
                if (it.isSuccessful) {
                    it.result.toObject(Tree::class.java).apply {
                        if (this != null) {
                            if (this.likers.contains(userID)) {
                                this.likers.remove(userID)
                            } else {
                                this.likers.add(userID)
                            }
                            updateTree(tree = this) { state ->
                                when (state) {
                                    is UiState.Loading -> {}
                                    is UiState.Failure -> {
                                        result.invoke(
                                            UiState.Failure("error while uploading tree")
                                        )
                                    }
                                    is UiState.Success -> {
                                        result.invoke(
                                            UiState.Success(state.data.likers.contains(userID))
                                        )
                                    }
                                }
                            }
                        }
                    }
                } else {
                    result.invoke(
                        UiState.Failure("Task is not successful")
                    )
                }
            }
            .addOnFailureListener {
                result.invoke(
                    UiState.Failure("Something went wrong")
                )
            }
    }

    override fun checkIfCurrentUserIsLiker(
        animeTitle: String,
        treeID: String,
        userID: String,
        result: (UiState<Boolean>) -> Unit
    ) {
        database
            .collection(FireStoreCollection.TREE)
            .document(animeTitle)
            .collection(FireStoreCollection.INNER_PATH)
            .document(treeID)
            .get()
            .addOnCompleteListener {
                if (it.isSuccessful) {
                    it.result.toObject(Tree::class.java).apply {
                        if (this != null) {
                            result.invoke(UiState.Success(this.likers.contains(userID)))
                        }
                    }
                } else {
                    result.invoke(
                        UiState.Failure("Task is not successful")
                    )
                }
            }
            .addOnFailureListener {
                result.invoke(
                    UiState.Failure("Something went wrong")
                )
            }
    }
}