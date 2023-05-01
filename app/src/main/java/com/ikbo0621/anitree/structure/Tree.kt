package com.ikbo0621.anitree.structure

import android.os.Parcelable
import android.util.Log
import kotlinx.parcelize.Parcelize

@Parcelize
data class Tree(
    var id: String = "",
    var authorID: String = "",
    var children: List<String?> = arrayListOf(),
    var studios: List<String?> = arrayListOf(),
    var urls: List<String?> = arrayListOf(),
    var likers: MutableList<String> = arrayListOf()
    // [ root,
    // 1, 2, 3,
    // 1.1, 1.2, 1.3, 2.1, 2.2, 2.3, 3.1, 3.2, 3.3 ]
) : Parcelable {

    override fun toString(): String {
        var presentation = ""
        presentation += "ID: $id\n"
        presentation += "Author: $authorID\n\n"

        Log.d("TREE", children.toString())

        presentation += "${children[0] + '/' + studios[0]}\n"
            presentation += "\t${children[1] + '/' + studios[1]}\n"
                presentation += "\t\t${children[4] + '/' + studios[4]}\n"
                presentation += "\t\t${children[5] + '/' + studios[5]}\n"
                presentation += "\t\t${children[6] + '/' + studios[6]}\n"
            presentation += "\t${children[2] + '/' + studios[2]}"
                presentation += "\t\t${children[7] + '/' + studios[7]}\n"
                presentation += "\t\t${children[8] + '/' + studios[8]}\n"
                presentation += "\t\t${children[9] + '/' + studios[9]}\n"
            presentation += "\t${children[3] + '/' + studios[3]}"
                presentation += "\t\t${children[10] + '/' + studios[10]}\n"
                presentation += "\t\t${children[11] + '/' + studios[11]}\n"
                presentation += "\t\t${children[12] + '/' + studios[12]}\n"

        return presentation
    }
}