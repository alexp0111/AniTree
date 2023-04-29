package com.ikbo0621.anitree.structure

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class Tree(
    var id: String = "",
    var authorID: String = "",
    var children: List<String?> = arrayListOf(),
    var likers: List<String> = arrayListOf()
    // [ root,
    // 1, 2, 3,
    // 1.1, 1.2, 1.3, 2.1, 2.2, 2.3, 3.1, 3.2, 3.3 ]
) : Parcelable {

    override fun toString(): String {
        var presentation = ""
        presentation += "ID: $id\n"
        presentation += "Author: $authorID\n\n"

        presentation += "${children[0]}\n"
            presentation += "\t${children[1]}\n"
                presentation += "\t\t${children[4]}\n"
                presentation += "\t\t${children[5]}\n"
                presentation += "\t\t${children[6]}\n"
            presentation += "\t${children[2]}"
                presentation += "\t\t${children[7]}\n"
                presentation += "\t\t${children[8]}\n"
                presentation += "\t\t${children[9]}\n"
            presentation += "\t${children[3]}"
                presentation += "\t\t${children[10]}\n"
                presentation += "\t\t${children[11]}\n"
                presentation += "\t\t${children[12]}\n"

        return presentation
    }
}