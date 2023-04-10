package com.ikbo0621.anitree.structure

import android.net.Uri
import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class Anime(
    var title: String = "-1",
    var description: String = "",
    var studio: String = "",
    var releaseDate: String = "",
    var imageURI: Uri = Uri.EMPTY
) : Parcelable {
    override fun toString(): String {
        return "Anime(\ntitle='$title'\n\ndescription='$description'\n\nstudio='$studio'\n\nreleaseDate='$releaseDate'\n\nimageURI=$imageURI)"
    }
}