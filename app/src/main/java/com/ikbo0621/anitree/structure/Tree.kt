package com.ikbo0621.anitree.structure

data class Tree(
    var id: String = "",
    var authorID: String = "",
    var children: List<String?> = arrayListOf(),
    var likers: List<String> = arrayListOf()
    // [ root,
    // 1, 2, 3,
    // 1.1, 1.2, 1.3, 2.1, 2.2, 2.3, 3.1, 3.2, 3.3 ]
) {

    override fun toString(): String {
        return "Tree(id='$id', authorID='$authorID', children=$children, likers=$likers)\n\n"
    }
}