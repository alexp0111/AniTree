package com.ikbo0621.anitree.structure

data class User(
    var id: String = "",
    var iconId: String = "",
    var name: String = "",
    var favoriteTrees: MutableList<String> = arrayListOf(),
    var createdTrees: MutableList<String> = arrayListOf()
){
    override fun toString(): String {
        return "User(id='$id', iconId='$iconId', name='$name', favoriteTrees=$favoriteTrees, createdTrees=$createdTrees)"
    }
}