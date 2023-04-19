package com.ikbo0621.anitree.structure

data class Tree(
    var id: String = "",
    var numOfLikes: Int = 0,
    var children: List<String> = arrayListOf()
    // [ root,
    // 1, 2, 3,
    // 1.1, 1.2, 1.3, 2.1, 2.2, 2.3, 3.1, 3.2, 3.3 ]
) {
}