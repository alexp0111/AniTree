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
        return "Anime(title='$title', description='$description', studio='$studio', releaseDate='$releaseDate', imageURI=$imageURI)"
    }
}