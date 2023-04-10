package com.ikbo0621.anitree.structure

import android.net.Uri

data class Anime(
    var title: String,
    var description: String,
    var studio: String,
    var releaseDate: String,
    var imageURI: Uri
){
    override fun toString(): String {
        return "Anime(\ntitle='$title'\n\ndescription='$description'\n\nstudio='$studio'\n\nreleaseDate='$releaseDate'\n\nimageURI=$imageURI)"
    }
}