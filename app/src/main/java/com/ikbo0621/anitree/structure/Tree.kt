package com.ikbo0621.anitree.structure

data class Tree(
    var id: String = "",
    var rootName: String = "",
    var numOfLikes: Int = 0,
    var children: List<List<String>> = arrayListOf(arrayListOf())
) {
}